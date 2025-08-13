package tr.bel.gaziantep.bysweb.moduls.aktifyasam.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyEtkinlikResim;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 3.07.2025 10:52
 */
@Stateless
public class AyEtkinlikResimService extends AbstractService<AyEtkinlikResim> {

    @Serial
    private static final long serialVersionUID = -8544156472203940291L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public AyEtkinlikResimService() {
        super(AyEtkinlikResim.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
