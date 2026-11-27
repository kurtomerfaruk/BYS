package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepDurumu;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalepDurumGecmisi;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 24.11.2026 15:57
 */
@Stateless
public class OrtTalepService extends AbstractService<OrtTalep> {

    @Serial
    private static final long serialVersionUID = 7662405663655858032L;

    @Inject
    private OrtTalepDurumGecmisiService durumGecmisiService;

    public OrtTalepService() {
        super(OrtTalep.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void createTalep(OrtTalep talep) {
        if (talep.getTalepTarihi() == null) {
            talep.setTalepTarihi(LocalDateTime.now());
        }
        if (talep.getDurum() == null) {
            talep.setDurum(EnumOrtTalepDurumu.YENI_TALEP);
        }

        create(talep);
        flush();
        talep.setTalepNo(generateTalepNo(talep.getId()));
        edit(talep);

        addDurumGecmisi(talep, null, talep.getDurum(), "Talep oluşturuldu.");
        flush();
    }

    /**
     * Talep genel bilgilerini günceller. Durum değişikliği bu metotla yapılamaz;
     * tüm geçişler OrtTalepWorkflowService üzerinden doğrulanarak uygulanır.
     *
     * Ücret/proje kararı sonrası genel güncelleme kilitlidir. Talep tipi, talep tarihi,
     * kişi ve workflow tarafından yönetilen alanlar bu metotla değiştirilemez;
     * yalnızca açıklama güncellenir.
     */
    public void updateTalep(OrtTalep talep) {
        if (talep == null || talep.getId() == null) {
            throw new IllegalArgumentException("Güncellenecek talep bulunamadı.");
        }
        OrtTalep mevcut = em.find(OrtTalep.class, talep.getId());
        if (mevcut == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }
        if (mevcut.getDurum() != null && mevcut.getDurum().isUpdateLocked()) {
            throw new IllegalArgumentException("Bu aşamada talep bilgileri güncellenemez. " +
                    "Değişiklikler workflow işlemleri üzerinden yapılmalıdır.");
        }
        // Talep tipi (Ücretli / Proje Kapsamında) değerlendirme öncesi kararıdır;
        // kaydedilmezse workflow yanlış dalı (Proje Belirle) seçer.
        if (talep.getTalepTipi() != null) {
            mevcut.setTalepTipi(talep.getTalepTipi());
        }
        mevcut.setAciklama(talep.getAciklama());
        edit(mevcut);
        flush();
    }

    /**
     * Workflow adımlarında (personel ataması, SUT kodu, proje seçimi) güvenli güncelleme.
     * Talep no, durum, tarih, tip ve kişi veritabanındaki değerlerde sabitlenir.
     */
    public void updateWorkflowFields(OrtTalep talep) {
        if (talep == null || talep.getId() == null) {
            throw new IllegalArgumentException("Güncellenecek talep bulunamadı.");
        }
        OrtTalep mevcut = em.find(OrtTalep.class, talep.getId());
        if (mevcut == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }
        talep.setTalepNo(mevcut.getTalepNo());
        talep.setDurum(mevcut.getDurum());
        talep.setTalepTarihi(mevcut.getTalepTarihi());
        talep.setTalepTipi(mevcut.getTalepTipi());
        talep.setOrtKisi(mevcut.getOrtKisi());
        edit(talep);
        flush();
    }

    public List<OrtTalepDurumGecmisi> findDurumGecmisiByTalep(OrtTalep talep) {
        if (talep == null || talep.getId() == null) {
            return List.of();
        }
        return getEntityManager()
                .createQuery("SELECT g FROM OrtTalepDurumGecmisi g WHERE g.aktif=true AND g.ortTalep = :talep ORDER BY g.islemTarihi DESC, g.id DESC",
                        OrtTalepDurumGecmisi.class)
                .setParameter("talep", talep)
                .getResultList();
    }

    private void addDurumGecmisi(OrtTalep talep, EnumOrtTalepDurumu eskiDurum, EnumOrtTalepDurumu yeniDurum, String aciklama) {
        SyKullanici kullanici = Util.getSyKullanici();
        OrtTalepDurumGecmisi gecmis = OrtTalepDurumGecmisi.builder()
                .ortTalep(talep)
                .eskiDurum(eskiDurum)
                .yeniDurum(yeniDurum)
                .islemTarihi(LocalDateTime.now())
                .islemiYapanSyKullanici(kullanici)
                .aciklama(aciklama)
                .build();
        durumGecmisiService.create(gecmis);
    }

    private String generateTalepNo(Integer id) {
        return "ORT-" + Year.now().getValue() + "-" + String.format("%06d", id == null ? 0 : id);
    }
}
