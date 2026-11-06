package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtProtezAmputeSeviye;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.27.0
 * @since 6.11.2026 09:43
 */
@Stateless
public class OrtProtezAmputeSeviyeService extends AbstractService<OrtProtezAmputeSeviye> {

    @Serial
    private static final long serialVersionUID = -3208042284932008720L;

    public OrtProtezAmputeSeviyeService() {
        super(OrtProtezAmputeSeviye.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "tanim";
    }
}
