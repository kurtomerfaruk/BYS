package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyGorusmeKonu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 10:59
 */
@Stateless
public class EyGorusmeKonuService extends AbstractService<EyGorusmeKonu> {

    @Serial
    private static final long serialVersionUID = 7486606207072618036L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EyGorusmeKonuService() {
        super(EyGorusmeKonu.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}