package tr.bel.gaziantep.bysweb.moduls.aktifyasam.service;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGun;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.*;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 10:55
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AyKisiService extends AbstractService<AyKisi> {

    @Serial
    private static final long serialVersionUID = 8871877278551526561L;
    
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public AyKisiService() {
        super(AyKisi.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<AyKisi> findByLatLngIsNull(int recordCount) {
        return getEntityManager().createNamedQuery("AyKisi.findByLatLngIsNull").setMaxResults(recordCount).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persist(AyKisi ayKisi, List<AyAktivite> ayAktivites, List<AySanatsalBeceri> aySanatsalBeceris, List<AySaglikBilgi> aySaglikBilgis) {

        ayAktivites.stream()
                .map(ayAktivite -> AyKisiAktivite.builder().ayAktivite(ayAktivite).ayKisi(ayKisi).secili(true).build())
                .forEach(ayKisiAktivite -> ayKisi.getAyKisiAktiviteList().add(ayKisiAktivite));
        aySanatsalBeceris.stream()
                .map(aySanatsalBeceri -> AyKisiSanatsalBeceri.builder().aySanatsalBeceri(aySanatsalBeceri).ayKisi(ayKisi).secili(true).build())
                .forEach(aySanatsalBeceri -> ayKisi.getAyKisiSanatsalBeceriList().add(aySanatsalBeceri));
        aySaglikBilgis.stream()
                .map(aySaglikBilgileri -> AyKisiSaglikBilgi.builder().aySaglikBilgi(aySaglikBilgileri).ayKisi(ayKisi).secili(true).build())
                .forEach(ayKisiSaglikBilgileri -> ayKisi.getAyKisiSaglikBilgileriList().add(ayKisiSaglikBilgileri));
        create(ayKisi);
    }


    public List<AyKisi> findByGun(EnumGnlGun day) {
        return getEntityManager().createNamedQuery("AyKisi.findByGun").setParameter("gun",day).getResultList();
    }
}
