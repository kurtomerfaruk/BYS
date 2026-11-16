package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStMaliyetYontemi;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 11:21
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class StStokIslemService {

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Inject
    private StStokHareketService hareketService;
    @Inject
    private StStokMiktarService miktarService;
    @Inject
    private StMaliyetService maliyetService;

    /**
     * Yeni stok hareketi oluþturur. Mevcut bir hareket bu metoda verilemez.
     */
    public StStokHareket createMovement(StStokHareket hareket) {
        if (hareket == null) {
            throw new IllegalArgumentException("Hareket zorunludur");
        }
        if (hareket.getId() != null) {
            throw new IllegalArgumentException("Mevcut hareket yeniden post edilemez. Düzeltme için hareketi iptal edip yeni kayýt girin");
        }
        if (hareket.getStStok() == null || hareket.getStDepo() == null) {
            throw new IllegalArgumentException("Stok kartý ve depo zorunludur");
        }

        StStok stokKarti = em.find(StStok.class, hareket.getStStok().getId(), LockModeType.PESSIMISTIC_WRITE);
        if (stokKarti == null) throw new IllegalArgumentException("Stok kartý bulunamadý");
        hareket.setStStok(stokKarti);

        StDepo depo = em.find(StDepo.class, hareket.getStDepo().getId(), LockModeType.PESSIMISTIC_WRITE);
        if (depo == null) throw new IllegalArgumentException("Depo bulunamadý");
        hareket.setStDepo(depo);

//        if (hareket.getKullanici() != null && !yetkiService.hasYetki(hareket.getKullanici(), depo, EnumStYetkiTuru.ISLEM)) {
//            throw new SecurityException("Depo iþlem yetkisi bulunamadý");
//        }

        BigDecimal miktarTemel = hareket.getMiktar();
        if (miktarTemel == null || miktarTemel.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Miktar sýfýrdan büyük olmalýdýr");
        }
        if (hareket.getStBirim() == null || !hareket.getStBirim().getId().equals(stokKarti.getStBirim().getId())) {
            throw new IllegalArgumentException("Birim dönüþümü desteklenmiyor (þimdilik temel birim kullanýn)");
        }

        if (hareket.getDurum() == EnumGirisCikis.CIKIS && !maliyetService.isNegativeStockAllowed()) {
            BigDecimal available = miktarService.getAvailable(stokKarti, depo);
            if (available.compareTo(miktarTemel) < 0) {
                throw new IllegalStateException("Yetersiz stok: " + stokKarti.getStokKodu());
            }
        }

        StDepoLokasyon lokasyon = hareket.getStDepoLokasyon();
        EnumStMaliyetYontemi method = maliyetService.getCostMethod(stokKarti);
        StStokBakiye bakiye = miktarService.getOrCreateBakiye(stokKarti, depo, lokasyon, null, LockModeType.PESSIMISTIC_WRITE);

        // Aðýrlýklý ortalama için hareket eklenmeden önceki miktar gerekir.
        BigDecimal oncekiMiktar = null;
        if (hareket.getDurum() == EnumGirisCikis.GIRIS && method == EnumStMaliyetYontemi.WEIGHTED_AVERAGE) {
            oncekiMiktar = miktarService.calculateNet(stokKarti, depo, lokasyon);
            if (oncekiMiktar.compareTo(BigDecimal.ZERO) < 0) oncekiMiktar = BigDecimal.ZERO;
        }

        if (hareket.getIslemTarihi() == null) hareket.setIslemTarihi(LocalDateTime.now());

        hareketService.create(hareket);
        hareketService.flush();

        if (hareket.getDurum() == EnumGirisCikis.GIRIS) {
            BigDecimal birimMaliyet = hareket.getBirimMaliyet() == null ? BigDecimal.ZERO : hareket.getBirimMaliyet();
            maliyetService.createCostLayer(hareket, miktarTemel, birimMaliyet);
            hareket.setToplamMaliyet(birimMaliyet.multiply(miktarTemel));
            if (method == EnumStMaliyetYontemi.WEIGHTED_AVERAGE) {
                BigDecimal oldAvg = bakiye.getOrtalamaMaliyet() == null ? BigDecimal.ZERO : bakiye.getOrtalamaMaliyet();
                BigDecimal toplamMiktar = oncekiMiktar.add(miktarTemel);
                if (toplamMiktar.compareTo(BigDecimal.ZERO) > 0) {
                    bakiye.setOrtalamaMaliyet(oldAvg.multiply(oncekiMiktar)
                            .add(birimMaliyet.multiply(miktarTemel))
                            .divide(toplamMiktar, 4, RoundingMode.HALF_UP));
                } else {
                    bakiye.setOrtalamaMaliyet(birimMaliyet);
                }
            }
        } else {
            BigDecimal totalCost = maliyetService.consumeCostLayers(stokKarti, depo, miktarTemel, method, hareket);
            hareket.setToplamMaliyet(totalCost);
            if (miktarTemel.compareTo(BigDecimal.ZERO) > 0) {
                hareket.setBirimMaliyet(totalCost.divide(miktarTemel, 4, RoundingMode.HALF_UP));
            }
            if (method == EnumStMaliyetYontemi.WEIGHTED_AVERAGE) {
                bakiye.setOrtalamaMaliyet(maliyetService.calculateAverageCost(stokKarti, depo));
            }
        }

        miktarService.recalculateBakiye(stokKarti, depo, lokasyon);
        miktarService.recalculateStokKartiMiktar(stokKarti);
        return hareket;
    }

    /**
     * Hareketin yalnýzca miktarý etkilemeyen alanlarýný günceller.
     *
     * <p>Miktar, durum, depo, lokasyon, birim ve maliyet alanlarý deðiþtirilemez.
     * Bunlar için hareket iptal edilip yeni kayýt girilmelidir.</p>
     */
    public StStokHareket updateMovement(StStokHareket hareket) {
        if (hareket == null || hareket.getId() == null) throw new IllegalArgumentException("Geçersiz hareket");
        StStokHareket managed = hareketService.find(hareket.getId());
        if (managed == null) throw new IllegalArgumentException("Hareket bulunamadý");
        if (managed.isIptalEdildi()) throw new IllegalStateException("Ýptal edilmiþ hareket güncellenemez");

        managed.setHareketTuru(hareket.getHareketTuru());
        managed.setBelgeTuru(hareket.getBelgeTuru());
        managed.setBelgeNo(hareket.getBelgeNo());
        managed.setStTedarikci(hareket.getStTedarikci());
        managed.setAciklama(hareket.getAciklama());
        managed.setKaynakIslemReferansi(hareket.getKaynakIslemReferansi());

        hareketService.edit(managed);
        hareketService.flush();

        // Miktar deðiþmediði için sonuç aynýdýr; yine de özet alanlarý tazeliyoruz.
        miktarService.recalculateBakiye(managed.getStStok(), managed.getStDepo(), managed.getStDepoLokasyon());
        miktarService.recalculateStokKartiMiktar(managed.getStStok());
        return managed;
    }

    /**
     * Hareketi iptal eder (silmez) ve bakiyeyi hareketlerden yeniden hesaplar.
     *
     * <p>Ters kayýt oluþturulmaz; iptal edilen hareket toplam hesabýna girmez.</p>
     */
    public StStokHareket cancel(StStokHareket hareket) {
        if (hareket == null || hareket.getId() == null) throw new IllegalArgumentException("Geçersiz hareket");
        StStokHareket managed = hareketService.find(hareket.getId());
        if (managed == null) throw new IllegalArgumentException("Hareket bulunamadý");
        if (managed.isIptalEdildi()) return managed;

        managed.setIptalEdildi(true);
        hareketService.edit(managed);
        hareketService.flush();

        miktarService.recalculateBakiye(managed.getStStok(), managed.getStDepo(), managed.getStDepoLokasyon());
        miktarService.recalculateStokKartiMiktar(managed.getStStok());
        return managed;
    }
}