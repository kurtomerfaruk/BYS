package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisiEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtHasta;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 24.09.2025 09:02
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrtHastaService extends AbstractService<OrtHasta> {

    @Serial
    private static final long serialVersionUID = 4581928980705276620L;

    public OrtHastaService() {
        super(OrtHasta.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(OrtHasta ortHasta, EyKisi eyKisi, List<EyEngelGrubu> eyEngelGrubus) {
        edit(ortHasta);

        if (!ortHasta.isEngelli()) {
            return;
        }

        Map<EyEngelGrubu, EyKisiEngelGrubu> mevcutMap = eyKisi.getEyKisiEngelGrubuList()
                .stream()
                .collect(Collectors.toMap(EyKisiEngelGrubu::getEyEngelGrubu, e -> e));

        for (EyEngelGrubu engelGrubu : eyEngelGrubus) {
            EyKisiEngelGrubu kisiEngel = mevcutMap.get(engelGrubu);
            if (kisiEngel == null) {
                kisiEngel = EyKisiEngelGrubu.builder()
                        .eyEngelGrubu(engelGrubu)
                        .eyKisi(eyKisi)
                        .secili(true)
                        .build();
                eyKisi.getEyKisiEngelGrubuList().add(kisiEngel);
            } else {
                kisiEngel.setSecili(true);
            }
        }

        getEntityManager().merge(eyKisi);
    }
}
