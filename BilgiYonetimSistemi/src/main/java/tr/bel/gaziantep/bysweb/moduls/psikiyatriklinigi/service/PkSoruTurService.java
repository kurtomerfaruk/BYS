package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkModul;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkSoruTur;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 2.10.2026 09:08
 */
@Stateless
public class PkSoruTurService extends AbstractService<PkSoruTur> {

    @Serial
    private static final long serialVersionUID = 3271229218092182421L;

    public PkSoruTurService() {
        super(PkSoruTur.class);
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

    public List<PkSoruTur> findAktifler(EnumPkModul modul) {
        return em.createQuery("SELECT tur FROM PkSoruTur tur "
                                + "WHERE tur.aktif = true AND tur.modul=:modul ORDER BY tur.siraNo",
                        PkSoruTur.class)
                .setParameter("modul",modul)
                .getResultList();
    }
}
