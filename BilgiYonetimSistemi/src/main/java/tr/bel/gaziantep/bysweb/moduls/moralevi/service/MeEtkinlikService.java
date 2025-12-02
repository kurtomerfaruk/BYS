package tr.bel.gaziantep.bysweb.moduls.moralevi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeEtkinlik;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.07.2025 14:08
 */
@Stateless
public class MeEtkinlikService extends AbstractService<MeEtkinlik> {

    @Serial
    private static final long serialVersionUID = -7769741447000382004L;

    public MeEtkinlikService() {
        super(MeEtkinlik.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "tarih";
    }

    @Override
    public String getSorting(){
        return "desc";
    }
}
