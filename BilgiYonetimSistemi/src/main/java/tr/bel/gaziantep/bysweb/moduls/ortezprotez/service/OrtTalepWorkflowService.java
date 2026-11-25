package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.*;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
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
        talep.setDurum(action.getTargetStatus());
        em.merge(talep);
        em.flush();

        OrtTalepDurumGecmisi gecmis = OrtTalepDurumGecmisi.builder()
                .ortTalep(talep)
                .eskiDurum(eskiDurum)
                .yeniDurum(action.getTargetStatus())
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createAppointmentAndTransition(Integer talepId, EnumOrtTalepIslem action, String description) {
        if (talepId == null || action == null) {
            return;
        }
        EnumOrtRandevuTuru randevuTuru = appointmentTypeOf(action);
        if (randevuTuru == null) {
            applyTransition(talepId, action, description);
            return;
        }

        OrtTalep talep = em.find(OrtTalep.class, talepId);
        if (talep != null && !ortRandevuService.existsPlanned(talep, randevuTuru)) {
            OrtRandevu randevu = OrtRandevu.builder()
                    .ortTalep(talep)
                    .ortKisi(talep.getOrtKisi())
                    .randevuTuru(randevuTuru)
                    .randevuTarihi(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0))
                    .durum(tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtRandevuDurumu.PLANLANDI)
                    .build();
            em.persist(randevu);
            em.flush();
        }
        applyTransition(talepId, action, description);
    }

    private EnumOrtRandevuTuru appointmentTypeOf(EnumOrtTalepIslem action) {
        return switch (action) {
            case ON_MUAYENE_RANDEVUSU -> EnumOrtRandevuTuru.ON_MUAYENE;
            case OLCU_RANDEVUSU -> EnumOrtRandevuTuru.OLCU;
            case PROVA_RANDEVUSU -> EnumOrtRandevuTuru.PROVA;
            case YENIDEN_PROVA_RANDEVUSU -> EnumOrtRandevuTuru.YENIDEN_PROVA;
            default -> null;
        };
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
        return syYetkiService.hasPermission(Util.getSyKullanici(), action.getPermission());
    }

    /**
     * Mevcut durumda kullanılabilir işlemler (yetki filtreli).
     */
    public Set<EnumOrtTalepIslem> getAvailableActions(OrtTalep talep) {
        if (talep == null) {
            return Set.of();
        }
        return EnumOrtTalepIslem.getActions(talep.getDurum()).stream()
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
            case DEPO_ONAYI_BEKLIYOR -> "Malzemeler onaylanacak/reddedilecek";
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
    }

    /**
     * İş kuralları (guard). Belge/ödeme/malzeme/teslimat entity'leri sonraki dilimlerde
     * eklendiğinde ilgili kontroller buraya eklenir.
     */
    private void checkGuards(OrtTalep talep, EnumOrtTalepIslem action, String description) {
        switch (action) {
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
                if (talep.getTalepTipi() != EnumOrtTalepTipi.UCRETLI) {
                    throw new IllegalArgumentException("Ücret belirleme işlemi yalnızca ücretli taleplerde yapılabilir.");
                }
            }
            case PROJE_BELIRLE -> {
                if (talep.getTalepTipi() != EnumOrtTalepTipi.PROJE_KAPSAMINDA) {
                    throw new IllegalArgumentException("Proje seçimi yalnızca proje kapsamındaki taleplerde yapılabilir.");
                }
                if (talep.getOrtProje() == null) {
                    throw new IllegalArgumentException("Proje kapsamındaki taleplerde proje seçilmelidir.");
                }
            }
            case ODEME_TAMAMLA -> {
                // TODO: OrtTalepOdeme entity'si eklendiğinde ödeme ve dekont belgesi kontrolü eklenecek.
            }
            case FORMLARI_TAMAMLA, RAPOR_AL, RECETE_AL, MALZEME_TALEP_ET, DEPO_ONAYINA_GONDER -> {
                // TODO: OrtTalepBelge / OrtMalzemeTalep entity'leri eklendiğinde ilgili belge ve onay kontrolleri eklenecek.
            }
            case TESLIM_ET, TAMAMLA -> {
                // TODO: OrtTeslimat entity'si eklendiğinde imzalı teslim tutanağı kontrolü eklenecek.
            }
            default -> {
                // Ek guard gerektirmeyen geçişler
            }
        }
    }
}
