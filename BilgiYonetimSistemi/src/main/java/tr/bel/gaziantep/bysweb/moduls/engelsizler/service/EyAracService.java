package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyArac;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 10:24
 */
@Stateless
public class EyAracService extends AbstractService<EyArac> {


    @Serial
    private static final long serialVersionUID = 5247307824181191122L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EyAracService() {
        super(EyArac.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}