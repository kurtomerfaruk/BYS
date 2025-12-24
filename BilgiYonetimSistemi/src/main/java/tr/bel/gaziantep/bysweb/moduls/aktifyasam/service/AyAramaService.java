package tr.bel.gaziantep.bysweb.moduls.aktifyasam.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.aktifyasam.EnumAyGrup;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGun;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyArama;

import java.io.Serial;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.11.2025 13:46
 */
@Stateless
public class AyAramaService extends AbstractService<AyArama> {

    @Serial
    private static final long serialVersionUID = -7296848927349189209L;

    public AyAramaService() {
        super(AyArama.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<AyArama> findByGunByTarihByGrup(EnumGnlGun day, LocalDate date, EnumAyGrup grup) {
        return getEntityManager().createNamedQuery("AyArama.findByGunByTarihByGrup")
                .setParameter("gun", day)
                .setParameter("tarih", date)
                .setParameter("grup", grup)
                .getResultList();
    }
}
