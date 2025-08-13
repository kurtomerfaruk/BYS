package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 20.06.2025 08:32
 */
@Stateless
public class GnlMahalleService extends AbstractService<GnlMahalle> {


    @Serial
    private static final long serialVersionUID = 3944596383163799194L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public GnlMahalleService() {
        super(GnlMahalle.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "tanim";
    }

    public GnlMahalle findByKod(Integer kod) {
        return (GnlMahalle) getEntityManager().createNamedQuery("GnlMahalle.findByKod").setParameter("kod", kod)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public List<GnlMahalle> findByIlce(GnlIlce gnlIlce) {
        return getEntityManager().createNamedQuery("GnlMahalle.findByIlce").setParameter("gnlIlce", gnlIlce).getResultList();
    }

    public void merge(GnlMahalle mahalle) {
        if (mahalle.getId() == null) {
            create(mahalle);
        } else {
            edit(mahalle);
        }
    }
}

