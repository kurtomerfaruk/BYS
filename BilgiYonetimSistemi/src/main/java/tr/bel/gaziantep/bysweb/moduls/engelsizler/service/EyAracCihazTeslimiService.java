package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyAracCihazTeslimi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 28.06.2025 08:32
 */
@Stateless
public class EyAracCihazTeslimiService extends AbstractService<EyAracCihazTeslimi> {

    @Serial
    private static final long serialVersionUID = 2324008167561392377L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EyAracCihazTeslimiService() {
        super(EyAracCihazTeslimi.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<EyAracCihazTeslimi> findByGnlKisi(GnlKisi kisi) {
        return getEntityManager().createNamedQuery("EyAracCihazTeslimi.findByGnlKisi").setParameter("gnlKisi",kisi).getResultList();
    }
}