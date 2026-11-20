package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBakiye;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 11:51
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StStokBakiyeService extends AbstractService<StStokBakiye> {

    @Serial
    private static final long serialVersionUID = 1202600117311L;

    public StStokBakiyeService() {
        super(StStokBakiye.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Bakiye satirini bulur. kilitle=true ise PESSIMISTIC_WRITE ile kilitler;
     * boylece ayni bakiye uzerinde eczamanli hareketler birbirini bekler.
     */
    public StStokBakiye find(StStok stStok, StDepo stDepo, LocalDate skt, boolean kilitle) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StStokBakiye> cq = cb.createQuery(StStokBakiye.class);
        Root<StStokBakiye> root = cq.from(StStokBakiye.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("stStok"), stStok));
        predicates.add(cb.equal(root.get("stDepo"), stDepo));
        predicates.add(skt == null
                ? cb.isNull(root.get("skt"))
                : cb.equal(root.get("skt"), skt));
        predicates.add(cb.isTrue(root.get("aktif")));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        TypedQuery<StStokBakiye> query = em.createQuery(cq);
        if (kilitle) {
            query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        }
        return query.getResultStream().findFirst().orElse(null);
    }

    /**
     * Bakiye satiri yoksa sifir degerlerle olusturur ve kilitli dondurur.
     */
    public StStokBakiye getOrCreate(StStok stStok, StDepo stDepo, LocalDate skt) {
        StStokBakiye bakiye = find(stStok, stDepo, skt, true);
        if (bakiye != null) {
            return bakiye;
        }

        StStokBakiye yeni = new StStokBakiye();
        yeni.setStStok(stStok);
        yeni.setStDepo(stDepo);
        yeni.setSkt(skt);
        yeni.setMiktar(BigDecimal.ZERO);
        yeni.setRezerveMiktar(BigDecimal.ZERO);
        yeni.setTransferdekiMiktar(BigDecimal.ZERO);
        create(yeni);
        flush();

        return find(stStok, stDepo, skt, true);
    }

    public List<StStokBakiye> findByStok(StStok stStok) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StStokBakiye> cq = cb.createQuery(StStokBakiye.class);
        Root<StStokBakiye> root = cq.from(StStokBakiye.class);
        cq.where(cb.and(cb.equal(root.get("stStok"), stStok), cb.isTrue(root.get("aktif"))));
        return em.createQuery(cq).getResultList();
    }

    /**
     * Bir sayfadaki stoklar icin, verilen depolardaki bakiyeleri tek sorguda dondurur.
     */
    public List<StStokBakiye> findByStokAndDepos(List<Integer> stokIds, List<Integer> depoIds) {
        if (stokIds == null || stokIds.isEmpty() || depoIds == null || depoIds.isEmpty()) {
            return List.of();
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StStokBakiye> cq = cb.createQuery(StStokBakiye.class);
        Root<StStokBakiye> root = cq.from(StStokBakiye.class);
        cq.where(cb.and(
                root.get("stStok").get("id").in(stokIds),
                root.get("stDepo").get("id").in(depoIds),
                cb.isTrue(root.get("aktif"))));
        return em.createQuery(cq).getResultList();
    }
}
