package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtPersonel;

import java.io.Serial;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 15:55
 */
@Stateless
public class OrtPersonelService extends AbstractService<OrtPersonel> {

    @Serial
    private static final long serialVersionUID = 2484522780040360225L;

    public OrtPersonelService() {
        super(OrtPersonel.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
