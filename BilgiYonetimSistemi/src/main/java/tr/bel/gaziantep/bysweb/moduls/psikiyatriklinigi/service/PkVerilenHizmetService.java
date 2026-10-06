package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkVerilenHizmet;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 5.10.2026 10:08
 */
@Stateless
public class PkVerilenHizmetService extends AbstractService<PkVerilenHizmet> {

    @Serial
    private static final long serialVersionUID = 8082724394189341367L;

    public PkVerilenHizmetService() {
        super(PkVerilenHizmet.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
