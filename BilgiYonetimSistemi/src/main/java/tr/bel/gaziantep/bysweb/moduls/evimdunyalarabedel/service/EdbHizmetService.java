package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbHizmetTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbHizmet;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 09:03
 */
@Stateless
public class EdbHizmetService extends AbstractService<EdbHizmet> {

    @Serial
    private static final long serialVersionUID = -3918369666042742042L;

    public EdbHizmetService() {
        super(EdbHizmet.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<EdbHizmet> findByHizmetTuru(EnumEdbHizmetTuru hizmetTuru) {
        return getEntityManager().createNamedQuery("EdbHizmet.findByHizmetTuru").setParameter("hizmetTuru",hizmetTuru).getResultList();
    }
}
