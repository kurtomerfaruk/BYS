package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHastaTedaviSekli;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 25.09.2026 10:42
 */
@Stateless
public class PkHastaTedaviSekliService  extends AbstractService<PkHastaTedaviSekli> {

    @Serial
    private static final long serialVersionUID = -7791949502888963582L;

    public PkHastaTedaviSekliService() {
        super(PkHastaTedaviSekli.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
