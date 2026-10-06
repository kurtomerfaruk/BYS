package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHizmetTur;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 5.10.2026 10:08
 */
@Stateless
public class PkHizmetTurService extends AbstractService<PkHizmetTur> {

    @Serial
    private static final long serialVersionUID = -7842411223510968025L;

    public PkHizmetTurService() {
        super(PkHizmetTur.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "tanim";
    }
}
