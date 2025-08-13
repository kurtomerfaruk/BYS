package tr.bel.gaziantep.bysweb.moduls.hafriyat.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.HfFirma;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 28.07.2025 16:30
 */
@Stateless
public class HfFirmaService extends AbstractService<HfFirma> {

    @Serial
    private static final long serialVersionUID = 2770097831406976253L;

    public HfFirmaService() {
        super(HfFirma.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
