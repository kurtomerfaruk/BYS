package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyIncelemeNedeni;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 11:18
 */
@Stateless
public class EyIncelemeNedeniService extends AbstractService<EyIncelemeNedeni> {

    @Serial
    private static final long serialVersionUID = -4783214072473723786L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EyIncelemeNedeniService() {
        super(EyIncelemeNedeni.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}