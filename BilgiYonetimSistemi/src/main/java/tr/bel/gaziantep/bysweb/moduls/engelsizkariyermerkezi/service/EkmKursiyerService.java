package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyKullandigiCihaz;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyMaddeKullanimi;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlFaydalandigiHak;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGelirKaynagi;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYardimAlinanYerler;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYardimTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyer;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyerKurs;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKurs;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 8.12.2025 08:33
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EkmKursiyerService extends AbstractService<EkmKursiyer> {

    @Serial
    private static final long serialVersionUID = -1278240052111967485L;

    @Inject
    private EyKisiService eyKisiService;

    public EkmKursiyerService() {
        super(EkmKursiyer.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(EkmKursiyer ekmKursiyer,
                       List<EyEngelGrubu> engelGrubus,
                       List<EnumGnlFaydalandigiHak> faydalandigiHakList,
                       List<EnumEyMaddeKullanimi> maddeKullanimis,
                       List<EnumGnlGelirKaynagi> aileninGelirKaynagis,
                       List<EnumGnlYardimAlinanYerler> yardimAlinanYerlers,
                       List<EnumGnlYardimTuru> yardimTurus,
                       List<EnumEyKullandigiCihaz> kullandigiCihazs,
                       List<GnlKurs> gnlKursList) {
        EyKisi eyKisi = ekmKursiyer.getEyKisi();
        eyKisi = eyKisiService.processAllUpdates(eyKisi, engelGrubus, faydalandigiHakList,maddeKullanimis,aileninGelirKaynagis,yardimAlinanYerlers,yardimTurus,
                kullandigiCihazs);
        ekmKursiyer.setEyKisi(eyKisi);

        ekmKursiyer = checkKursiyerGnlKurs(ekmKursiyer, gnlKursList);
        edit(ekmKursiyer);
    }

    private EkmKursiyer checkKursiyerGnlKurs(EkmKursiyer ekmKursiyer, List<GnlKurs> gnlKursList) {
        if (gnlKursList == null || gnlKursList.isEmpty()) {
            return ekmKursiyer;
        }

        List<EkmKursiyerKurs> kursiyerKursList = ekmKursiyer.getEkmKursiyerKursList();
        Set<Integer> newGrubuIds = gnlKursList.stream()
                .map(GnlKurs::getId)
                .collect(Collectors.toSet());

        Map<Integer, EkmKursiyerKurs> existingMap = new HashMap<>();
        for (EkmKursiyerKurs kurs : kursiyerKursList) {
            existingMap.put(kurs.getGnlKurs().getId(), kurs);
            kurs.setSecili(newGrubuIds.contains(kurs.getGnlKurs().getId()));
        }

        for (GnlKurs newGrubu : gnlKursList) {
            if (!existingMap.containsKey(newGrubu.getId())) {
                EkmKursiyerKurs newKeg = new EkmKursiyerKurs();
                newKeg.setEkmKursiyer(ekmKursiyer);
                newKeg.setGnlKurs(newGrubu);
                newKeg.setSecili(true);
                kursiyerKursList.add(newKeg);
            }
        }

        return ekmKursiyer;
    }
}
