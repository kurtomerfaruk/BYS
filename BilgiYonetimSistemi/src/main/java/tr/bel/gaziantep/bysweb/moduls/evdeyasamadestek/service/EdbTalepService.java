package tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbTalep;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 15:55
 */
@Stateless
public class EdbTalepService extends AbstractService<EdbTalep> {

    @Serial
    private static final long serialVersionUID = 6036587484324859685L;

    public EdbTalepService() {
        super(EdbTalep.class);
    }
    
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;
    
    protected EntityManager getEntityManager(){
        return  em;
    }
}
