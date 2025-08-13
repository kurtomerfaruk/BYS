package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyGorusme;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.06.2025 00:08
 */
@Stateless
public class EyGorusmeService extends AbstractService<EyGorusme> {

    @Serial
    private static final long serialVersionUID = -8511450501376821660L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EyGorusmeService() {
        super(EyGorusme.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
