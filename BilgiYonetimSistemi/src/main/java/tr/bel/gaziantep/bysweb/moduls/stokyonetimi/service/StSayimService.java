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
            sayim.getKalemler().add(kalem);
        }
    }

    public void baslat(StSayim sayim, SyKullanici sayan) {
        if (sayim == null || sayim.getId() == null) {
            throw new IllegalArgumentException("İşlem için bir sayım seçin.");
        }
        StSayim managed = em.find(StSayim.class, sayim.getId());
        if (managed.getDurum() != EnumStSayimDurum.TASLAK) {
            throw new IllegalArgumentException("Bu sayım başlatılamaz");
        }
        managed.setDurum(EnumStSayimDurum.SAYIMDA);
        if (managed.getBaslangicTarihi() == null) {
            managed.setBaslangicTarihi(LocalDateTime.now());
        }
        managed.setSayanSyKullanici(sayan);
    }

    public void tamamlaSayim(StSayim sayim, SyKullanici sayan) {
        if (sayim == null || sayim.getId() == null) {
            throw new IllegalArgumentException("Bu sayım başlatılamaz");
        }
        StSayim managed = em.find(StSayim.class, sayim.getId());
        if (managed.getDurum() != EnumStSayimDurum.SAYIMDA) {
            throw new IllegalArgumentException("Bu sayım tamamlanamaz");
        }
        // fark hesapla
        for (StSayimKalem k : managed.getKalemler()) {
            BigDecimal sayilan = k.getSayilanMiktar() == null ? BigDecimal.ZERO : k.getSayilanMiktar();
            k.setFark(sayilan.subtract(k.getSistemMiktari()));
        }
        managed.setDurum(EnumStSayimDurum.ONAY_BEKLIYOR);
        if (managed.getSayanSyKullanici() == null) {
            managed.setSayanSyKullanici(sayan);
        }
    }

    public void onayla(StSayim sayim, SyKullanici onaylayan) {
        if (sayim == null || sayim.getId() == null) {
            throw new IllegalArgumentException("Bu sayım başlatılamaz");
        }
        StSayim managed = em.find(StSayim.class, sayim.getId());
        if (managed.getDurum() != EnumStSayimDurum.ONAY_BEKLIYOR) {
            throw new IllegalArgumentException("Bu sayım onaylanamaz");
        }
        // fark hareketleri
        for (StSayimKalem k : managed.getKalemler()) {
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

    public void iptalEt(StSayim sayim) {
        if (sayim == null || sayim.getId() == null) {
            throw new IllegalArgumentException("Bu sayım başlatılamaz");
        }
        StSayim managed = em.find(StSayim.class, sayim.getId());
        if (managed.getDurum() == EnumStSayimDurum.TAMAMLANDI) {
            throw new IllegalArgumentException("Bu sayım iptal edilemez");
        }
        managed.setDurum(EnumStSayimDurum.IPTAL);
    }

    private void resolveRefs(StSayim sayim) {
        if (sayim.getStDepo() != null && sayim.getStDepo().getId() != null) {
            sayim.setStDepo(em.find(StDepo.class, sayim.getStDepo().getId()));
        }
    }
}
