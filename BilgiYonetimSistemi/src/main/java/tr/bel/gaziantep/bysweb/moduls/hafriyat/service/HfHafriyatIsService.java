package tr.bel.gaziantep.bysweb.moduls.hafriyat.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.HfFirma;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.HfHafriyatIs;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.07.2025 08:21
 */
@Stateless
public class HfHafriyatIsService extends AbstractService<HfHafriyatIs> {

    @Serial
    private static final long serialVersionUID = -5829337320934372917L;

    public HfHafriyatIsService() {
        super(HfHafriyatIs.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<HfHafriyatIs> findByFirma(HfFirma hfFirma) {
        return getEntityManager().createNamedQuery("HfHafriyatIs.findByFirma").setParameter("firma",hfFirma).getResultList();
    }
}
