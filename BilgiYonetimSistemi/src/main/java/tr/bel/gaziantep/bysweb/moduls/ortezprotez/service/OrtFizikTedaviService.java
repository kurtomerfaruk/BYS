package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruHareketDurumu;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuru;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtFizikTedavi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtRandevu;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 8.10.2025 15:29
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrtFizikTedaviService extends AbstractService<OrtFizikTedavi> {

    @Serial
    private static final long serialVersionUID = -8254343121977838658L;

    public OrtFizikTedaviService() {
        super(OrtFizikTedavi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Inject
    private OrtBasvuruHareketService ortBasvuruHareketService;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persist(List<OrtFizikTedavi> fizikTedaviList, OrtBasvuru ortBasvuru) {
        ortBasvuru.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.FIZIK_TEDAVI_PLANI_OLUSTURULDU);
        ortBasvuruHareketService.addHistory(ortBasvuru);
        getEntityManager().merge(ortBasvuru);
        for (OrtFizikTedavi ortFizikTedavi : fizikTedaviList) {
            edit(ortFizikTedavi);
            OrtRandevu randevu = OrtRandevu.builder()
                    .konu("Fizik Tedavi")
                    .aciklama(ortBasvuru.getId()+" nolu başvuruya istinaden Fizik Tedavi için eklenmiştir.")
                    .randevuTarihi(ortFizikTedavi.getTarih())
                    .ortHasta(ortFizikTedavi.getOrtHasta())
                    .build();
            getEntityManager().persist(randevu);
        }
    }
}
