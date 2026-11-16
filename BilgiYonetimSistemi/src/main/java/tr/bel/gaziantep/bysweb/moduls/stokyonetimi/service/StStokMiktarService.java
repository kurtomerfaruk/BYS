package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepoLokasyon;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBakiye;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 11:34
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class StStokMiktarService {

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public StStokBakiye findBakiye(StStok stStok, StDepo depo, StDepoLokasyon lokasyon, LocalDate skt, LockModeType lockMode) {
        StringBuilder jpql = new StringBuilder("SELECT b FROM StStokBakiye b WHERE b.stStok = :stok AND b.stDepo = :depo");
        if (lokasyon == null) jpql.append(" AND b.stDepoLokasyon IS NULL");
        else jpql.append(" AND b.stDepoLokasyon = :lokasyon");
        if (skt == null) jpql.append(" AND b.sonKullanmaTarihi IS NULL");
        else jpql.append(" AND b.sonKullanmaTarihi = :skt");

        TypedQuery<StStokBakiye> query = em.createQuery(jpql.toString(), StStokBakiye.class);
        query.setParameter("stok", stStok);
        query.setParameter("depo", depo);
        if (lokasyon != null) query.setParameter("lokasyon", lokasyon);
        if (skt != null) query.setParameter("skt", skt);
        if (lockMode != null) query.setLockMode(lockMode);

        return query.getResultList().stream().findFirst().orElse(null);
    }

    public StStokBakiye getOrCreateBakiye(StStok stStok, StDepo depo, StDepoLokasyon lokasyon, LocalDate skt, LockModeType lockMode) {
        StStokBakiye bakiye = findBakiye(stStok, depo, lokasyon, skt, lockMode);
        if (bakiye == null) {
            bakiye = new StStokBakiye();
            bakiye.setStStok(stStok);
            bakiye.setStDepo(depo);
            bakiye.setStDepoLokasyon(lokasyon);
            bakiye.setSonKullanmaTarihi(skt);
            bakiye.setMiktar(BigDecimal.ZERO);
            bakiye.setRezerveMiktar(BigDecimal.ZERO);
            bakiye.setTransferdekiMiktar(BigDecimal.ZERO);
            em.persist(bakiye);
            if (lockMode != null) em.lock(bakiye, lockMode);
        }
        return bakiye;
    }

    public BigDecimal getOnHand(StStok stStok, StDepo depo) {
        TypedQuery<BigDecimal> q = em.createQuery(
                "SELECT COALESCE(SUM(b.miktar),0) FROM StStokBakiye b WHERE b.stStok = :stok AND b.stDepo = :depo", BigDecimal.class);
        q.setParameter("stok", stStok);
        q.setParameter("depo", depo);
        BigDecimal result = q.getSingleResult();
        return result == null ? BigDecimal.ZERO : result;
    }

    /**
     * Verilen stok kartý + depo + lokasyon için hareketlerden net miktarý hesaplar.
     */
    public BigDecimal calculateNet(StStok stStok, StDepo depo, StDepoLokasyon lokasyon) {
        StringBuilder jpql = new StringBuilder(
                "SELECT COALESCE(SUM(CASE WHEN h.durum = :giris THEN h.miktar ELSE -h.miktar END), 0) " +
                        "FROM StStokHareket h WHERE h.stStok = :stok AND h.stDepo = :depo " +
                        "AND h.aktif = true AND h.iptalEdildi = false");
        if (lokasyon == null) jpql.append(" AND h.stDepoLokasyon IS NULL");
        else jpql.append(" AND h.stDepoLokasyon = :lokasyon");

        TypedQuery<BigDecimal> q = em.createQuery(jpql.toString(), BigDecimal.class);
        q.setParameter("giris", EnumGirisCikis.GIRIS);
        q.setParameter("stok", stStok);
        q.setParameter("depo", depo);
        if (lokasyon != null) q.setParameter("lokasyon", lokasyon);

        BigDecimal result = q.getSingleResult();
        return result == null ? BigDecimal.ZERO : result;
    }

    /**
     * Stok kartýnýn tüm depolardaki net toplam miktarýný hareketlerden hesaplar.
     */
    public BigDecimal calculateNetToplam(StStok stStok) {
        TypedQuery<BigDecimal> q = em.createQuery(
                "SELECT COALESCE(SUM(CASE WHEN h.durum = :giris THEN h.miktar ELSE -h.miktar END), 0) " +
                        "FROM StStokHareket h WHERE h.stStok = :stok " +
                        "AND h.aktif = true AND h.iptalEdildi = false", BigDecimal.class);
        q.setParameter("giris", EnumGirisCikis.GIRIS);
        q.setParameter("stok", stStok);
        BigDecimal result = q.getSingleResult();
        return result == null ? BigDecimal.ZERO : result;
    }

    /**
     * Depo/lokasyon bakiyesini hareketlerden yeniden hesaplayýp yazar (artýmlý deðil).
     */
    public StStokBakiye recalculateBakiye(StStok stStok, StDepo depo, StDepoLokasyon lokasyon) {
        StStokBakiye bakiye = getOrCreateBakiye(stStok, depo, lokasyon, null, LockModeType.PESSIMISTIC_WRITE);
        bakiye.setMiktar(calculateNet(stStok, depo, lokasyon));
        em.merge(bakiye);
        return bakiye;
    }

    /**
     * Stok kartýndaki toplam miktar alanýný hareketlerden yeniden hesaplar.
     */
    public void recalculateStokKartiMiktar(StStok stStok) {
        if (stStok == null || stStok.getId() == null) return;
        StStok managed = em.find(StStok.class, stStok.getId());
        if (managed != null) {
            managed.setMiktar(calculateNetToplam(managed));
        }
    }

    public BigDecimal getAvailable(StStok stStok, StDepo depo) {
        TypedQuery<Object[]> q = em.createQuery(
                "SELECT COALESCE(SUM(b.miktar),0), COALESCE(SUM(b.rezerveMiktar),0) FROM StStokBakiye b WHERE b.stStok = :stok AND b.stDepo = :depo",
                Object[].class);
        q.setParameter("stok", stStok);
        q.setParameter("depo", depo);
        Object[] row = q.getSingleResult();
        BigDecimal onHand = row[0] == null ? BigDecimal.ZERO : (BigDecimal) row[0];
        BigDecimal reserved = row[1] == null ? BigDecimal.ZERO : (BigDecimal) row[1];
        return onHand.subtract(reserved);
    }

    /**
     * Bakiye özet satýrýný yazar. Miktar hesabý yapmaz.
     */
    public void save(StStokBakiye bakiye) {
        em.merge(bakiye);
    }

    /**
     * Tüm bakiyeleri ve stok kartý toplamlarýný hareketlerden yeniden hesaplar.
     *
     * <p>Mevcut hatalý/þiþmiþ kayýtlarý onarmak için kullanýlýr. Ayný
     * (stok, depo, lokasyon) anahtarýna düþen mükerrer bakiye satýrlarý temizlenir.
     * Hareketlerde son kullanma tarihi tutulmadýðý için bakiye anahtarý
     * (stok, depo, lokasyon) olarak alýnýr.</p>
     *
     * @return yeniden hesaplanan stok kartý sayýsý
     */
    public int recalculateAll() {
        List<StStokBakiye> bakiyeler = em.createQuery(
                "SELECT b FROM StStokBakiye b ORDER BY b.id", StStokBakiye.class).getResultList();

        Map<String, StStokBakiye> tekil = new LinkedHashMap<>();
        for (StStokBakiye bakiye : bakiyeler) {
            String key = bakiyeKey(bakiye);
            if (tekil.containsKey(key)) {
                em.remove(bakiye);
            } else {
                tekil.put(key, bakiye);
            }
        }
        em.flush();

        List<Object[]> hareketAnahtarlari = em.createQuery(
                "SELECT DISTINCT h.stStok.id, h.stDepo.id, h.stDepoLokasyon.id FROM StStokHareket h " +
                        "WHERE h.aktif = true AND h.iptalEdildi = false", Object[].class).getResultList();

        for (Object[] key : hareketAnahtarlari) {
            StStok kart = em.find(StStok.class, (Integer) key[0]);
            StDepo depo = em.find(StDepo.class, (Integer) key[1]);
            StDepoLokasyon lokasyon = key[2] == null ? null : em.find(StDepoLokasyon.class, (Integer) key[2]);
            if (kart == null || depo == null) continue;
            recalculateBakiye(kart, depo, lokasyon);
        }

        for (StStokBakiye bakiye : tekil.values()) {
            bakiye.setMiktar(calculateNet(bakiye.getStStok(), bakiye.getStDepo(), bakiye.getStDepoLokasyon()));
        }

        List<StStok> kartlar = em.createQuery("SELECT s FROM StStok s", StStok.class).getResultList();
        for (StStok kart : kartlar) {
            kart.setMiktar(calculateNetToplam(kart));
        }

        em.flush();
        return kartlar.size();
    }

    private String bakiyeKey(StStokBakiye bakiye) {
        Integer lokasyonId = bakiye.getStDepoLokasyon() == null ? null : bakiye.getStDepoLokasyon().getId();
        return bakiye.getStStok().getId() + ":" + bakiye.getStDepo().getId() + ":" + lokasyonId;
    }
}