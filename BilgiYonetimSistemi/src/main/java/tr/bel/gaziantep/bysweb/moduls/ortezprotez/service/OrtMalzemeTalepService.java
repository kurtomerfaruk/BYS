package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruHareketDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtMalzemeOnayDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtStokHareketTablo;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtStokHareketTur;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.*;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.10.2025 13:53
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrtMalzemeTalepService extends AbstractService<OrtMalzemeTalep> {

    @Serial
    private static final long serialVersionUID = -1304811183724913035L;

    public OrtMalzemeTalepService() {
        super(OrtMalzemeTalep.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Inject
    private OrtStokService stokService;
    @Inject
    private OrtStokHareketService stokHareketService;
    @Inject
    private OrtBasvuruHareketService ortBasvuruHareketService;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(OrtMalzemeTalep malzemeTalep, SyKullanici syKullanici, EnumOrtBasvuruHareketDurumu durum) {
        OrtMalzemeTalep existingTalep = find(malzemeTalep.getId());
        updateMalzemeFields(existingTalep, malzemeTalep);
        List<OrtMalzemeTalepStok> updatedDetails = malzemeTalep.getOrtMalzemeTalepStokList();
        List<OrtMalzemeTalepStok> existingDetails = existingTalep.getOrtMalzemeTalepStokList();

        existingDetails.stream()
                .filter(detail -> !updatedDetails.contains(detail))
                .forEach(detail -> detail.setAktif(false));

        updatedDetails.forEach(detail -> {
            if (detail.getId() == null) {
                detail.setEkleyen(syKullanici);
                detail.setEklemeTarihi(LocalDateTime.now());
                detail.setOrtMalzemeTalep(existingTalep);
            }
        });

        existingTalep.setOrtMalzemeTalepStokList(updatedDetails);
        refreshEdit(existingTalep);
        basvuruUpdate(existingTalep.getOrtBasvuru(),durum);
    }

    private void updateMalzemeFields(OrtMalzemeTalep target, OrtMalzemeTalep source) {
        target.setOrtBasvuru(source.getOrtBasvuru());
        target.setTalepTarihi(source.getTalepTarihi());
        target.setAciklama(source.getAciklama());
        target.setTalepEdenOrtPersonel(source.getTalepEdenOrtPersonel());
        target.setDurum(source.getDurum());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void approve(OrtMalzemeTalep malzemeTalep, SyKullanici syKullanici, EnumOrtBasvuruHareketDurumu durum) {
        edit(malzemeTalep);
        basvuruUpdate(malzemeTalep.getOrtBasvuru(), durum);
        for (OrtMalzemeTalepStok talepStok : malzemeTalep.getOrtMalzemeTalepStokList()) {
            OrtStok ortStok = talepStok.getOrtStok();

            OrtStokHareket stokHareket = stokHareketService.createHareket(syKullanici,
                    ortStok,
                    malzemeTalep.getOnayTarihi(),
                    talepStok.getAciklama(),
                    talepStok.getMiktar(),
                    EnumOrtStokHareketTur.HASTA_ICIN_KULLANIM,
                    malzemeTalep.getId(),
                    EnumGirisCikis.CIKIS,
                    EnumOrtStokHareketTablo.ORTMALZEME_TALEP,
                    talepStok.isAktif());

            stokHareket = stokHareketService.refreshEdit(stokHareket);
            if (ortStok.getOrtStokHareketList() == null) ortStok.setOrtStokHareketList(new ArrayList<>());
            if (!ortStok.getOrtStokHareketList().contains(stokHareket)) {
                ortStok.getOrtStokHareketList().add(stokHareket);
            }
            stokService.setStokMiktar(ortStok);
        }
    }

//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public void persist(OrtMalzemeTalep malzemeTalep, EnumOrtBasvuruHareketDurumu durum) {
//        create(malzemeTalep);
//        basvuruUpdate(malzemeTalep.getOrtBasvuru(), durum);
//    }

    private void basvuruUpdate(OrtBasvuru basvuru, EnumOrtBasvuruHareketDurumu durum) {
        basvuru.setBasvuruHareketDurumu(durum);
        ortBasvuruHareketService.addHistory(basvuru);
        getEntityManager().merge(basvuru);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveNew(OrtMalzemeTalep malzemeTalep, EnumOrtBasvuruHareketDurumu durum) {
        create(malzemeTalep);
        basvuruUpdate(malzemeTalep.getOrtBasvuru(), durum);
    }

//    public void delivery(OrtMalzemeTalep malzemeTalep, EnumOrtBasvuruHareketDurumu durum) {
//        edit(malzemeTalep);
//        basvuruUpdate(malzemeTalep.getOrtBasvuru(), durum);
//    }
//
//    public void reject(OrtMalzemeTalep malzemeTalep) {
//        edit(malzemeTalep);
//    }

    public void merge(OrtMalzemeTalep malzemeTalep, EnumOrtBasvuruHareketDurumu durum) {
        edit(malzemeTalep);
        basvuruUpdate(malzemeTalep.getOrtBasvuru(), durum);
    }

    public List<OrtMalzemeTalep> findByOrtBasvuruByOnayDurum(OrtBasvuru ortBasvuru, EnumOrtMalzemeOnayDurumu onayDurumu) {
        return getEntityManager().createNamedQuery("OrtMalzemeTalep.findByOrtBasvuruByOnayDurum")
                .setParameter("ortBasvuru",ortBasvuru)
                .setParameter("onayDurumu",onayDurumu)
                .getResultList();
    }
}
