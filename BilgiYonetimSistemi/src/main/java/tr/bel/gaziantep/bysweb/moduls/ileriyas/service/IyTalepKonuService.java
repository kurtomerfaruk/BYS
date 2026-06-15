package tr.bel.gaziantep.bysweb.moduls.ileriyas.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.entity.IyTalepKonu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.11.0
 * @since 15.06.2026 08:28
 */
@Stateless
public class IyTalepKonuService extends AbstractService<IyTalepKonu> {

    @Serial
    private static final long serialVersionUID = 6539914749159024505L;

    public IyTalepKonuService() {
        super(IyTalepKonu.class);
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
