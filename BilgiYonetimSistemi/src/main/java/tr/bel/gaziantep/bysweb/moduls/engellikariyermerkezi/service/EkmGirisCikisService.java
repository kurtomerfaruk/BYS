package tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.entity.EkmGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.08.2025 15:24
 */
@Stateless
public class EkmGirisCikisService extends AbstractService<EkmGirisCikis>     {
    @Serial
    private static final long serialVersionUID = 7436165278378884429L;

    public EkmGirisCikisService() {
        super(EkmGirisCikis.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager(){
        return em;
    }

    public List<EkmGirisCikis> findByGnlKisi(GnlKisi kisi) {
        return getEntityManager().createNamedQuery("EkmGirisCikis.findByGnlKisi").setParameter("gnlKisi",kisi).getResultList();
    }
}
