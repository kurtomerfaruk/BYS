package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepoLokasyon;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBakiye;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    public StStokBakiye findBakiye(StStok stokKarti, StDepo depo, StDepoLokasyon lokasyon, LocalDate skt, LockModeType lockMode) {
        StringBuilder jpql = new StringBuilder("SELECT b FROM StStokBakiye b WHERE b.stStok = :stok AND b.stDepo = :depo");
        if (lokasyon == null) jpql.append(" AND b.stDepoLokasyon IS NULL");
        else jpql.append(" AND b.stDepoLokasyon = :lokasyon");
        if (skt == null) jpql.append(" AND b.sonKullanmaTarihi IS NULL");
        else jpql.append(" AND b.sonKullanmaTarihi = :skt");

        TypedQuery<StStokBakiye> query = em.createQuery(jpql.toString(), StStokBakiye.class);
        query.setParameter("stok", stokKarti);
        query.setParameter("depo", depo);
        if (lokasyon != null) query.setParameter("lokasyon", lokasyon);
        if (skt != null) query.setParameter("skt", skt);
        if (lockMode != null) query.setLockMode(lockMode);

        return query.getResultList().stream().findFirst().orElse(null);
    }

    public StStokBakiye getOrCreateBakiye(StStok stokKarti, StDepo depo, StDepoLokasyon lokasyon, LocalDate skt, LockModeType lockMode) {
        StStokBakiye bakiye = findBakiye(stokKarti, depo, lokasyon, skt, lockMode);
        if (bakiye == null) {
            bakiye = new StStokBakiye();
            bakiye.setStStok(stokKarti);
            bakiye.setStDepo(depo);
            bakiye.setStDepoLokasyon(lokasyon);
            bakiye.setSonKullanmaTarihi(skt);
            bakiye.setMiktar(BigDecimal.ZERO);
            bakiye.setRezerveMiktar(BigDecimal.ZERO);
            bakiye.setTransferdekiMiktar(BigDecimal.ZERO);
            em.persist(bakiye);
            if (lockMode != null) em.lock(bakiye, lockMode);
            updateStokMiktar(stokKarti);
        }
        return bakiye;
    }

    public BigDecimal getOnHand(StStok stokKarti, StDepo depo) {
        TypedQuery<BigDecimal> q = em.createQuery(
                "SELECT COALESCE(SUM(b.miktar),0) FROM StStokBakiye b WHERE b.stStok = :stok AND b.stDepo = :depo", BigDecimal.class);
        q.setParameter("stok", stokKarti);
        q.setParameter("depo", depo);
        BigDecimal result = q.getSingleResult();
        return result == null ? BigDecimal.ZERO : result;
    }

    public BigDecimal getToplamMiktar(StStok stokKarti) {
        jakarta.persistence.TypedQuery<BigDecimal> q = em.createQuery(
                "SELECT COALESCE(SUM(b.miktar),0) FROM StStokBakiye b WHERE b.stStok = :stok AND b.aktif = true", BigDecimal.class);
        q.setParameter("stok", stokKarti);
        BigDecimal result = q.getSingleResult();
        return result == null ? BigDecimal.ZERO : result;
    }

    public BigDecimal getAvailable(StStok stokKarti, StDepo depo) {
        TypedQuery<Object[]> q = em.createQuery(
                "SELECT COALESCE(SUM(b.miktar),0), COALESCE(SUM(b.rezerveMiktar),0) FROM StStokBakiye b WHERE b.stStok = :stok AND b.stDepo = :depo",
                Object[].class);
        q.setParameter("stok", stokKarti);
        q.setParameter("depo", depo);
        Object[] row = q.getSingleResult();
        BigDecimal onHand = row[0] == null ? BigDecimal.ZERO : (BigDecimal) row[0];
        BigDecimal reserved = row[1] == null ? BigDecimal.ZERO : (BigDecimal) row[1];
        return onHand.subtract(reserved);
    }

    public void save(StStokBakiye bakiye) {
        em.merge(bakiye);
        updateStokMiktar(bakiye.getStStok());
    }

    private void updateStokMiktar(StStok stokKarti) {
        if (stokKarti == null || stokKarti.getId() == null) return;
        StStok managed = em.find(StStok.class, stokKarti.getId());
        if (managed != null) {
            managed.setMiktar(getToplamMiktar(managed));
        }
    }
}
