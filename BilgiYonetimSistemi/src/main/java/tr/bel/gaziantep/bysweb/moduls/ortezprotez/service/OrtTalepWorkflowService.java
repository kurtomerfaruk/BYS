package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepIslem;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepTipi;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalepDurumGecmisi;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyYetkiService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Ortez-Protez talebi durum geçişlerini merkezi olarak yönetir.
 *
 * <p>Geçiş kuralları {@link EnumOrtTalepIslem} üzerinde tanımlıdır. Bu servis:
 * <ul>
 *     <li>kullanıcının ilgili işlem yetkisini servis katmanında kontrol eder,</li>
 *     <li>mevcut durumdan hedef duruma geçişin kurallara uygunluğunu doğrular,</li>
 *     <li>iş kuralları (guard) koşullarını uygular,</li>
 *     <li>optimistic lock ile eşzamanlı güncellemeleri engeller,</li>
 *     <li>durum değişikliği ve işlem geçmişini tek transaction'da kaydeder.</li>
 * </ul>
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
    private OrtTalepDurumGecmisiService durumGecmisiService;
    @Inject
    private SyYetkiService syYetkiService;

    /**
     * Belirtilen işlemi uygular; talebin durumunu değiştirir ve geçmiş kaydı oluşturur.
     * Hatalarda anlamlı mesajlı IllegalArgumentException fırlatır; transaction geri alınır.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void gecisYap(Integer talepId, EnumOrtTalepIslem islem, String aciklama) {
        if (talepId == null) {
            throw new IllegalArgumentException("İşlem yapılacak talep bulunamadı.");
        }
        if (islem == null) {
            throw new IllegalArgumentException("Geçersiz talep işlemi.");
        }

        OrtTalep talep = em.find(OrtTalep.class, talepId);
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }

        SyKullanici kullanici = Util.getSyKullanici();
        yetkiKontrol(kullanici, islem);
        gecisKontrol(talep, islem);
        guardKontrol(talep, islem, aciklama);

        // Eşzamanlı güncellemeleri optimistic lock ile engelle
        em.lock(talep, LockModeType.OPTIMISTIC);

        EnumOrtTalepDurumu eskiDurum = talep.getDurum();
        talep.setDurum(islem.getHedefDurum());
        em.merge(talep);
        em.flush();

        OrtTalepDurumGecmisi gecmis = OrtTalepDurumGecmisi.builder()
                .ortTalep(talep)
                .eskiDurum(eskiDurum)
                .yeniDurum(islem.getHedefDurum())
                .islemTarihi(LocalDateTime.now())
                .islemiYapanSyKullanici(kullanici)
                .aciklama(aciklama == null || aciklama.isBlank() ? islem.getDisplayValue() : aciklama)
                .build();
        durumGecmisiService.create(gecmis);
        em.flush();
    }

    /**
     * UI'da işlem butonlarını göstermek için: kullanıcının yetkisi ve mevcut duruma
     * uygunluk kontrolü yapar. Guard koşulları burada uygulanmaz (servis tekrar kontrol eder).
     */
    public boolean islemKullanilabilir(OrtTalep talep, EnumOrtTalepIslem islem) {
        if (talep == null || islem == null) {
            return false;
        }
        if (talep.getDurum() != islem.getKaynakDurum()) {
            return false;
        }
        return syYetkiService.hasYetki(Util.getSyKullanici(), islem.getYetki());
    }

    /**
     * Mevcut durumda kullanılabilir işlemler (yetki filtreli).
     */
    public Set<EnumOrtTalepIslem> kullanilabilirIslemler(OrtTalep talep) {
        if (talep == null) {
            return Set.of();
        }
        return EnumOrtTalepIslem.getIslemler(talep.getDurum()).stream()
                .filter(islem -> syYetkiService.hasYetki(Util.getSyKullanici(), islem.getYetki()))
                .collect(java.util.stream.Collectors.toCollection(() -> EnumSet.noneOf(EnumOrtTalepIslem.class)));
    }

    /**
     * Durum için bekleyen işlem açıklaması (liste ve detay ekranlarında gösterilir).
     */
    public String bekleyenIslem(EnumOrtTalepDurumu durum) {
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
    public List<OrtTalepDurumGecmisi> durumGecmisi(OrtTalep talep) {
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

    private void yetkiKontrol(SyKullanici kullanici, EnumOrtTalepIslem islem) {
        if (!syYetkiService.hasYetki(kullanici, islem.getYetki())) {
            throw new IllegalArgumentException("Bu işlem için yetkiniz bulunmuyor: " + islem.getDisplayValue());
        }
    }

    private void gecisKontrol(OrtTalep talep, EnumOrtTalepIslem islem) {
        if (talep.getDurum() != islem.getKaynakDurum()) {
            throw new IllegalArgumentException(
                    "Talep mevcut durumda (" + talep.getDurum().getDisplayValue() +
                            ") bu işlem uygulanamaz: " + islem.getDisplayValue());
        }
        if (talep.getDurum() == EnumOrtTalepDurumu.KAPATILDI) {
            throw new IllegalArgumentException("Kapatılmış talep üzerinde işlem yapılamaz.");
        }
    }

    /**
     * İş kuralları (guard). Bu dilimde mevcut alanlarla kontrol edilebilen kurallar
     * uygulanır. Belge/ödeme/malzeme/teslimat entity'leri sonraki dilimlerde eklendiğinde
     * ilgili kontroller buraya eklenir.
     */
    private void guardKontrol(OrtTalep talep, EnumOrtTalepIslem islem, String aciklama) {
        switch (islem) {
            case DEGERLENDIRMEYE_AL -> {
                if (talep.getTeknikerOrtPersonel() == null || talep.getFizyoterapistOrtPersonel() == null) {
                    throw new IllegalArgumentException("Değerlendirmeye almadan önce tekniker ve fizyoterapist atanmalıdır.");
                }
            }
            case TALEP_REDET -> {
                if (aciklama == null || aciklama.isBlank()) {
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
