package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisiEngelGrubu;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 1.10.2025 09:11
 */
@Stateless
public class EyKisiEngelGrubuService extends AbstractService<EyKisiEngelGrubu> {

    @Serial
    private static final long serialVersionUID = -7013186229396466049L;

    public EyKisiEngelGrubuService() {
        super(EyKisiEngelGrubu.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<EyEngelGrubu> getEyEngelGrubuByEyKisi(EyKisi eyKisi) {
        return getEntityManager().createNamedQuery("EyKisiEngelGrubu.getEyEngelGrubuByEyKisi")
                .setParameter("eyKisi",eyKisi)
                .getResultList();
    }
}
