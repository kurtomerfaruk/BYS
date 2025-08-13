package tr.bel.gaziantep.bysweb.moduls.moralevi.service;

import jakarta.ejb.Stateless;
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
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeKisi;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.07.2025 14:42
 */
@Stateless
public class MeKisiService extends AbstractService<MeKisi> {

    @Serial
    private static final long serialVersionUID = 4175052522554571386L;

    public MeKisiService() {
        super(MeKisi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Inject
    private EyKisiService eyKisiService;

    public void update(MeKisi meKisi, List<EyEngelGrubu> engelGrubus,
                       List<EnumGnlFaydalandigiHak> faydalandigiHaklars,
                       List<EnumEyMaddeKullanimi> maddeKullanimis,
                       List<EnumGnlGelirKaynagi> aileninGelirKaynagis,
                       List<EnumGnlYardimAlinanYerler> yardimAlinanYerlers,
                       List<EnumGnlYardimTuru> yardimTurus,
                       List<EnumEyKullandigiCihaz> kullandigiCihazs) {
        EyKisi eyKisi = eyKisiService.processAllUpdates(meKisi.getEyKisi(), engelGrubus, faydalandigiHaklars, maddeKullanimis,
                aileninGelirKaynagis, yardimAlinanYerlers, yardimTurus, kullandigiCihazs);
        eyKisi = getEntityManager().merge(eyKisi);
        meKisi.setEyKisi(eyKisi);
        edit(meKisi);
    }
}
