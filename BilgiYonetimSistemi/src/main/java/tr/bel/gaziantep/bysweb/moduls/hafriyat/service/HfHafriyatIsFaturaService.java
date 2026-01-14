package tr.bel.gaziantep.bysweb.moduls.hafriyat.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.HfHafriyatIsFatura;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.01.2026 10:39
 */
@Stateless
public class HfHafriyatIsFaturaService extends AbstractService<HfHafriyatIsFatura> {

    @Serial
    private static final long serialVersionUID = 4644893364184908724L;

    public HfHafriyatIsFaturaService() {
        super(HfHafriyatIsFatura.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
