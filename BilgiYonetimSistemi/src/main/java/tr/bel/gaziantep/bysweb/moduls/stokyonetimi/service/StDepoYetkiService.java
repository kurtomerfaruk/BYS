package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyKullaniciTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepoYetki;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 09:21
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StDepoYetkiService extends AbstractService<StDepoYetki> {

    @Serial
    private static final long serialVersionUID = 6078492065231687718L;

    public StDepoYetkiService() {
        super(StDepoYetki.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public boolean isSistemYoneticisi(SyKullanici kullanici) {
        return kullanici != null && kullanici.getKullaniciTuru() == EnumSyKullaniciTuru.SISTEM_YONETICISI;
    }

    public List<StDepoYetki> findByKullanici(SyKullanici kullanici) {
        if (kullanici == null || kullanici.getId() == null) {
            return new ArrayList<>();
        }
        return em.createQuery("SELECT y FROM StDepoYetki y WHERE y.syKullanici = :k AND y.aktif = true",
                        StDepoYetki.class)
                .setParameter("k", kullanici)
                .getResultList();
    }

//    public boolean hasYetki(SyKullanici kullanici, StDepo depo) {
//        if (kullanici == null || depo == null) {
//            return false;
//        }
//        if (isSistemYoneticisi(kullanici)) {
//            return true;
//        }
//        List<StDepoYetki> yetkiler = findByKullanici(kullanici);
//        if (yetkiler.isEmpty()) {
//            // geriye uyum: yetki kaydi yoksa tum depolara yetkili
//            return true;
//        }
//        return yetkiler.stream().anyMatch(y ->
//                y.getStDepo() != null
//                        && y.getStDepo().getId().equals(depo.getId()));
//    }
//
//    public List<StDepo> getYetkiliDepolar(SyKullanici kullanici) {
//        if (kullanici == null) {
//            return new ArrayList<>();
//        }
//        if (isSistemYoneticisi(kullanici)) {
//            // tum aktif depolar
//            return em.createQuery("SELECT d FROM StDepo d WHERE d.aktif = true", StDepo.class).getResultList();
//        }
//        List<StDepoYetki> yetkiler = findByKullanici(kullanici);
//        if (yetkiler.isEmpty()) {
//            return em.createQuery("SELECT d FROM StDepo d WHERE d.aktif = true", StDepo.class).getResultList();
//        }
//        List<StDepo> result = new ArrayList<>();
//        for (StDepoYetki y : yetkiler) {
//            if (y.getStDepo() != null && !result.contains(y.getStDepo())) {
//                result.add(y.getStDepo());
//            }
//        }
//        return result;
//    }

    public StDepoYetki findByKullaniciByDepo(SyKullanici syKullanici, StDepo stDepo) {
        return getEntityManager().createNamedQuery("StDepoYetki.findByKullaniciByDepo", StDepoYetki.class)
                .setParameter("syKullanici", syKullanici)
                .setParameter("stDepo", stDepo)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveNew(StDepoYetki depoYetki, List<StDepo> depoList) {
        for (StDepo stDepo : depoList) {
            StDepoYetki exist = findByKullaniciByDepo(depoYetki.getSyKullanici(), stDepo);
            if (exist == null) {
                StDepoYetki yetki = StDepoYetki.builder().syKullanici(depoYetki.getSyKullanici()).stDepo(stDepo).build();
                create(yetki);
            }
        }
    }
}
