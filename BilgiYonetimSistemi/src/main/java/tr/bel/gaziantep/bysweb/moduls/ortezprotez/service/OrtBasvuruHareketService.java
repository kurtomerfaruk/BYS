package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruHareketDurumu;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuru;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuruHareket;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.10.2025 09:26
 */
@Stateless
public class OrtBasvuruHareketService extends AbstractService<OrtBasvuruHareket>     {

    @Serial
    private static final long serialVersionUID = 5598649876804015723L;

    public OrtBasvuruHareketService() {
        super(OrtBasvuruHareket.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void addHistory(OrtBasvuru ortBasvuru, EnumOrtBasvuruHareketDurumu durum) {
        boolean alreadyExists = ortBasvuru.getOrtBasvuruHareketList().stream()
                .anyMatch(x -> x.isAktif() && x.getDurum() == durum);

        if (!alreadyExists) {
            OrtBasvuruHareket hareket = OrtBasvuruHareket.builder()
                    .ortBasvuru(ortBasvuru)
                    .durum(durum)
                    .build();
            ortBasvuru.getOrtBasvuruHareketList().add(hareket);
        }
    }
}
