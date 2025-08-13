package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyAracTamir;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 13:46
 */
@Stateless
public class EyAracTamirService extends AbstractService<EyAracTamir> {

    @Serial
    private static final long serialVersionUID = 5919177377230836455L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EyAracTamirService() {
        super(EyAracTamir.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "kayitTarihi";
    }
}