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
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.*;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 24.11.2026 11:39
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrtKisiService extends AbstractService<OrtKisi> {

    @Serial
    private static final long serialVersionUID = 4581928980705276620L;

    @Inject
    private EyKisiService eyKisiService;

    public OrtKisiService() {
        super(OrtKisi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrtKisi findByGnlKisi(GnlKisi gnlKisi) {
        List<OrtKisi> result = getEntityManager()
                .createNamedQuery("OrtKisi.findByGnlKisi", OrtKisi.class)
                .setParameter("gnlKisi", gnlKisi)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public OrtKisi findByTcKimlikNo(String tcKimlikNo) {
        if (tcKimlikNo == null || tcKimlikNo.isBlank()) {
            return null;
        }
        List<OrtKisi> result = getEntityManager()
                .createNamedQuery("OrtKisi.findByTcKimlikNo", OrtKisi.class)
                .setParameter("tcKimlikNo", tcKimlikNo)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(OrtKisi ortKisi, EyKisi eyKisi,
                     List<EyEngelGrubu> eyEngelGrubus,
                     List<OrtCihaz> ortezCihazList,
                     List<OrtCihaz> protezCihazList,
                     List<EyEngelinOlusumu> engelinOlusumuList,
                     List<OrtProtezAmputeSeviye> amputeSeviyeList) {

        ortKisi = checkOrtKisiKullandigiCihaz(ortKisi, ortezCihazList, EnumOrtCihazTur.ORTEZ);
        ortKisi = checkOrtKisiKullandigiCihaz(ortKisi, protezCihazList, EnumOrtCihazTur.PROTEZ);
        ortKisi = checkOrtKisiProtezAmputeSeviye(ortKisi, amputeSeviyeList);
        edit(ortKisi);

        if (!ortKisi.isEngelli()) {
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

    private OrtKisi checkOrtKisiKullandigiCihaz(OrtKisi ortKisi, List<OrtCihaz> cihazList, EnumOrtCihazTur cihazTur) {
        if (cihazList == null || cihazList.isEmpty()) {
            return ortKisi;
        }

        List<OrtKisiKullandigiCihaz> hastaCihazList =
                ortKisi.getOrtKisiKullandigiCihazList();

        Set<Integer> newGrubuIds = cihazList.stream()
                .map(OrtCihaz::getId)
                .collect(Collectors.toSet());

        Map<Integer, OrtKisiKullandigiCihaz> existingMap = hastaCihazList.stream()
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
                OrtKisiKullandigiCihaz newKeg = new OrtKisiKullandigiCihaz();
                newKeg.setOrtKisi(ortKisi);
                newKeg.setOrtCihaz(cihaz);
                newKeg.setSecili(true);

                hastaCihazList.add(newKeg);
            }
        }

        return ortKisi;
    }

    private OrtKisi checkOrtKisiProtezAmputeSeviye(OrtKisi ortKisi, List<OrtProtezAmputeSeviye> amputeSeviyeList) {
        if (amputeSeviyeList == null || amputeSeviyeList.isEmpty()) {
            return ortKisi;
        }

        List<OrtKisiProtezAmputeSeviye> hastaProtezAmputeSeviye = ortKisi.getOrtKisiProtezAmputeSeviyeList();
        Set<Integer> newGrubuIds = amputeSeviyeList.stream()
                .map(OrtProtezAmputeSeviye::getId)
                .collect(Collectors.toSet());

        Map<Integer, OrtKisiProtezAmputeSeviye> existingMap = new HashMap<>();
        for (OrtKisiProtezAmputeSeviye keg : hastaProtezAmputeSeviye) {
            existingMap.put(keg.getOrtProtezAmputeSeviye().getId(), keg);
            keg.setSecili(newGrubuIds.contains(keg.getOrtProtezAmputeSeviye().getId()));
        }

        for (OrtProtezAmputeSeviye ampute : amputeSeviyeList) {
            if (!existingMap.containsKey(ampute.getId())) {
                OrtKisiProtezAmputeSeviye newKeg = new OrtKisiProtezAmputeSeviye();
                newKeg.setOrtKisi(ortKisi);
                newKeg.setOrtProtezAmputeSeviye(ampute);
                newKeg.setSecili(true);
                hastaProtezAmputeSeviye.add(newKeg);
            }
        }

        return ortKisi;
    }
}
