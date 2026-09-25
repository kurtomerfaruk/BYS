package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHasta;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHastaMaddeKullanimi;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkMadde;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 22.09.2026 14:20
 */
@Stateless
public class PkHastaService  extends AbstractService<PkHasta> {

    @Serial
    private static final long serialVersionUID = -8365947549596330975L;

    public PkHastaService() {
        super(PkHasta.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PkHasta findByTcKimlikNo(@Size(max = 11) String tcKimlikNo) {
        return  getEntityManager()
                .createNamedQuery("PkHasta.findByKisiTcKimlikNo",PkHasta.class)
                .setParameter("tcKimlikNo", tcKimlikNo)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public void update(PkHasta pkHasta, List<PkMadde> maddeList, List<String> maddeKullananBireyler, List<String> alkolKullananBireyler) {

        pkHasta = processAllUpdates(pkHasta, maddeList, maddeKullananBireyler, alkolKullananBireyler);
        edit(pkHasta);
    }

    public PkHasta processAllUpdates(PkHasta pkHasta, List<PkMadde> maddeList, List<String> maddeKullananBireyler, List<String> alkolKullananBireyler) {

        pkHasta = checkPkMadde(pkHasta, maddeList);
        pkHasta.setAiledeMaddeKullanimiOlanBireyler(StringUtils.join(maddeKullananBireyler, "|"));
        pkHasta.setAiledeAlkolKullanimiOlanBirey(StringUtils.join(alkolKullananBireyler, "|"));
        return pkHasta;
    }

    public PkHasta checkPkMadde(PkHasta pkHasta, List<PkMadde> maddeList) {
        if (maddeList == null || maddeList.isEmpty()) {
            return pkHasta;
        }

        List<PkHastaMaddeKullanimi> maddeKullanimiList = pkHasta.getPkHastaMaddeKullanimiList();
        Set<Integer> newGrubuIds = maddeList.stream()
                .map(PkMadde::getId)
                .collect(Collectors.toSet());

        Map<Integer, PkHastaMaddeKullanimi> existingMap = new HashMap<>();
        for (PkHastaMaddeKullanimi keg : maddeKullanimiList) {
            existingMap.put(keg.getPkMadde().getId(), keg);
            keg.setSecili(newGrubuIds.contains(keg.getPkMadde().getId()));
        }

        for (PkMadde newGrubu : maddeList) {
            if (!existingMap.containsKey(newGrubu.getId())) {
                PkHastaMaddeKullanimi newKeg = new PkHastaMaddeKullanimi();
                newKeg.setPkHasta(pkHasta);
                newKeg.setPkMadde(newGrubu);
                newKeg.setSecili(true);
                maddeKullanimiList.add(newKeg);
            }
        }

        return pkHasta;
    }
}
