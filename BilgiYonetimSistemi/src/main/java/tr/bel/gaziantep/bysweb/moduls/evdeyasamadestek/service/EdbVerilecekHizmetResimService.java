package tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbVerilecekHizmetResim;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.07.2025 11:47
 */
@Stateless
public class EdbVerilecekHizmetResimService extends AbstractService<EdbVerilecekHizmetResim> {

    @Serial
    private static final long serialVersionUID = -8463071272615426526L;

    public EdbVerilecekHizmetResimService() {
        super(EdbVerilecekHizmetResim.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    protected EntityManager getEntityManager(){
        return  em;
    }
}
