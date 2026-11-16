package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStMaliyetYontemi;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyGenelAyar;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyGenelAyarService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 11:09
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class StMaliyetService {

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Inject
    private StMaliyetKatmaniService katmaniService;
    @Inject
    private StMaliyetTuketimService tuketimService;
    @Inject
    private SyGenelAyarService syGenelAyarService;

    public EnumStMaliyetYontemi getCostMethod(StStok stokKarti) {
        if (stokKarti != null && stokKarti.getMaliyetYontemi() != null) {
            return stokKarti.getMaliyetYontemi();
        }
        SyGenelAyar ayar = findAyar("STOK_MALIYET_YONTEMI");
        if (ayar != null && ayar.getDeger() != null) {
            try {
                return EnumStMaliyetYontemi.valueOf(ayar.getDeger());
            } catch (IllegalArgumentException ignored) {
            }
        }
        return EnumStMaliyetYontemi.FIFO;
    }

    public boolean isNegativeStockAllowed() {
        SyGenelAyar ayar = findAyar("NEGATIF_STOK_IZIN");
        return ayar != null && "true".equalsIgnoreCase(ayar.getDeger());
    }

    private SyGenelAyar findAyar(String anahtar) {
        return syGenelAyarService.findByTanim(anahtar);
    }

    public StMaliyetKatmani createCostLayer(StStokHareket girisHareket, BigDecimal miktarTemel, BigDecimal birimMaliyet) {
        StMaliyetKatmani katman = new StMaliyetKatmani();
        katman.setStStok(girisHareket.getStStok());
        katman.setStDepo(girisHareket.getStDepo());
        katman.setGirisHareket(girisHareket);
        katman.setGirisTarihi(girisHareket.getIslemTarihi() != null ? girisHareket.getIslemTarihi() : LocalDateTime.now());
        katman.setMiktar(miktarTemel);
        katman.setKalanMiktar(miktarTemel);
        katman.setBirimMaliyet(birimMaliyet == null ? BigDecimal.ZERO : birimMaliyet);
        katman.setToplamMaliyet(katman.getBirimMaliyet().multiply(miktarTemel));
        katmaniService.create(katman);
        return katman;
    }

    public BigDecimal consumeCostLayers(StStok stokKarti, StDepo depo, BigDecimal miktarTemel,
                                        EnumStMaliyetYontemi method, StStokHareket cikisHareket) {
        if (miktarTemel == null || miktarTemel.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        String order = (method == EnumStMaliyetYontemi.LIFO) ? "DESC" : "ASC";
        StringBuilder jpql = new StringBuilder("SELECT k FROM StMaliyetKatmani k WHERE k.stStok = :stok AND k.aktif = true AND k.kalanMiktar > 0");
        if (depo != null) jpql.append(" AND k.stDepo = :depo");
        jpql.append(" ORDER BY k.girisTarihi ").append(order).append(", k.id ").append(order);

        TypedQuery<StMaliyetKatmani> q = em.createQuery(jpql.toString(), StMaliyetKatmani.class);
        q.setParameter("stok", stokKarti);
        if (depo != null) q.setParameter("depo", depo);
        List<StMaliyetKatmani> layers = q.getResultList();

        BigDecimal remaining = miktarTemel;
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal fallbackCost = layers.isEmpty() ? BigDecimal.ZERO : layers.get(layers.size() - 1).getBirimMaliyet();

        for (StMaliyetKatmani layer : layers) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
            BigDecimal consumeQty = layer.getKalanMiktar().min(remaining);
            layer.setKalanMiktar(layer.getKalanMiktar().subtract(consumeQty));
            katmaniService.edit(layer);

            BigDecimal lineCost = layer.getBirimMaliyet().multiply(consumeQty);
            totalCost = totalCost.add(lineCost);

            StMaliyetTuketim tuketim = new StMaliyetTuketim();
            tuketim.setCikisHareket(cikisHareket);
            tuketim.setStMaliyetKatmani(layer);
            tuketim.setMiktar(consumeQty);
            tuketim.setBirimMaliyet(layer.getBirimMaliyet());
            tuketim.setToplamMaliyet(lineCost);
            tuketimService.create(tuketim);

            remaining = remaining.subtract(consumeQty);
        }

        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            if (!isNegativeStockAllowed()) {
                throw new IllegalStateException("Yetersiz stok: " + stokKarti.getStokKodu());
            }
            StMaliyetKatmani synthetic = new StMaliyetKatmani();
            synthetic.setStStok(stokKarti);
            synthetic.setStDepo(depo);
            synthetic.setGirisHareket(cikisHareket);
            synthetic.setGirisTarihi(LocalDateTime.now());
            synthetic.setMiktar(remaining);
            synthetic.setKalanMiktar(BigDecimal.ZERO);
            synthetic.setBirimMaliyet(fallbackCost);
            synthetic.setToplamMaliyet(fallbackCost.multiply(remaining));
            katmaniService.create(synthetic);

            BigDecimal lineCost = fallbackCost.multiply(remaining);
            totalCost = totalCost.add(lineCost);

            StMaliyetTuketim tuketim = new StMaliyetTuketim();
            tuketim.setCikisHareket(cikisHareket);
            tuketim.setStMaliyetKatmani(synthetic);
            tuketim.setMiktar(remaining);
            tuketim.setBirimMaliyet(fallbackCost);
            tuketim.setToplamMaliyet(lineCost);
            tuketimService.create(tuketim);
        }

        return totalCost;
    }

    public BigDecimal calculateAverageCost(StStok stokKarti, StDepo depo) {
        TypedQuery<Object[]> q = em.createQuery(
                "SELECT COALESCE(SUM(k.kalanMiktar),0), COALESCE(SUM(k.kalanMiktar * k.birimMaliyet),0) " +
                        "FROM StMaliyetKatmani k WHERE k.stStok = :stok AND k.aktif = true AND k.kalanMiktar > 0" +
                        (depo != null ? " AND k.stDepo = :depo" : ""), Object[].class);
        q.setParameter("stok", stokKarti);
        if (depo != null) q.setParameter("depo", depo);
        Object[] row = q.getSingleResult();
        BigDecimal qty = row[0] == null ? BigDecimal.ZERO : (BigDecimal) row[0];
        BigDecimal value = row[1] == null ? BigDecimal.ZERO : (BigDecimal) row[1];
        if (qty.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return value.divide(qty, 4, RoundingMode.HALF_UP);
    }
}
