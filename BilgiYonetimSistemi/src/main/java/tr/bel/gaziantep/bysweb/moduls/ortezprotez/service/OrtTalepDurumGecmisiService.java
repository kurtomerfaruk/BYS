package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalepDurumGecmisi;

import java.io.Serial;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 15:55
 */
@Stateless
public class OrtTalepDurumGecmisiService extends AbstractService<OrtTalepDurumGecmisi> {

    @Serial
    private static final long serialVersionUID = -3826793125594775280L;

    public OrtTalepDurumGecmisiService() {
        super(OrtTalepDurumGecmisi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
