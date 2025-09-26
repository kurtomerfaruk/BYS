package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtPersonel;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.09.2025 08:35
 */
@Stateless
public class OrtPersonelService extends AbstractService<OrtPersonel> {

    @Serial
    private static final long serialVersionUID = 2484522780040360225L;

    public OrtPersonelService() {
        super(OrtPersonel.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrtPersonel findByGnlPersonel(GnlPersonel gnlPersonel) {
        return (OrtPersonel) getEntityManager().createNamedQuery("OrtPersonel.findByGnlPersonel")
                .setParameter("gnlPersonel",gnlPersonel)
                .getResultList()
                .stream().findFirst()
                .orElse(null);
    }
}
