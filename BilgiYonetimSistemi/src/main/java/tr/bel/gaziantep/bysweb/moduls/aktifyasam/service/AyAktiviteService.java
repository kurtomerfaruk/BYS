package tr.bel.gaziantep.bysweb.moduls.aktifyasam.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyAktivite;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 14:01
 */
@Stateless
public class AyAktiviteService extends AbstractService<AyAktivite> {

    @Serial
    private static final long serialVersionUID = 4732504664202568675L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public AyAktiviteService() {
        super(AyAktivite.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
