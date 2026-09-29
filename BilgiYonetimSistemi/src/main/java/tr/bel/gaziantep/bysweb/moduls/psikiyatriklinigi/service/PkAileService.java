package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkAile;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHasta;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 28.09.2026 08:25
 */
@Stateless
public class PkAileService extends AbstractService<PkAile> {

    @Serial
    private static final long serialVersionUID = 1312277907962362006L;

    public PkAileService() {
        super(PkAile.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Aile kaydini, aileye bagli hasta kayitlari ile birlikte kaydeder.
     * Hem yeni aile olusturmada hem de guncellemede ayni metot kullanilir.
     * Hastalar kayitli oldugu icin (id != null) merge ile islenir.
     */
    public PkAile save(PkAile pkAile) {
        if (pkAile == null) {
            return null;
        }
        PkAile aile = getEntityManager().merge(pkAile);
        List<PkHasta> pkHastaList = aile.getPkHastaList();
        if (pkHastaList == null) {
            return aile;
        }
        for (PkHasta pkHasta : pkHastaList) {
            if (pkHasta == null || pkHasta.getId() == null) {
                continue;
            }
            PkAile hastaAilesi = pkHasta.getPkAile();
            if (hastaAilesi == null || !Objects.equals(hastaAilesi.getId(), aile.getId())) {
                pkHasta.setPkAile(aile);
            }
        }
        return aile;
    }

    /**
     * Kayitli bir hastayi aileye eklemeden once kontrol eder.
     * Hasta baska bir aileye bagliysa eklenemez (her hastanin tek ailesi olabilir).
     *
     * @return hasta bu aileye eklenebilir ise true, degilse false
     */
    public boolean aileyeEklenebilirMi(PkHasta pkHasta, PkAile pkAile) {
        if (pkHasta == null || pkHasta.getId() == null || pkAile == null) {
            return false;
        }
        PkHasta kayitliHasta = getEntityManager().find(PkHasta.class, pkHasta.getId());
        if (kayitliHasta == null || !kayitliHasta.isAktif()) {
            return false;
        }
        PkAile hastaAilesi = kayitliHasta.getPkAile();
        return hastaAilesi == null || Objects.equals(hastaAilesi.getId(), pkAile.getId());
    }

}
