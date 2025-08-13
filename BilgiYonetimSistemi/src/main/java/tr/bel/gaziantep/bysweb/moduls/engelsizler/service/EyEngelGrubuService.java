package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:39
 */
@Stateless
public class EyEngelGrubuService extends AbstractService<EyEngelGrubu> {

    @Serial
    private static final long serialVersionUID = -640990854876826376L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EyEngelGrubuService() {
        super(EyEngelGrubu.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EyEngelGrubu findByName(String engelGrup) {
        return (EyEngelGrubu) getEntityManager().createNamedQuery("EyEngelGrubu.findByTanim")
                .setParameter("engelGrup",engelGrup)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }
}
