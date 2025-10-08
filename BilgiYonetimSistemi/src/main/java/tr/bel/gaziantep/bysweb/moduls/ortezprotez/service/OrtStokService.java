package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStok;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStokHareket;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.10.2025 13:57
 */
@Stateless
public class OrtStokService extends AbstractService<OrtStok> {

    @Serial
    private static final long serialVersionUID = -1595385025559420159L;

    public OrtStokService() {
        super(OrtStok.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void setStokMiktar(OrtStok ortStok) {
        List<OrtStokHareket> harekets = ortStok.getOrtStokHareketList();
        harekets.removeIf(x -> !x.isAktif());
        Map<EnumGirisCikis, BigDecimal> totalMap = harekets
                .stream()
                .collect(Collectors.groupingBy(
                        OrtStokHareket::getDurum,
                        Collectors.reducing(BigDecimal.ZERO, OrtStokHareket::getMiktar, BigDecimal::add)
                ));
        BigDecimal miktar = BigDecimal.ZERO;
        for (Map.Entry<EnumGirisCikis, BigDecimal> item : totalMap.entrySet()) {
            boolean isSum = item.getKey().equals(EnumGirisCikis.GIRIS);
            miktar = miktar.add(isSum ? item.getValue() : item.getValue().negate());
        }
        ortStok.setMiktar(miktar);
        edit(ortStok);
    }
}
