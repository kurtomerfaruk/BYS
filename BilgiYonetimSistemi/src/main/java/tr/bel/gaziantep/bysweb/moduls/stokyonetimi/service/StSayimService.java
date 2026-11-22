package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStIslemLogTuru;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStSayimDurum;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 10:41
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StSayimService extends AbstractService<StSayim> {

    @Serial
    private static final long serialVersionUID = 573078596337683140L;

    @Inject
    private StStokHareketService hareketService;
    @Inject
    private StStokBakiyeService bakiyeService;
    @Inject
    private StIslemLogService stIslemLogService;

    public StSayimService() {
        super(StSayim.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StSayim sayimOlustur(StSayim sayim) {
        if (sayim == null || sayim.getStDepo() == null) {
            throw new IllegalArgumentException("Sayım için depo seçiniz...");
        }
        if (sayim.getBaslangicTarihi() == null) {
            sayim.setBaslangicTarihi(LocalDateTime.now());
        }
        sayim.setDurum(EnumStSayimDurum.TASLAK);
        resolveRefs(sayim);
        create(sayim);
        flush();
        sayim.setSayimNo("SY-" + String.format("%08d", sayim.getId()));
        // snapshot
        snapshotKalemler(sayim);
        return sayim;
    }

    private void snapshotKalemler(StSayim sayim) {
        StDepo depo = sayim.getStDepo();
        List<StStokBakiye> bakiyeler = em.createQuery(
                        "SELECT b FROM StStokBakiye b WHERE b.stDepo = :depo AND b.aktif = true",
                        StStokBakiye.class)
                .setParameter("depo", depo)
                .getResultList();

        // stok bazinda topla
        Map<Integer, BigDecimal> sistemMap = new HashMap<>();
        Map<Integer, StStok> stokMap = new HashMap<>();
        for (StStokBakiye b : bakiyeler) {
            Integer stokId = b.getStStok().getId();
            sistemMap.merge(stokId, b.getMiktar(), BigDecimal::add);
            stokMap.putIfAbsent(stokId, b.getStStok());
        }

        if (sayim.getKalemler() == null) {
            sayim.setKalemler(new ArrayList<>());
        }
        for (Integer stokId : sistemMap.keySet()) {
            StSayimKalem kalem = new StSayimKalem();
            kalem.setStSayim(sayim);
            kalem.setStStok(stokMap.get(stokId));
            kalem.setSistemMiktari(sistemMap.get(stokId));
            kalem.setSayilanMiktar(null);
            kalem.setSayilmadi(false);
            sayim.getKalemler().add(kalem);
        }
    }

    /**
     * Tek bir sayım kaleminin sayılan değerini anında kaydeder ve farkı hesaplar.
     */
    public StSayimKalem saveCountValue(StSayimKalem item) {
        if (item == null || item.getId() == null) {
            throw new IllegalArgumentException("Sayım kalemi bulunamadı.");
        }
        StSayimKalem managed = em.find(StSayimKalem.class, item.getId());
        if (managed == null) {
            throw new IllegalArgumentException("Sayım kalemi bulunamadı.");
        }
        StSayim sayim = em.find(StSayim.class, managed.getStSayim().getId());
        if (sayim == null || sayim.getDurum() != EnumStSayimDurum.SAYIMDA) {
            throw new IllegalArgumentException("Bu sayımda değer girişi yapılamaz.");
        }
        managed.setSayilanMiktar(item.getSayilanMiktar());
        managed.setSayilmadi(false);
        managed.setFark(item.getSayilanMiktar() == null ? null
                : item.getSayilanMiktar().subtract(managed.getSistemMiktari() == null ? BigDecimal.ZERO : managed.getSistemMiktari()));
        em.flush();
        return managed;
    }

    /**
     * Kalemi "sayılmadı" olarak işaretler; fark üretilmez, onayda hareket oluşmaz.
     */
    public StSayimKalem markNotCounted(StSayimKalem item) {
        if (item == null || item.getId() == null) {
            throw new IllegalArgumentException("Sayım kalemi bulunamadı.");
        }
        StSayimKalem managed = em.find(StSayimKalem.class, item.getId());
        if (managed == null) {
            throw new IllegalArgumentException("Sayım kalemi bulunamadı.");
        }
        managed.setSayilmadi(true);
        managed.setSayilanMiktar(null);
        managed.setFark(null);
        em.flush();
        return managed;
    }

    /**
     * Sayaç modunda barkod karşılığı olmayan stokları sayıma ekler.
     */
    public StSayimKalem addOrGetCountItem(StSayim sayim, StStok stok) {
        if (sayim == null || sayim.getId() == null || stok == null || stok.getId() == null) {
            throw new IllegalArgumentException("Sayım veya stok seçilmedi.");
        }
        StSayim managedSayim = em.find(StSayim.class, sayim.getId());
        if (managedSayim == null || managedSayim.getDurum() != EnumStSayimDurum.SAYIMDA) {
            throw new IllegalArgumentException("Bu sayıma kalem eklenemez.");
        }
        StStok managedStok = em.find(StStok.class, stok.getId());
        for (StSayimKalem mevcut : managedSayim.getKalemler()) {
            if (mevcut.getStStok() != null && mevcut.getStStok().getId().equals(managedStok.getId())) {
                return mevcut;
            }
        }
        StSayimKalem kalem = new StSayimKalem();
        kalem.setStSayim(managedSayim);
        kalem.setStStok(managedStok);
        kalem.setSistemMiktari(BigDecimal.ZERO);
        kalem.setSayilanMiktar(BigDecimal.ZERO);
        kalem.setSayilmadi(false);
        managedSayim.getKalemler().add(kalem);
        em.flush();
        return kalem;
    }

    /**
     * Geri alma islemi için önceki değeri geri yükler.
     */
    public StSayimKalem restoreCountValue(StSayimKalem item, BigDecimal quantity, boolean notCounted) {
        if (item == null || item.getId() == null) {
            throw new IllegalArgumentException("Sayım kalemi bulunamadı.");
        }
        StSayimKalem managed = em.find(StSayimKalem.class, item.getId());
        if (managed == null) {
            throw new IllegalArgumentException("Sayım kalemi bulunamadı.");
        }
        managed.setSayilmadi(notCounted);
        managed.setSayilanMiktar(notCounted ? null : quantity);
        BigDecimal sistem = managed.getSistemMiktari() == null ? BigDecimal.ZERO : managed.getSistemMiktari();
        managed.setFark(notCounted || quantity == null ? null : quantity.subtract(sistem));
        em.flush();
        return managed;
    }

    /**
     * Sayaç modunda yanlış eklenen kalemi geri alır.
     */
    public void removeCountItem(StSayimKalem item) {
        if (item == null || item.getId() == null) {
            return;
        }
        StSayimKalem managed = em.find(StSayimKalem.class, item.getId());
        if (managed == null) {
            return;
        }
        StSayim sayim = managed.getStSayim();
        if (sayim != null) {
            sayim.getKalemler().remove(managed);
        }
        em.remove(managed);
        em.flush();
    }

    public void finish(StSayim sayim, SyKullanici sayan) {
        if (sayim == null || sayim.getId() == null) {
            throw new IllegalArgumentException("Bu sayım başlatılamaz");
        }
        StSayim managed = em.find(StSayim.class, sayim.getId());
        if (managed.getDurum() != EnumStSayimDurum.SAYIMDA) {
            throw new IllegalArgumentException("Bu sayım tamamlanamaz");
        }
        // fark hesapla: sayılmayan kalemler 0 kabul edilmez, ayrı durumda bırakılır
        for (StSayimKalem k : managed.getKalemler()) {
            if (k.isSayilmadi() || k.getSayilanMiktar() == null) {

                k.setSayilmadi(true);
                k.setSayilanMiktar(null);
                k.setFark(null);
                continue;
            }
            BigDecimal sistem = k.getSistemMiktari() == null ? BigDecimal.ZERO : k.getSistemMiktari();
            k.setFark(k.getSayilanMiktar().subtract(sistem));
        }
        managed.setDurum(EnumStSayimDurum.ONAY_BEKLIYOR);
        if (managed.getSayanSyKullanici() == null) {
            managed.setSayanSyKullanici(sayan);
        }
    }

    public void confirm(StSayim sayim, SyKullanici onaylayan) {
        if (sayim == null || sayim.getId() == null) {
            throw new IllegalArgumentException("Bu sayım başlatılamaz");
        }
        StSayim managed = em.find(StSayim.class, sayim.getId());
        if (managed.getDurum() != EnumStSayimDurum.ONAY_BEKLIYOR) {
            throw new IllegalArgumentException("Bu sayım onaylanamaz");
        }
        // fark hareketleri
        for (StSayimKalem k : managed.getKalemler()) {
            if (k.isSayilmadi()) {
                continue;
            }
            BigDecimal fark = k.getFark();
            if (fark == null || fark.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            StStokHareket hareket = new StStokHareket();
            hareket.setStStok(k.getStStok());
            hareket.setStDepo(managed.getStDepo());
            hareket.setHareketTuru(EnumStHareketTuru.SAYIM_FARKI);
            hareket.setYon(fark.compareTo(BigDecimal.ZERO) > 0 ? EnumGirisCikis.GIRIS : EnumGirisCikis.CIKIS);
            hareket.setMiktar(fark.abs());
            hareket.setIslemTarihi(LocalDateTime.now());
            hareket.setAciklama("Sayım farkı: " + managed.getSayimNo());
            hareketService.hareketEkle(hareket);
        }
        managed.setDurum(EnumStSayimDurum.TAMAMLANDI);
        managed.setBitisTarihi(LocalDateTime.now());
        managed.setOnaylayanSyKullanici(onaylayan);
        stIslemLogService.log(EnumStIslemLogTuru.SAYIM, onaylayan, managed.getStDepo(),
                "StSayim", managed.getSayimNo(), "Sayım onaylandı");
    }

    private void resolveRefs(StSayim sayim) {
        if (sayim.getStDepo() != null && sayim.getStDepo().getId() != null) {
            sayim.setStDepo(em.find(StDepo.class, sayim.getStDepo().getId()));
        }
    }
}
