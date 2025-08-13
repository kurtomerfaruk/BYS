package tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.entity.EkmIsBasvuru;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.07.2025 11:32
 */
@Stateless
public class EkmIsBasvuruService extends AbstractService<EkmIsBasvuru> {

    @Serial
    private static final long serialVersionUID = -3126066674044862373L;

    public EkmIsBasvuruService() {
        super(EkmIsBasvuru.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager(){
        return em;
    }
}
