package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHemsireGozlemFormu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 1.10.2026 12:06
 */
@Stateless
public class PkHemsireGozlemFormuService extends AbstractService<PkHemsireGozlemFormu> {

    @Serial
    private static final long serialVersionUID = -4107467552154535427L;

    public PkHemsireGozlemFormuService() {
        super(PkHemsireGozlemFormu.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
