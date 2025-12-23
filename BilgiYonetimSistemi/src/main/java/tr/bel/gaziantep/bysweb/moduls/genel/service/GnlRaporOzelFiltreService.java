package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporOzelFiltre;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 22.12.2025 16:20
 */
@Stateless
public class GnlRaporOzelFiltreService extends AbstractService<GnlRaporOzelFiltre> {

    @Serial
    private static final long serialVersionUID = -2129179983840377683L;

    public GnlRaporOzelFiltreService() {
        super(GnlRaporOzelFiltre.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
