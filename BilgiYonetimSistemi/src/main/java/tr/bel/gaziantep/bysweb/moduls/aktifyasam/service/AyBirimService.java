package tr.bel.gaziantep.bysweb.moduls.aktifyasam.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyBirim;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 10:11
 */
@Stateless
public class AyBirimService extends AbstractService<AyBirim> {

    @Serial
    private static final long serialVersionUID = -8247663497238111304L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public AyBirimService() {
        super(AyBirim.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
