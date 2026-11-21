package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStMaliyetYontemi;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyGenelAyar;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyGenelAyarService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StMaliyetKatmani;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StMaliyetTuketim;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;

import java.io.Serial;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 19.11.2026 16:00
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StMaliyetKatmaniService extends AbstractService<StMaliyetKatmani> {

    @Serial
    private static final long serialVersionUID = 2450977504119121123L;

    @Inject
    private SyGenelAyarService syGenelAyarService;

    public StMaliyetKatmaniService() {
        super(StMaliyetKatmani.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EnumStMaliyetYontemi getYontem() {
        try {
            SyGenelAyar ayar = syGenelAyarService.findByTanim("VarsayilanMaliyetYontemi");
            if (ayar != null && ayar.getDeger() != null) {
                String deger = ayar.getDeger().trim().toUpperCase();
                if (deger.contains("LIFO")) {
                    return EnumStMaliyetYontemi.LIFO;
                }
                if (deger.contains("WEIGHT") || deger.contains("AĞIRLIK") || deger.contains("AGIRLIK")) {
                    return EnumStMaliyetYontemi.WEIGHTED_AVERAGE;
                }
                if (deger.contains("FIFO")) {
                    return EnumStMaliyetYontemi.FIFO;
                }
            }
        } catch (Exception ignored) {
        }
        return EnumStMaliyetYontemi.FIFO;
    }

    /**
     * Giriş hareketi için maliyet katmanı oluşturur.
     */
    public void katmanOlustur(StStokHareket hareket) {
        if (hareket == null || hareket.getYon() != EnumGirisCikis.GIRIS) {
            return;
        }
        StMaliyetKatmani katman = new StMaliyetKatmani();
        katman.setStStok(hareket.getStStok());
        katman.setStDepo(hareket.getStDepo());
        katman.setStStokHareket(hareket);
        katman.setAcilanMiktar(hareket.getMiktar());
        katman.setKalanMiktar(hareket.getMiktar());
        BigDecimal birim = hareket.getBirimMaliyet() == null ? BigDecimal.ZERO : hareket.getBirimMaliyet();
        katman.setBirimMaliyet(birim);
        katman.setTarih(hareket.getIslemTarihi() == null ? LocalDateTime.now() : hareket.getIslemTarihi());
        em.persist(katman);
    }

    /**
     * Hareketin olusturdugu maliyet katmanini bulur.
     */
    public StMaliyetKatmani findByStockMovement(StStokHareket hareket) {
        if (hareket == null || hareket.getId() == null) {
            return null;
        }
        return em.createQuery("SELECT k FROM StMaliyetKatmani k WHERE k.stStokHareket = :hareket AND k.aktif = true",
                        StMaliyetKatmani.class)
                .setParameter("hareket", hareket)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Giriş hareketi miktari/fiyati degistiginde ilgili katmani gunceller.
     */
    public void updateCostLayer(StStokHareket hareket, BigDecimal oldQuantity, BigDecimal oldUnitCost) {
        if (hareket == null || hareket.getYon() != EnumGirisCikis.GIRIS) {
            return;
        }
        StMaliyetKatmani katman = findByStockMovement(hareket);
        if (katman == null) {
            katmanOlustur(hareket);
            return;
        }
        BigDecimal yeniMiktar = hareket.getMiktar() == null ? BigDecimal.ZERO : hareket.getMiktar();
        BigDecimal eskiMiktar = oldQuantity == null ? BigDecimal.ZERO : oldQuantity;
        BigDecimal delta = yeniMiktar.subtract(eskiMiktar);
        katman.setAcilanMiktar(yeniMiktar);
        BigDecimal yeniKalan = katman.getKalanMiktar().add(delta);
        katman.setKalanMiktar(yeniKalan.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : yeniKalan);
        katman.setBirimMaliyet(hareket.getBirimMaliyet() == null ? BigDecimal.ZERO : hareket.getBirimMaliyet());
        katman.setTarih(hareket.getIslemTarihi() == null ? LocalDateTime.now() : hareket.getIslemTarihi());
    }

    /**
     * Hareket iptal edildiginde katmani pasif hale getirir.
     */
    public void deactivateCostLayer(StStokHareket hareket) {
        StMaliyetKatmani katman = findByStockMovement(hareket);
        if (katman != null) {
            katman.setAktif(false);
        }
    }

    /**
     * Çıkış hareketi için katmanları tüketir; hareketin birimMaliyet/toplamMaliyet'ini doldurur.
     */
    public void katmanTuket(StStokHareket hareket) {
        if (hareket == null || hareket.getYon() != EnumGirisCikis.CIKIS) {
            return;
        }
        EnumStMaliyetYontemi yontem = getYontem();
        List<StMaliyetKatmani> acikKatmanlar = em.createQuery(
                        "SELECT k FROM StMaliyetKatmani k WHERE k.stStok = :stok AND k.stDepo = :depo AND k.kalanMiktar > 0 AND k.aktif = true",
                        StMaliyetKatmani.class)
                .setParameter("stok", hareket.getStStok())
                .setParameter("depo", hareket.getStDepo())
                .getResultList();

        if (acikKatmanlar.isEmpty()) {
            hareket.setBirimMaliyet(BigDecimal.ZERO);
            hareket.setToplamMaliyet(BigDecimal.ZERO);
            return;
        }

        // sıralama
        List<StMaliyetKatmani> sirali = new ArrayList<>(acikKatmanlar);
        if (yontem == EnumStMaliyetYontemi.LIFO) {
            sirali.sort(Comparator.comparing(StMaliyetKatmani::getTarih, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        } else {
            sirali.sort(Comparator.comparing(StMaliyetKatmani::getTarih, Comparator.nullsLast(Comparator.naturalOrder())));
        }

        BigDecimal birimMaliyetOrtalama = null;
        if (yontem == EnumStMaliyetYontemi.WEIGHTED_AVERAGE) {
            BigDecimal toplamMiktar = BigDecimal.ZERO;
            BigDecimal toplamDeger = BigDecimal.ZERO;
            for (StMaliyetKatmani k : acikKatmanlar) {
                toplamMiktar = toplamMiktar.add(k.getKalanMiktar());
                toplamDeger = toplamDeger.add(k.getKalanMiktar().multiply(
                        k.getBirimMaliyet() == null ? BigDecimal.ZERO : k.getBirimMaliyet()));
            }
            if (toplamMiktar.compareTo(BigDecimal.ZERO) > 0) {
                birimMaliyetOrtalama = toplamDeger.divide(toplamMiktar, 4, RoundingMode.HALF_UP);
            } else {
                birimMaliyetOrtalama = BigDecimal.ZERO;
            }
        }

        BigDecimal kalanMiktar = hareket.getMiktar();
        BigDecimal toplamTuketimMaliyeti = BigDecimal.ZERO;
        BigDecimal toplamTuketilen = BigDecimal.ZERO;

        for (StMaliyetKatmani katman : sirali) {
            if (kalanMiktar.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            BigDecimal alinacak = katman.getKalanMiktar().min(kalanMiktar);
            if (alinacak.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            BigDecimal birim = (yontem == EnumStMaliyetYontemi.WEIGHTED_AVERAGE)
                    ? birimMaliyetOrtalama
                    : (katman.getBirimMaliyet() == null ? BigDecimal.ZERO : katman.getBirimMaliyet());

            StMaliyetTuketim tuketim = new StMaliyetTuketim();
            tuketim.setStMaliyetKatmani(katman);
            tuketim.setStStokHareket(hareket);
            tuketim.setTuketilenMiktar(alinacak);
            tuketim.setBirimMaliyet(birim);
            tuketim.setToplamMaliyet(alinacak.multiply(birim));
            em.persist(tuketim);

            katman.setKalanMiktar(katman.getKalanMiktar().subtract(alinacak));
            kalanMiktar = kalanMiktar.subtract(alinacak);
            toplamTuketimMaliyeti = toplamTuketimMaliyeti.add(alinacak.multiply(birim));
            toplamTuketilen = toplamTuketilen.add(alinacak);
        }

        // kalan miktar (yetersiz katman) -> 0 maliyet
        if (kalanMiktar.compareTo(BigDecimal.ZERO) > 0) {
            // eksik kisim 0 maliyetle maliyetlenir; hareket engellenmez
        }

        BigDecimal birimMaliyetHareket = BigDecimal.ZERO;
        if (toplamTuketilen.compareTo(BigDecimal.ZERO) > 0) {
            birimMaliyetHareket = toplamTuketimMaliyeti.divide(toplamTuketilen, 4, RoundingMode.HALF_UP);
        }
        hareket.setBirimMaliyet(birimMaliyetHareket);
        hareket.setToplamMaliyet(toplamTuketimMaliyeti);
    }
}
