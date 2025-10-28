package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.service;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbBasvuru;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:54
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EdbBasvuruService extends AbstractService<EdbBasvuru> {

    @Serial
    private static final long serialVersionUID = 6778280155570015784L;

    public EdbBasvuruService() {
        super(EdbBasvuru.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }



    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void merge(EdbBasvuru edbBasvuru) {
        GnlKisi gnlKisi = getEntityManager().merge(edbBasvuru.getGnlKisi());
        EyKisi eyKisi = getEntityManager().merge(edbBasvuru.getEyKisi());

        edbBasvuru.setGnlKisi(gnlKisi);
        edbBasvuru.setEyKisi(eyKisi);

        getEntityManager().merge(edbBasvuru);
    }
}
