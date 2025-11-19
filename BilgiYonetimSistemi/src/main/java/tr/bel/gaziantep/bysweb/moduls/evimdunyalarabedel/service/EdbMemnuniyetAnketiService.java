package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbMemnuniyetAnketi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.11.2025 16:13
 */
@Stateless
public class EdbMemnuniyetAnketiService extends AbstractService<EdbMemnuniyetAnketi> {

    @Serial
    private static final long serialVersionUID = 4622772105315411722L;

    public EdbMemnuniyetAnketiService() {
        super(EdbMemnuniyetAnketi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
