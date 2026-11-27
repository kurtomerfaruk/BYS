package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.*;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtMalzemeTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtRandevu;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalepDurumGecmisi;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyYetkiService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Merkezi yönetim: Ortez-Protez talebi durum geçişleri, yetki ve iş kuralı kontrolleri.
 * Tüm geçişler {@link EnumOrtTalepIslem} üzerinden doğrulanır; XHTML doğrudan durum değiştirmez.
 *
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 16:46
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrtTalepWorkflowService implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -6419034585239127017L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Inject
    private SyYetkiService syYetkiService;
    @Inject
    private OrtRandevuService ortRandevuService;
    @Inject
    private OrtTalepBelgeService ortTalepBelgeService;
    @Inject
    private OrtTalepOdemeService ortTalepOdemeService;
    @Inject
    private OrtMalzemeTalepService ortMalzemeTalepService;
    @Inject
    private OrtTeslimatService ortTeslimatService;

    /**
     * İşlemi uygular: yetki, geçiş kuralı ve guard kontrollerinden sonra durumu değiştirir
     * ve geçmiş kaydı oluşturur. Hatalarda transaction geri alınır.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void applyTransition(Integer talepId, EnumOrtTalepIslem action, String description) {
        if (talepId == null) {
            throw new IllegalArgumentException("İşlem yapılacak talep bulunamadı.");
        }
        if (action == null) {
            throw new IllegalArgumentException("Geçersiz talep işlemi.");
        }

        OrtTalep talep = em.find(OrtTalep.class, talepId);
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }

        SyKullanici kullanici = Util.getSyKullanici();
        checkPermission(kullanici, action);
        checkTransition(talep, action);
        checkGuards(talep, action, description);

        // Eşzamanlı güncellemeleri optimistic lock ile engelle
        em.lock(talep, LockModeType.OPTIMISTIC);

        EnumOrtTalepDurumu eskiDurum = talep.getDurum();
        // Ücretli talepte proje seçimi sadece bilgi ekler; ödeme akışı
        // (fiyat -> sözleşme -> ödeme -> dekont) kesilmez.
        boolean sadeceProjeBilgisi = action == EnumOrtTalepIslem.PROJE_BELIRLE
                && talep.getTalepTipi() == EnumOrtTalepTipi.UCRETLI;
        EnumOrtTalepDurumu yeniDurum = sadeceProjeBilgisi ? eskiDurum : action.getTargetStatus();
        talep.setDurum(yeniDurum);
        em.merge(talep);
        em.flush();

        OrtTalepDurumGecmisi gecmis = OrtTalepDurumGecmisi.builder()
                .ortTalep(talep)
                .eskiDurum(eskiDurum)
                .yeniDurum(yeniDurum)
                .islemTarihi(LocalDateTime.now())
                .islemiYapanSyKullanici(kullanici)
                .aciklama(description == null || description.isBlank() ? action.getDisplayValue() : description)
                .build();
        em.persist(gecmis);
        em.flush();
    }

    /**
     * "Hasta Geldi" türündeki işlemler için: ilgili randevuyu da GELDI yapar,
     * sonra talep durumunu geçirir. Böylece ayrıca randevu sekmesine gidilmesine gerek kalmaz.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void applyAttendanceTransition(Integer talepId, EnumOrtTalepIslem action, String description) {
        if (talepId == null || action == null) {
            return;
        }
        EnumOrtRandevuTuru randevuTuru = attendanceTypeOf(action);
        if (randevuTuru != null) {
            OrtTalep talep = em.find(OrtTalep.class, talepId);
            if (talep != null) {
                java.util.List<OrtRandevu> randevular = em.createQuery(
                                "SELECT r FROM OrtRandevu r WHERE r.aktif=true AND r.ortTalep.id=:talepId " +
                                        "AND r.randevuTuru=:tur ORDER BY r.randevuTarihi DESC",
                                OrtRandevu.class)
                        .setParameter("talepId", talepId)
                        .setParameter("tur", randevuTuru)
                        .getResultList();
                if (!randevular.isEmpty()) {
                    OrtRandevu randevu = randevular.get(0);
                    if (randevu.getDurum() != EnumOrtRandevuDurumu.GELDI
                            && randevu.getDurum() != EnumOrtRandevuDurumu.TAMAMLANDI) {
                        randevu.setDurum(EnumOrtRandevuDurumu.GELDI);
                        em.merge(randevu);
                        em.flush();
                    }
                }
            }
        }
        applyTransition(talepId, action, description);
    }

    private EnumOrtRandevuTuru attendanceTypeOf(EnumOrtTalepIslem action) {
        return switch (action) {
            case ON_MUAYENEYE_GEL -> EnumOrtRandevuTuru.ON_MUAYENE;
            case OLCU_AL -> EnumOrtRandevuTuru.OLCU;
            case PROVA_YAP -> EnumOrtRandevuTuru.PROVA;
            default -> null;
        };
    }


    /**
     * Randevu "Geldi"/"Tamamlandı" durumuna geçtiğinde ilgili talep geçişini uygular.
     * Randevu tipine göre uygun işlem seçilir; talep henüz "Yeni Talep" olsa bile
     * ilgili adıma geçiş yapılır.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void applyAppointmentTransition(Integer randevuId, EnumOrtRandevuDurumu randevuDurumu) {
        if (randevuId == null || randevuDurumu == null) {
            return;
        }
        if (randevuDurumu != EnumOrtRandevuDurumu.GELDI
                && randevuDurumu != EnumOrtRandevuDurumu.TAMAMLANDI) {
            return;
        }

        OrtRandevu randevu = em.find(OrtRandevu.class, randevuId);
        if (randevu == null || randevu.getOrtTalep() == null || randevu.getOrtTalep().getId() == null) {
            return;
        }

        EnumOrtTalepIslem action = resolveAppointmentAction(randevu.getRandevuTuru());
        if (action == null) {
            return;
        }

        String aciklama = (randevu.getRandevuTuru() == null ? "Randevu" : randevu.getRandevuTuru().getDisplayValue())
                + " - " + randevuDurumu.getDisplayValue();
        applyTransition(randevu.getOrtTalep().getId(), action, aciklama);
    }

    private EnumOrtTalepIslem resolveAppointmentAction(EnumOrtRandevuTuru randevuTuru) {
        if (randevuTuru == null) {
            return null;
        }
        return switch (randevuTuru) {
            case ON_MUAYENE -> EnumOrtTalepIslem.ON_MUAYENEYE_GEL;
            case OLCU -> EnumOrtTalepIslem.OLCU_AL;
            case PROVA, YENIDEN_PROVA -> EnumOrtTalepIslem.PROVA_YAP;
            default -> null;
        };
    }

    /**
     * "Randevu Oluştur" işlemleri için: randevu kaydını ve talep durum geçişini
     * tek transaction'da yapar. Aynı talep/tipte planlanmış randevu varsa tekrar oluşturmaz.
     */
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public void createAppointmentAndTransition(Integer talepId, EnumOrtTalepIslem action, String description) {
//        if (talepId == null || action == null) {
//            return;
//        }
//        EnumOrtRandevuTuru randevuTuru = appointmentTypeOf(action);
//        if (randevuTuru == null) {
//            applyTransition(talepId, action, description);
//            return;
//        }
//
//        OrtTalep talep = em.find(OrtTalep.class, talepId);
//        if (talep != null && !ortRandevuService.existsPlanned(talep, randevuTuru)) {
//            OrtRandevu randevu = OrtRandevu.builder()
//                    .ortTalep(talep)
//                    .ortKisi(talep.getOrtKisi())
//                    .randevuTuru(randevuTuru)
//                    .randevuTarihi(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0))
//                    .durum(tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtRandevuDurumu.PLANLANDI)
//                    .build();
//            em.persist(randevu);
//            em.flush();
//        }
//        applyTransition(talepId, action, description);
//    }

//    private EnumOrtRandevuTuru appointmentTypeOf(EnumOrtTalepIslem action) {
//        return switch (action) {
//            case ON_MUAYENE_RANDEVUSU -> EnumOrtRandevuTuru.ON_MUAYENE;
//            case OLCU_RANDEVUSU -> EnumOrtRandevuTuru.OLCU;
//            case PROVA_RANDEVUSU -> EnumOrtRandevuTuru.PROVA;
//            case YENIDEN_PROVA_RANDEVUSU -> EnumOrtRandevuTuru.YENIDEN_PROVA;
//            default -> null;
//        };
//    }

    /**
     * Önceki adıma geri döner. Kullanıcı yetkisi ve açıklama zorunludur.
     * Kapatılmış talepler geri alınamaz; her geri dönüş geçmişe kaydedilir.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void goBack(Integer talepId, EnumOrtTalepDurumu hedefDurum, String description) {
        if (talepId == null || hedefDurum == null) {
            throw new IllegalArgumentException("Geri dönüş için hedef durum belirtilmelidir.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Geri dönüş nedeni (açıklama) zorunludur.");
        }
        OrtTalep talep = em.find(OrtTalep.class, talepId);
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }
        SyKullanici kullanici = Util.getSyKullanici();
        if (!syYetkiService.hasPermission(kullanici, "orttalep:guncelle")) {
            throw new IllegalArgumentException("Geri dönüş yetkiniz bulunmuyor.");
        }
        if (talep.getDurum() == EnumOrtTalepDurumu.KAPATILDI) {
            throw new IllegalArgumentException("Kapatılmış talep geri alınamaz.");
        }
        if (!talep.getDurum().getPreviousStatuses().contains(hedefDurum)) {
            throw new IllegalArgumentException("Bu durumdan hedef adıma geri dönülemez.");
        }

        em.lock(talep, LockModeType.OPTIMISTIC);
        EnumOrtTalepDurumu eskiDurum = talep.getDurum();
        talep.setDurum(hedefDurum);
        em.merge(talep);
        em.flush();

        OrtTalepDurumGecmisi gecmis = OrtTalepDurumGecmisi.builder()
                .ortTalep(talep)
                .eskiDurum(eskiDurum)
                .yeniDurum(hedefDurum)
                .islemTarihi(LocalDateTime.now())
                .islemiYapanSyKullanici(kullanici)
                .aciklama("Geri dönüş: " + description)
                .build();
        em.persist(gecmis);
        em.flush();
    }

    /**
     * Talep için geri dönüş seçenekleri.
     */
    public Set<EnumOrtTalepDurumu> getBackOptions(OrtTalep talep) {
        if (talep == null || talep.getDurum() == null) {
            return Set.of();
        }
        return talep.getDurum().getPreviousStatuses();
    }


    /**
     * Teslimat işlemi: teslimat kaydını oluşturur, stok çıkışını yapar ve
     * talep durumunu geçirir. Tümü tek transaction'da çalışır.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void applyDelivery(Integer talepId, String description) {
        if (talepId == null) {
            throw new IllegalArgumentException("Teslimat yapılacak talep bulunamadı.");
        }
        OrtTalep talep = em.find(OrtTalep.class, talepId);
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }
        SyKullanici kullanici = Util.getSyKullanici();
        EnumOrtTalepIslem action = EnumOrtTalepIslem.TESLIM_ET;
        checkPermission(kullanici, action);
        checkTransition(talep, action);
        checkGuards(talep, action, description);

        ortTeslimatService.createDelivery(talep, description);
        applyTransition(talepId, action, description);
    }

    /**
     * Depo onayı tamamlandı mı? Bekleyen veya reddedilmiş malzeme talebi varsa
     * talep "DEPO_ONAYI_BEKLIYOR" durumunda kilitlidir; hiçbir ileri işlem yapılamaz.
     */
    public boolean isWarehouseApprovalCompleted(OrtTalep talep) {
        if (talep == null || talep.getId() == null) {
            return false;
        }
        List<OrtMalzemeTalep> talepler = ortMalzemeTalepService.findByTalep(talep);
        return !talepler.isEmpty() && talepler.stream()
                .allMatch(m -> m.getDurum() == EnumOrtMalzemeDurumu.ONAYLANDI);
    }

    /**
     * Depo onayı sonucunu işler.
     * Onay: talep DEPO_ONAYI_BEKLIYOR durumunda kalır; kilit açılır ve
     * PROVA_RANDEVUSU (randevu oluşturma) adımı kullanılabilir olur.
     * Red: talep MALZEME_TALEBI_BEKLIYOR adımına geri döner.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void applyWarehouseDecision(Integer ortTalepId, boolean approved, String description) {
        if (ortTalepId == null) {
            return;
        }
        OrtTalep talep = em.find(OrtTalep.class, ortTalepId);
        if (talep == null || talep.getDurum() != EnumOrtTalepDurumu.DEPO_ONAYI_BEKLIYOR) {
            return;
        }

        em.lock(talep, LockModeType.OPTIMISTIC);
        EnumOrtTalepDurumu eski = talep.getDurum();
        EnumOrtTalepDurumu hedef = eski;
        if (!approved) {
            hedef = EnumOrtTalepDurumu.MALZEME_TALEBI_BEKLIYOR;
            talep.setDurum(hedef);
        }
        em.merge(talep);

        OrtTalepDurumGecmisi gecmis = OrtTalepDurumGecmisi.builder()
                .ortTalep(talep)
                .eskiDurum(eski)
                .yeniDurum(hedef)
                .islemTarihi(LocalDateTime.now())
                .islemiYapanSyKullanici(Util.getSyKullanici())
                .aciklama((approved ? "Depo onayı tamamlandı. " : "Depo reddi: ")
                        + (description == null ? "" : description))
                .build();
        em.persist(gecmis);
        em.flush();
    }

    /**
     * Depo onayı bekleniyor ve kilit hâlâ aktif mi?
     */
    public boolean isWaitingForWarehouseApproval(OrtTalep talep) {
        return talep != null
                && talep.getDurum() == EnumOrtTalepDurumu.DEPO_ONAYI_BEKLIYOR
                && !isWarehouseApprovalCompleted(talep);
    }

    /**
     * UI butonları için: yetki ve mevcut duruma uygunluk kontrolü.
     */
    public boolean isActionAvailable(OrtTalep talep, EnumOrtTalepIslem action) {
        if (talep == null || action == null) {
            return false;
        }
        if (!action.getSourceStatuses().contains(talep.getDurum())) {
            return false;
        }
        if (isWarehouseLocked(talep, action)) {
            return false;
        }
        if (!isActionReady(talep, action)) {
            return false;
        }
        return syYetkiService.hasPermission(Util.getSyKullanici(), action.getPermission());
    }

    /**
     * Randevu gerektiren adımlarda doğru sırayı garanti eder:
     * randevu yoksa "randevu oluştur", randevu varsa işlemi tamamla butonu açılır.
     * Böylece guard'ın her zaman reddettiği bir buton kullanıcıya gösterilmez
     * (ön muayene, ölçü ve prova için aynı kural geçerlidir).
     */
    private boolean isActionReady(OrtTalep talep, EnumOrtTalepIslem action) {
        if (talep == null || action == null) {
            return false;
        }
        if (action == EnumOrtTalepIslem.PROVA_YAP) {
            return ortRandevuService.hasAppointment(talep, EnumOrtRandevuTuru.PROVA)
                    || ortRandevuService.hasAppointment(talep, EnumOrtRandevuTuru.YENIDEN_PROVA);
        }
        if (action == EnumOrtTalepIslem.ON_MUAYENEYE_GEL) {
            return ortRandevuService.hasAppointment(talep, EnumOrtRandevuTuru.ON_MUAYENE);
        }
        if (action == EnumOrtTalepIslem.OLCU_AL) {
            return ortRandevuService.hasAppointment(talep, EnumOrtRandevuTuru.OLCU);
        }
        if (action == EnumOrtTalepIslem.ON_MUAYENE_RANDEVUSU
                && talep.getDurum() == EnumOrtTalepDurumu.ON_MUAYENE_RANDEVUSU_BEKLIYOR) {
            return !ortRandevuService.hasAppointment(talep, EnumOrtRandevuTuru.ON_MUAYENE);
        }
        if (action == EnumOrtTalepIslem.OLCU_RANDEVUSU
                && talep.getDurum() == EnumOrtTalepDurumu.OLCU_RANDEVUSU_BEKLIYOR) {
            return !ortRandevuService.hasAppointment(talep, EnumOrtRandevuTuru.OLCU);
        }
        if (action == EnumOrtTalepIslem.PROVA_RANDEVUSU
                && talep.getDurum() == EnumOrtTalepDurumu.PROVA_RANDEVUSU_BEKLIYOR) {
            return !ortRandevuService.hasAppointment(talep, EnumOrtRandevuTuru.PROVA);
        }
        return true;
    }

    /**
     * Depo onayı beklenirken ileri işlemlere izin verilmez (geri dönüş dışında).
     */
    private boolean isWarehouseLocked(OrtTalep talep, EnumOrtTalepIslem action) {
        if (talep.getDurum() != EnumOrtTalepDurumu.DEPO_ONAYI_BEKLIYOR) {
            return false;
        }
        if (action == EnumOrtTalepIslem.PROVA_RANDEVUSU
                || action == EnumOrtTalepIslem.PROVA_YAP) {
            return !isWarehouseApprovalCompleted(talep);
        }
        return false;
    }

    /**
     * Mevcut durumda kullanılabilir işlemler (yetki filtreli).
     */
    public Set<EnumOrtTalepIslem> getAvailableActions(OrtTalep talep) {
        if (talep == null) {
            return Set.of();
        }
        return EnumOrtTalepIslem.getActions(talep.getDurum()).stream()
                .filter(action -> !isWarehouseLocked(talep, action))
                .filter(action -> isActionReady(talep, action))
                .filter(action -> syYetkiService.hasPermission(Util.getSyKullanici(), action.getPermission()))
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(EnumOrtTalepIslem.class)));
    }

    /**
     * Durum için bekleyen işlem açıklaması (liste ve detay ekranlarında gösterilir).
     */
    public String getPendingAction(EnumOrtTalepDurumu durum) {
        if (durum == null) {
            return "-";
        }
        return switch (durum) {
            case YENI_TALEP -> "Ön muayene randevusu oluşturulacak";
            case ON_MUAYENE_RANDEVUSU_BEKLIYOR -> "Hasta ön muayeneye gelecek";
            case ON_MUAYENEYE_GELDI -> "Tekniker ve fizyoterapist atanacak";
            case PERSONEL_ATAMASI_BEKLIYOR -> "Değerlendirme yapılacak";
            case DEGERLENDIRME_BEKLIYOR -> "Uygunluk kararı verilecek";
            case TALEP_REDDEDILDI -> "Talep kapatılacak";
            case UCRET_BELIRLEME_BEKLIYOR -> "Fiyat belirlenecek";
            case SOZLESME_BEKLIYOR -> "Ücretli hasta sözleşmesi imzalanacak";
            case ODEME_BEKLIYOR -> "Ödeme alınacak ve dekont kaydedilecek";
            case PROJE_BELIRLENDI -> "Ölçü randevusu oluşturulacak";
            case OLCU_RANDEVUSU_BEKLIYOR -> "Hasta ölçüye gelecek";
            case OLCU_ALINDI -> "Ölçü formları doldurulacak ve taranacak";
            case FORMLAR_BEKLENIYOR -> "Doktor raporu alınacak";
            case DOKTOR_RAPORU_BEKLENIYOR -> "Reçete alınacak";
            case RECETE_BEKLENIYOR -> "Malzeme talebi oluşturulacak";
            case MALZEME_TALEBI_BEKLIYOR -> "Depo onayı beklenecek";
            case DEPO_ONAYI_BEKLIYOR -> "Malzeme talebi depo onayında. Onaylanana kadar işlem yapılamaz.";
            case PROVA_RANDEVUSU_BEKLIYOR -> "Hasta provaya gelecek";
            case PROVA_YAPILDI -> "Prova sonucuna göre teslimat veya yeniden prova";
            case YENIDEN_PROVA_BEKLIYOR -> "Yeni prova randevusu oluşturulacak";
            case TESLIMAT_BEKLIYOR -> "Ürün teslim edilecek ve tutanak imzalanacak";
            case TESLIM_EDILDI -> "Talep tamamlanacak";
            case TAMAMLANDI -> "Talep kapatılacak";
            case KAPATILDI -> "İşlem tamamlandı";
        };
    }

    /**
     * Talebe özel bekleyen işlem; depo onayı sonrası mesaj güncellenir.
     */
    public String getPendingAction(OrtTalep talep) {
        if (talep == null) {
            return "-";
        }
        if (talep.getDurum() == EnumOrtTalepDurumu.DEPO_ONAYI_BEKLIYOR) {
            return isWarehouseApprovalCompleted(talep)
                    ? "Malzeme talebi onaylandı. Prova randevusu oluşturulacak."
                    : getPendingAction(talep.getDurum());
        }
        return getPendingAction(talep.getDurum());
    }

    /**
     * Talep durum geçmişi.
     */
    public List<OrtTalepDurumGecmisi> getStatusHistory(OrtTalep talep) {
        if (talep == null || talep.getId() == null) {
            return List.of();
        }
        return em.createQuery(
                        "SELECT g FROM OrtTalepDurumGecmisi g WHERE g.aktif=true AND g.ortTalep.id = :talepId " +
                                "ORDER BY g.islemTarihi DESC, g.id DESC",
                        OrtTalepDurumGecmisi.class)
                .setParameter("talepId", talep.getId())
                .getResultList();
    }

    private void checkPermission(SyKullanici kullanici, EnumOrtTalepIslem action) {
        if (!syYetkiService.hasPermission(kullanici, action.getPermission())) {
            throw new IllegalArgumentException("Bu işlem için yetkiniz bulunmuyor: " + action.getDisplayValue());
        }
    }

    private void checkTransition(OrtTalep talep, EnumOrtTalepIslem action) {
        if (!action.getSourceStatuses().contains(talep.getDurum())) {
            throw new IllegalArgumentException(
                    "Talep mevcut durumda (" + talep.getDurum().getDisplayValue() +
                            ") bu işlem uygulanamaz: " + action.getDisplayValue());
        }
        if (talep.getDurum() == EnumOrtTalepDurumu.KAPATILDI) {
            throw new IllegalArgumentException("Kapatılmış talep üzerinde işlem yapılamaz.");
        }
        // Depo onayı bekleniyorsa hiçbir ileri işleme izin verilmez.
        if (isWarehouseLocked(talep, action)) {
            throw new IllegalArgumentException("Depo onayı tamamlanmadan bu adıma geçilemez. " +
                    "Malzeme taleplerinin onaylanması bekleniyor.");
        }
    }

    /**
     * İş kuralları (guard). Belge/ödeme/malzeme/teslimat entity'leri sonraki dilimlerde
     * eklendiğinde ilgili kontroller buraya eklenir.
     */
    private void checkGuards(OrtTalep talep, EnumOrtTalepIslem action, String description) {
        switch (action) {
            case ON_MUAYENE_RANDEVUSU -> {
                if (!ortRandevuService.hasAppointment(talep, EnumOrtRandevuTuru.ON_MUAYENE)) {
                    throw new IllegalArgumentException("Ön muayene randevusu oluşturulmadan bu adıma geçilemez.");
                }
            }
            case OLCU_RANDEVUSU -> {
                if (!ortRandevuService.hasAppointment(talep, EnumOrtRandevuTuru.OLCU)) {
                    throw new IllegalArgumentException("Ölçü randevusu oluşturulmadan bu adıma geçilemez.");
                }
            }
            case PROVA_RANDEVUSU, YENIDEN_PROVA_RANDEVUSU -> {
                EnumOrtRandevuTuru tur = action == EnumOrtTalepIslem.PROVA_RANDEVUSU
                        ? EnumOrtRandevuTuru.PROVA : EnumOrtRandevuTuru.YENIDEN_PROVA;
                if (!ortRandevuService.hasAppointment(talep, tur)) {
                    throw new IllegalArgumentException("Prova randevusu oluşturulmadan bu adıma geçilemez.");
                }
                List<OrtMalzemeTalep> talepler = ortMalzemeTalepService.findByTalep(talep);
                if (!talepler.isEmpty()
                        && talepler.stream().anyMatch(m -> m.getDurum() != EnumOrtMalzemeDurumu.ONAYLANDI)) {
                    throw new IllegalArgumentException("Malzeme talepleri onaylanmadan provaya geçilemez.");
                }
            }
            case ON_MUAYENEYE_GEL -> {
                if (!ortRandevuService.existsAttended(talep, EnumOrtRandevuTuru.ON_MUAYENE)) {
                    throw new IllegalArgumentException("Ön muayene randevusu tamamlanmadan bu işlem yapılamaz.");
                }
            }
            case OLCU_AL -> {
                if (!ortRandevuService.existsAttended(talep, EnumOrtRandevuTuru.OLCU)) {
                    throw new IllegalArgumentException("Ölçü randevusu tamamlanmadan ölçü alınamaz.");
                }
            }
            case PROVA_YAP -> {
                if (!ortRandevuService.existsAttended(talep, EnumOrtRandevuTuru.PROVA)
                        && !ortRandevuService.existsAttended(talep, EnumOrtRandevuTuru.YENIDEN_PROVA)) {
                    throw new IllegalArgumentException("Prova randevusu tamamlanmadan prova yapılamaz.");
                }
            }
            case PERSONEL_ATA -> {
                if (talep.getTeknikerOrtPersonel() == null || talep.getFizyoterapistOrtPersonel() == null) {
                    throw new IllegalArgumentException("Personel ataması yapılmadan bu adıma geçilemez. Tekniker ve fizyoterapist seçilmelidir.");
                }
            }
            case DEGERLENDIRMEYE_AL -> {
                if (talep.getTeknikerOrtPersonel() == null || talep.getFizyoterapistOrtPersonel() == null) {
                    throw new IllegalArgumentException("Değerlendirmeye almadan önce tekniker ve fizyoterapist atanmalıdır.");
                }
            }
            case TALEP_REDET -> {
                if (description == null || description.isBlank()) {
                    throw new IllegalArgumentException("Talep reddedilirken gerekçe (açıklama) zorunludur.");
                }
            }
            case UCRET_BELIRLE -> {
                if (talep.getTalepTipi() == EnumOrtTalepTipi.PROJE_KAPSAMINDA) {
                    throw new IllegalArgumentException("Proje kapsamındaki taleplerde ücret belirlenemez.");
                }
                // Ön görüşme/ücretsiz talep, ücret belirlendiğinde ücretliye geçer.
                if (talep.getTalepTipi() != EnumOrtTalepTipi.UCRETLI) {
                    talep.setTalepTipi(EnumOrtTalepTipi.UCRETLI);
                }
            }
            case PROJE_BELIRLE -> {
                if (talep.getOrtProje() == null) {
                    throw new IllegalArgumentException("Proje seçilmeden bu işlem yapılamaz. Lütfen proje seçiniz.");
                }
                // Ücretli talep projeye dahil olabilir veya olmayabilir. Proje
                // bilgisi eklenir; talep tipi ve ücret akışı değiştirilmez.
                if (talep.getTalepTipi() != EnumOrtTalepTipi.UCRETLI
                        && talep.getTalepTipi() != EnumOrtTalepTipi.PROJE_KAPSAMINDA) {
                    talep.setTalepTipi(EnumOrtTalepTipi.PROJE_KAPSAMINDA);
                }
            }
            case SOZLESME_IMZALA -> {
                if (!ortTalepBelgeService.existsByType(talep, EnumOrtBelgeTuru.SOZLESME)) {
                    throw new IllegalArgumentException("Sözleşme imzalanmadan önce sözleşme belgesi yüklenmelidir.");
                }
            }
            case ODEME_AL -> {
                if (!ortTalepBelgeService.existsByType(talep, EnumOrtBelgeTuru.SOZLESME)) {
                    throw new IllegalArgumentException("Ödeme alınmadan önce hasta sözleşmesi imzalanıp sisteme yüklenmelidir.");
                }
            }
            case ODEME_TAMAMLA -> {
                if (!ortTalepOdemeService.existsByTalep(talep)) {

                    throw new IllegalArgumentException("Ödeme kaydı oluşturulmadan bu işlem yapılamaz.");
                }
                if (!ortTalepBelgeService.existsByType(talep, EnumOrtBelgeTuru.DEKONT)) {
                    throw new IllegalArgumentException("Ödeme dekontu sisteme yüklenmelidir.");
                }
            }
            case FORMLARI_TAMAMLA -> {

                if (!ortTalepBelgeService.existsByType(talep, EnumOrtBelgeTuru.OLCU_FORMU)) {
                    throw new IllegalArgumentException("Ölçü formu taranıp sisteme yüklenmelidir.");
                }
                if (talep.getOrtSutKodu() == null) {
                    throw new IllegalArgumentException("Talep için SUT kodu girilmelidir.");
                }
            }
            case RAPOR_AL -> {
                if (!ortTalepBelgeService.existsByType(talep, EnumOrtBelgeTuru.RAPOR)) {
                    throw new IllegalArgumentException("Doktor raporu taranıp sisteme yüklenmelidir.");
                }
            }
            case RECETE_AL -> {
                if (!ortTalepBelgeService.existsByType(talep, EnumOrtBelgeTuru.RECETE)) {
                    throw new IllegalArgumentException("Reçete taranıp sisteme yüklenmelidir.");
                }
            }
            case MALZEME_TALEP_ET -> {
                List<OrtMalzemeTalep> talepler = ortMalzemeTalepService.findByTalep(talep);
                if (talepler.isEmpty()) {
                    throw new IllegalArgumentException("Malzeme talebi oluşturulmadan bu adıma geçilemez. Lütfen en az bir malzeme kalemi ekleyip kaydediniz.");
                }
            }
            case DEPO_ONAYINA_GONDER -> {
                List<OrtMalzemeTalep> talepler = ortMalzemeTalepService.findByTalep(talep);
                if (talepler.isEmpty()) {
                    throw new IllegalArgumentException("Depo onayına göndermeden önce malzeme talebi oluşturulmalıdır.");
                }
                if (talepler.stream().noneMatch(m -> m.getDurum() == EnumOrtMalzemeDurumu.BEKLIYOR)) {
                    throw new IllegalArgumentException("Onaya gönderilecek bekleyen malzeme talebi bulunamadı.");
                }
            }
            case TESLIM_ET -> {
                if (!ortTalepBelgeService.existsByType(talep, EnumOrtBelgeTuru.TESLIM_TUTANAGI)) {
                    throw new IllegalArgumentException("Teslimat öncesinde imzalı teslim tutanağı yüklenmelidir.");
                }
            }
            case TAMAMLA -> {
                if (!ortTeslimatService.existsByTalep(talep)) {
                    throw new IllegalArgumentException("Teslimat kaydı oluşturulmadan talep tamamlanamaz.");
                }
            }
            default -> {
                // Ek guard gerektirmeyen geçişler
            }
        }
    }
}
