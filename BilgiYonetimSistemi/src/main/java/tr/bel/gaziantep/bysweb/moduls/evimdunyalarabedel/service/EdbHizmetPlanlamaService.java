package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbHizmetPlanlama;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.07.2025 08:42
 */
@Stateless
public class EdbHizmetPlanlamaService extends AbstractService<EdbHizmetPlanlama> {

    @Serial
    private static final long serialVersionUID = -4801011837643632009L;

    public EdbHizmetPlanlamaService() {
        super(EdbHizmetPlanlama.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    protected EntityManager getEntityManager(){
        return  em;
    }
}
