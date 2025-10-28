package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmIsBasvuru;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.07.2025 11:32
 */
@Stateless
public class EkmIsBasvuruService extends AbstractService<EkmIsBasvuru> {

    @Serial
    private static final long serialVersionUID = -3126066674044862373L;

    public EkmIsBasvuruService() {
        super(EkmIsBasvuru.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager(){
        return em;
    }

    public List<EkmIsBasvuru> findByGnlKisi(GnlKisi kisi) {
        return getEntityManager().createNamedQuery("EkmIsBasvuru.findByGnlKisi").setParameter("gnlKisi", kisi).getResultList();
    }

    public List<EyKisi> getEyKisiList() {
        return getEntityManager().createNamedQuery("EkmIsBasvuru.getEyKisiList").getResultList();
    }
}
