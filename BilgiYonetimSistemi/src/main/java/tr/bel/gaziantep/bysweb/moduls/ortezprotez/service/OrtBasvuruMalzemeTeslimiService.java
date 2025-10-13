package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruHareketDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtStokHareketTablo;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtStokHareketTur;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuru;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuruMalzemeTeslimi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStok;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStokHareket;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.util.ArrayList;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.10.2025 11:05
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrtBasvuruMalzemeTeslimiService extends AbstractService<OrtBasvuruMalzemeTeslimi> {

    @Serial
    private static final long serialVersionUID = 4762596552786528443L;

    public OrtBasvuruMalzemeTeslimiService() {
        super(OrtBasvuruMalzemeTeslimi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Inject
    private OrtStokHareketService stokHareketService;
    @Inject
    private OrtStokService stokService;
    @Inject
    private OrtBasvuruHareketService ortBasvuruHareketService;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delivery(OrtBasvuruMalzemeTeslimi ortBasvuruMalzemeTeslimi, SyKullanici syKullanici) {
        OrtBasvuru basvuru = ortBasvuruMalzemeTeslimi.getOrtBasvuru();
        basvuru.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.SILIKON_TESLIM_EDILDI);
        ortBasvuruMalzemeTeslimi= refreshEdit(ortBasvuruMalzemeTeslimi);
        ortBasvuruHareketService.addHistory(basvuru, EnumOrtBasvuruHareketDurumu.SILIKON_TESLIM_EDILDI);
        getEntityManager().merge(basvuru);
        OrtStok ortStok =  ortBasvuruMalzemeTeslimi.getOrtStok();
        OrtStokHareket stokHareket = stokHareketService.createHareket(syKullanici,
                ortStok,
                ortBasvuruMalzemeTeslimi.getTeslimTarihi(),
                ortBasvuruMalzemeTeslimi.getAciklama(),
                ortBasvuruMalzemeTeslimi.getMiktar(),
                EnumOrtStokHareketTur.HASTA_ICIN_KULLANIM,
                ortBasvuruMalzemeTeslimi.getId(),
                EnumGirisCikis.CIKIS,
                EnumOrtStokHareketTablo.ORTBASVURU_MALZEME_TESLIMI,
                ortBasvuruMalzemeTeslimi.isAktif());

        stokHareket =  stokHareketService.refreshEdit(stokHareket);
        if (ortStok.getOrtStokHareketList() == null) ortStok.setOrtStokHareketList(new ArrayList<>());
        if(!ortStok.getOrtStokHareketList().contains(stokHareket)) {
            ortStok.getOrtStokHareketList().add(stokHareket);
        }
        stokService.setStokMiktar(ortStok);
    }
}
