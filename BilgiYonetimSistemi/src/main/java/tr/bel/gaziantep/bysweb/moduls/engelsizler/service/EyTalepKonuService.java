package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalepKonu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 15:18
 */
@Stateless
public class EyTalepKonuService extends AbstractService<EyTalepKonu> {

    @Serial
    private static final long serialVersionUID = 5669711144792513958L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EyTalepKonuService() {
        super(EyTalepKonu.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "tanim";
    }
}
