package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlEgitimBolum;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.07.2025 09:45
 */
@Stateless
public class GnlEgitimBolumService extends AbstractService<GnlEgitimBolum> {

    @Serial
    private static final long serialVersionUID = -977343147462702235L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public GnlEgitimBolumService() {
        super(GnlEgitimBolum.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
