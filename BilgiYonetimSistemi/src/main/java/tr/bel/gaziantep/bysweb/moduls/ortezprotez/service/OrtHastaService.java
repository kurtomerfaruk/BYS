package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtCihazTur;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelinOlusumu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisiEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtCihaz;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtHasta;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtHastaKullandigiCihaz;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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

    @Inject
    private EyKisiService eyKisiService;

    public OrtHastaService() {
        super(OrtHasta.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrtHasta findByTcKimlikNo(String tcKimlikNo) {
        return getEntityManager()
                .createNamedQuery("OrtHasta.findByKisiTcKimlikNo", OrtHasta.class)
                .setParameter("tcKimlikNo", tcKimlikNo)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(OrtHasta ortHasta, EyKisi eyKisi,
                     List<EyEngelGrubu> eyEngelGrubus,
                     List<OrtCihaz> ortezCihazList,
                     List<OrtCihaz> protezCihazList,
                     List<EyEngelinOlusumu> engelinOlusumuList) {

        ortHasta = checkOrtHastaKullandigiCihaz(ortHasta, ortezCihazList, EnumOrtCihazTur.ORTEZ);
        ortHasta = checkOrtHastaKullandigiCihaz(ortHasta, protezCihazList, EnumOrtCihazTur.PROTEZ);
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

        eyKisi = eyKisiService.checkEyKisiEngelinOlusumu(eyKisi, engelinOlusumuList);

        getEntityManager().merge(eyKisi);
    }

    private OrtHasta checkOrtHastaKullandigiCihaz(OrtHasta ortHasta, List<OrtCihaz> cihazList, EnumOrtCihazTur cihazTur) {
        if (cihazList == null || cihazList.isEmpty()) {
            return ortHasta;
        }

        List<OrtHastaKullandigiCihaz> hastaCihazList =
                ortHasta.getOrtHastaKullandigiCihazList();

        Set<Integer> newGrubuIds = cihazList.stream()
                .map(OrtCihaz::getId)
                .collect(Collectors.toSet());

        Map<Integer, OrtHastaKullandigiCihaz> existingMap = hastaCihazList.stream()
                .filter(x -> x.getOrtCihaz().getCihazTur() == cihazTur)
                .collect(Collectors.toMap(
                        x -> x.getOrtCihaz().getId(),
                        Function.identity()
                ));

        // Mevcut cihazların seçim durumunu güncelle
        existingMap.forEach((id, keg) ->
                keg.setSecili(newGrubuIds.contains(id))
        );

        // Yeni cihazları ekle
        for (OrtCihaz cihaz : cihazList) {
            if (!existingMap.containsKey(cihaz.getId())) {
                OrtHastaKullandigiCihaz newKeg = new OrtHastaKullandigiCihaz();
                newKeg.setOrtHasta(ortHasta);
                newKeg.setOrtCihaz(cihaz);
                newKeg.setSecili(true);

                hastaCihazList.add(newKeg);
            }
        }

        return ortHasta;
    }
}
