package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyTablo;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 15:56
 */
@Stateless
public class SyTabloService extends AbstractService<SyTablo> {

    @Serial
    private static final long serialVersionUID = -4610140214568294485L;

    public SyTabloService() {
        super(SyTablo.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "tabloAdi";
    }

    public List<String> findAllTableName() {
        List<String> result = new ArrayList<>();
        getEntityManager().getMetamodel().getEntities().forEach(x -> result.add(x.getName()));
        return result;
    }
}
