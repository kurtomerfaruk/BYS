package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuruMalzemeTeslimi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.10.2025 11:05
 */
@Stateless
public class OrtBasvuruMalzemeTeslimiService extends AbstractService<OrtBasvuruMalzemeTeslimi> {

    @Serial
    private static final long serialVersionUID = 4762596552786528443L;

    public OrtBasvuruMalzemeTeslimiService() {
        super(OrtBasvuruMalzemeTeslimi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
