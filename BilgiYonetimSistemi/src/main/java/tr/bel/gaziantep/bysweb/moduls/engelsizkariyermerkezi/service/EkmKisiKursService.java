package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKisiKurs;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.10.2025 15:15
 */
@Stateless
public class EkmKisiKursService extends AbstractService<EkmKisiKurs> {

    @Serial
    private static final long serialVersionUID = 5287622480229540760L;

    public EkmKisiKursService() {
        super(EkmKisiKurs.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
