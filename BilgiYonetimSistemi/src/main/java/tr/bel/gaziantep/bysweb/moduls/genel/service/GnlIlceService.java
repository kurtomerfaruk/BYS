package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 20.06.2025 08:32
 */
@Stateless
public class GnlIlceService extends AbstractService<GnlIlce> {


    @Serial
    private static final long serialVersionUID = -7497121258878605924L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public GnlIlceService() {
        super(GnlIlce.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "tanim";
    }

    public GnlIlce findByKod(Integer kod) {
        return (GnlIlce) getEntityManager().createNamedQuery("GnlIlce.findByKod")
                .setParameter("kod",kod)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public List<GnlIlce> findByIlId(int ilId) {
        return getEntityManager().createNamedQuery("GnlIlce.findByIlId").setParameter("ilId",ilId).getResultList();
    }

    public void merge(GnlIlce ilce) {
        if(ilce.getId()==null){
            create(ilce);
        }else{
            edit(ilce);
        }
    }
}
