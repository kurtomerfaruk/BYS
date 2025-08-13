package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyYetki;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 16:37
 */
@Stateless
public class SyYetkiService extends AbstractService<SyYetki> {

    @Serial
    private static final long serialVersionUID = -6152787044387066387L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public SyYetkiService() {
        super(SyYetki.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
