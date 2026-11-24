package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtProtezAmputeSeviye;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 13:24
 */
@Stateless
public class OrtProtezAmputeSeviyeService extends AbstractService<OrtProtezAmputeSeviye> {
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
