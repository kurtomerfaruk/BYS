package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkEgitimTur;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkEgitimKonu;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 29.09.2026 10:46
 */
@Stateless
public class PkEgitimKonuService extends AbstractService<PkEgitimKonu> {

    @Serial
    private static final long serialVersionUID = -4829986998277011175L;

    public PkEgitimKonuService() {
        super(PkEgitimKonu.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "tanim";
    }

    public List<PkEgitimKonu> findByTur(EnumPkEgitimTur tur) {
        return getEntityManager().createNamedQuery("PkEgitimKonu.findByTur", PkEgitimKonu.class)
                .setParameter("tur",tur)
                .getResultList();
    }
}
