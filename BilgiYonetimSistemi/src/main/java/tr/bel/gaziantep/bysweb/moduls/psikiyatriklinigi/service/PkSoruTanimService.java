package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkModul;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkSoruTanim;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 2.10.2026 09:09
 */
@Stateless
public class PkSoruTanimService extends AbstractService<PkSoruTanim> {

    @Serial
    private static final long serialVersionUID = 5898787252700261427L;

    public PkSoruTanimService() {
        super(PkSoruTanim.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<PkSoruTanim> findAktifler(EnumPkModul modul) {
        return em.createQuery("SELECT tanim FROM PkSoruTanim tanim "
                                + "JOIN FETCH tanim.pkSoruTur tur "
                                + "WHERE tur.aktif = true AND tanim.aktif = true AND tur.modul=:modul "
                                + " ORDER BY tur.siraNo, tanim.siraNo",
                        PkSoruTanim.class)
                .setParameter("modul",modul)
                .getResultList();
    }
}
