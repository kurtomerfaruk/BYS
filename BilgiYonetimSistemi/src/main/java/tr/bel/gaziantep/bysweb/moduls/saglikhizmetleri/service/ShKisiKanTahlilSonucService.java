package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisiKanTahlilSonuc;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.07.2025 13:12
 */
@Stateless
public class ShKisiKanTahlilSonucService extends AbstractService<ShKisiKanTahlilSonuc> {

    @Serial
    private static final long serialVersionUID = 1178219980069084720L;

    public ShKisiKanTahlilSonucService() {
        super(ShKisiKanTahlilSonuc.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<ShKisiKanTahlilSonuc> findByGnlKisi(GnlKisi kisi) {
        return getEntityManager().createNamedQuery("ShKisiKanTahlilSonuc.findByGnlKisi").setParameter("gnlKisi",kisi).getResultList();
    }
}
