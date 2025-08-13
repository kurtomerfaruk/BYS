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
        return getEntityManager().createNamedQuery("GnlKisi.findByLatLngIsNull").setFirstResult(first).setMaxResults(kayitSayisi).getResultList();
    }

    public void updateLatLng(GnlKisi gnlkisiId) {
        List<GnlKisi> gnlKisis = findByBinaNo(gnlkisiId.getBinaNo());
        gnlKisis.forEach(gnlKisi -> gnlKisi.setLatLng(gnlkisiId.getLatLng()));
        this.editAll(gnlKisis);
    }

    public List<GnlKisi> findByBinaNo(Integer binaNo) {
        return getEntityManager().createNamedQuery("GnlKisi.findByBinaNo").setParameter("binaNo", binaNo).getResultList();
    }

    public List<GnlKisi> getTcList(int kayitSayisi) {
        return getEntityManager().createNamedQuery("GnlKisi.getTcList").setMaxResults(kayitSayisi).getResultList();
    }

    public GnlKisi findByTckimlikNo(String tcKimlikNo) {
        return (GnlKisi) getEntityManager().createNamedQuery("GnlKisi.existByTcKimlikNo")
                .setParameter("tcKimlikNo",tcKimlikNo)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public GnlKisi findByTckimlikNoByDogumTarihi(String tcKimlikNo, LocalDate dogumTarihi) {
        return (GnlKisi) getEntityManager()
                .createNamedQuery("GnlKisi.findByTckimlikNoByDogumTarihi")
                .setParameter("tcKimlikNo", tcKimlikNo)
                .setParameter("dogumTarihi",dogumTarihi)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }
}
