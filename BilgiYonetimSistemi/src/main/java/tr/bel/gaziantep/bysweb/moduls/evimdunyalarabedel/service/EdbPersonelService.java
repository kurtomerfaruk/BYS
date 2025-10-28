package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbPersonel;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 09:23
 */
@Stateless
public class EdbPersonelService extends AbstractService<EdbPersonel> {

    @Serial
    private static final long serialVersionUID = -6611758626741563174L;

    public EdbPersonelService() {
        super(EdbPersonel.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
