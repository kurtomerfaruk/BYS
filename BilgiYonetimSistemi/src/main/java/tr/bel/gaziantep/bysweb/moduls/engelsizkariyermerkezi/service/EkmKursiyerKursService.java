package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyer;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyerKurs;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKurs;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 8.12.2025 15:00
 */
@Stateless
public class EkmKursiyerKursService extends AbstractService<EkmKursiyerKurs> {
    @Serial
    private static final long serialVersionUID = -6270548912522866338L;

    public EkmKursiyerKursService() {
        super(EkmKursiyerKurs.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<EkmKursiyer> findByKurs(GnlKurs gnlKurs) {
        return getEntityManager().createNamedQuery("EkmKursiyerKurs.findByKurs")
                .setParameter("gnlKurs", gnlKurs)
                .getResultList();
    }
}
