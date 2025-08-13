package tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.entity.EkmIsPozisyon;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.07.2025 10:25
 */
@Stateless
public class EkmIsPozisyonService extends AbstractService<EkmIsPozisyon> {

    @Serial
    private static final long serialVersionUID = -5077760386710604868L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EkmIsPozisyonService() {
        super(EkmIsPozisyon.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
