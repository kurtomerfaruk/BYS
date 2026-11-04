package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtCihazTur;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtCihaz;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.26.0
 * @since 3.11.2026 13:50
 */
@Stateless
public class OrtCihazService extends AbstractService<OrtCihaz> {

    @Serial
    private static final long serialVersionUID = -5752513368113726485L;

    public OrtCihazService() {
        super(OrtCihaz.class);
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

    public List<OrtCihaz> findByCihazTur(EnumOrtCihazTur cihazTur) {
        return getEntityManager().createNamedQuery("OrtCihaz.findByCihazTur",OrtCihaz.class)
                .setParameter("cihazTur",cihazTur)
                .getResultList();
    }
}
