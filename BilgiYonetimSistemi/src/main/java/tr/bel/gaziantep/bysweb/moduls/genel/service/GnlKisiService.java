package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 14:25
 */
@Stateless
public class GnlKisiService extends AbstractService<GnlKisi> {

    @Serial
    private static final long serialVersionUID = 4007833594299844768L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GnlKisiService() {
        super(GnlKisi.class);
    }

    public boolean existByTcKimlikNo(String tcKimlikNo) {
        return !getEntityManager().createNamedQuery("GnlKisi.existByTcKimlikNo")
                .setParameter("tcKimlikNo",tcKimlikNo)
                .getResultList()
                .isEmpty();
    }

    public List<GnlKisi> findByLatLngIsNull(int kayitSayisi) {
        Random random = new Random();
        int first = random.nextInt(kayitSayisi);
        return getEntityManager().createNamedQuery("GnlKisi.findByLatLngIsNull",GnlKisi.class).setFirstResult(first).setMaxResults(kayitSayisi).getResultList();
    }

    public void updateLatLng(GnlKisi gnlkisiId) {
        List<GnlKisi> gnlKisis = findByBinaNo(gnlkisiId.getBinaNo());
        gnlKisis.forEach(gnlKisi -> gnlKisi.setLatLng(gnlkisiId.getLatLng()));
        this.editAll(gnlKisis);
    }

    public List<GnlKisi> findByBinaNo(Integer binaNo) {
        return getEntityManager().createNamedQuery("GnlKisi.findByBinaNo",GnlKisi.class).setParameter("binaNo", binaNo).getResultList();
    }

    public List<GnlKisi> getTcList(int kayitSayisi) {
        return getEntityManager().createNamedQuery("GnlKisi.getTcList",GnlKisi.class).setMaxResults(kayitSayisi).getResultList();
    }

    public GnlKisi findByTckimlikNo(String tcKimlikNo) {
        return  getEntityManager().createNamedQuery("GnlKisi.existByTcKimlikNo",GnlKisi.class)
                .setParameter("tcKimlikNo",tcKimlikNo)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public GnlKisi findByTckimlikNoByDogumTarihi(String tcKimlikNo, LocalDate dogumTarihi) {
        return  getEntityManager()
                .createNamedQuery("GnlKisi.findByTckimlikNoByDogumTarihi",GnlKisi.class)
                .setParameter("tcKimlikNo", tcKimlikNo)
                .setParameter("dogumTarihi",dogumTarihi)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public List<GnlKisi> getByMernisGuncellemeTarihi(int count) {
        return getEntityManager().createNamedQuery("GnlKisi.getByMernisGuncellemeTarihi",GnlKisi.class).setMaxResults(count).getResultList();
    }

    public List<GnlKisi> findByTcKimlikNoListToList(List<String> tcList) {
        return getEntityManager().createNamedQuery("GnlKisi.findByTcKimlikNoListToList",GnlKisi.class).setParameter("tcList",tcList).getResultList();
    }

    public List<GnlKisi> findByYasByYasli(int age,int first,int max) {
        LocalDate date = LocalDate.now().minusYears(age);
        return getEntityManager().createNamedQuery("GnlKisi.findByYasByYasli",GnlKisi.class)
                .setParameter("date",date)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();
    }

    public List<GnlKisi> findByYasByYasli(int age) {
        LocalDate date = LocalDate.now().minusYears(age);
        return getEntityManager().createNamedQuery("GnlKisi.findByYasByYasli",GnlKisi.class)
                .setParameter("date",date)
                .getResultList();
    }

    public List<String> findByTcKimlikNoList(List<String> tcList) {
        return getEntityManager().createNamedQuery("GnlKisi.findByKisiTcKimlikNoList",String.class).setParameter("tcList", tcList).getResultList();
    }
}
