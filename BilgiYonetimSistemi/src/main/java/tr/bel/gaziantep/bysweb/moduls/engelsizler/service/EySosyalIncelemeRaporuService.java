package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EySosyalIncelemeRaporu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.06.2025 11:41
 */
@Stateless
public class EySosyalIncelemeRaporuService extends AbstractService<EySosyalIncelemeRaporu> {


    @Serial
    private static final long serialVersionUID = 8375118177481977362L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EySosyalIncelemeRaporuService() {
        super(EySosyalIncelemeRaporu.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}