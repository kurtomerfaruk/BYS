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
    private StStokBakiyeService stokBakiyeService;
    @Inject
    private StMaliyetService maliyetService;
//    @Inject
//    private StKullaniciDepoYetkiService yetkiService;

    public StStokHareket postMovement(StStokHareket hareket) {
        if (hareket == null || hareket.getStStok() == null || hareket.getStDepo() == null) {
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
        java.time.LocalDate skt = null;
        StStokBakiye bakiye = miktarService.getOrCreateBakiye(stokKarti, depo, lokasyon, skt, LockModeType.PESSIMISTIC_WRITE);

        if (hareket.getDurum() == EnumGirisCikis.GIRIS) {
            bakiye.setMiktar(bakiye.getMiktar().add(miktarTemel));
        } else {
            bakiye.setMiktar(bakiye.getMiktar().subtract(miktarTemel));
        }

        EnumStMaliyetYontemi method = maliyetService.getCostMethod(stokKarti);
        if (hareket.getDurum() == EnumGirisCikis.GIRIS) {
            BigDecimal birimMaliyet = hareket.getBirimMaliyet() == null ? BigDecimal.ZERO : hareket.getBirimMaliyet();
            maliyetService.createCostLayer(hareket, miktarTemel, birimMaliyet);
            hareket.setToplamMaliyet(birimMaliyet.multiply(miktarTemel));
            if (method == EnumStMaliyetYontemi.WEIGHTED_AVERAGE) {
                BigDecimal oldQty = bakiye.getMiktar().subtract(miktarTemel);
                if (oldQty.compareTo(BigDecimal.ZERO) < 0) oldQty = BigDecimal.ZERO;
                BigDecimal oldAvg = bakiye.getOrtalamaMaliyet() == null ? BigDecimal.ZERO : bakiye.getOrtalamaMaliyet();
                BigDecimal totalQty = oldQty.add(miktarTemel);
                if (totalQty.compareTo(BigDecimal.ZERO) > 0) {
                    bakiye.setOrtalamaMaliyet(oldAvg.multiply(oldQty).add(birimMaliyet.multiply(miktarTemel)).divide(totalQty, 4, RoundingMode.HALF_UP));
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

        if (hareket.getIslemTarihi() == null) hareket.setIslemTarihi(LocalDateTime.now());

        if (hareket.getId() == null) hareketService.create(hareket);
        else hareketService.edit(hareket);

        stokBakiyeService.edit(bakiye);
        return hareket;
    }

    public StStokHareket reverseMovement(StStokHareket original, String aciklama) {
        if (original == null || original.getId() == null) throw new IllegalArgumentException("Geçersiz hareket");
        StStokHareket ters = new StStokHareket();
        ters.setStStok(original.getStStok());
        ters.setStDepo(original.getStDepo());
        ters.setStDepoLokasyon(original.getStDepoLokasyon());
        ters.setHedefDepo(original.getHedefDepo());
        ters.setHedefLokasyon(original.getHedefLokasyon());
        ters.setHareketTuru(original.getHareketTuru());
        ters.setDurum(original.getDurum() == EnumGirisCikis.GIRIS ? EnumGirisCikis.CIKIS : EnumGirisCikis.GIRIS);
        ters.setMiktar(original.getMiktar());
        ters.setStBirim(original.getStBirim());
        ters.setBirimMaliyet(original.getBirimMaliyet());
        ters.setIslemTarihi(LocalDateTime.now());
        ters.setSyKullanici(original.getSyKullanici());
        ters.setAciklama(aciklama != null ? aciklama : "Ters kayýt: " + original.getHareketNo());
        ters.setKaynakIslemReferansi(original.getHareketNo());
        ters.setIptalEdildi(false);

        StStokHareket saved = postMovement(ters);
        original.setIptalEdildi(true);
        hareketService.edit(original);
        return saved;
    }
}