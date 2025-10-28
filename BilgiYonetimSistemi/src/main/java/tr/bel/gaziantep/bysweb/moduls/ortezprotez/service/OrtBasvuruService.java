package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruHareketDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtStokHareketTablo;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtStokHareketTur;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.*;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 30.09.2025 13:47
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrtBasvuruService extends AbstractService<OrtBasvuru> {

    @Serial
    private static final long serialVersionUID = 4259094003224116213L;


    public OrtBasvuruService() {
        super(OrtBasvuru.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Inject
    private OrtBasvuruHareketService ortBasvuruHareketService;
    @Inject
    private OrtStokHareketService stokHareketService;
    @Inject
    private OrtStokService stokService;

//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public void save(OrtBasvuru ortBasvuru, boolean addAppointment, LocalDateTime appointmentDate) {
//        if(ortBasvuru.getBasvuruDurumu()== EnumOrtBasvuruDurumu.OLUMLU){
//            ortBasvuru = addHistory(ortBasvuru,EnumOrtBasvuruHareketDurumu.OLUMLU);
//        }
//        if (addAppointment) {
//            OrtRandevu randevu =OrtRandevu.builder()
//                    .randevuTarihi(appointmentDate)
//                    .ortHasta(ortBasvuru.getOrtHasta())
//                    .konu("Ölçü alımı")
//                    .aciklama("Başvuruya istinaden randevu verilmiştir.")
//                    .build();
//            getEntityManager().persist(randevu);
//
//            ortBasvuru=addHistory(ortBasvuru,EnumOrtBasvuruHareketDurumu.OLCU_ICIN_RANDEVU_VERILDI);
//            ortBasvuru.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.OLCU_ICIN_RANDEVU_VERILDI);
//        }
//
//        edit(ortBasvuru);
//    }
//
//    private OrtBasvuru addHistory(OrtBasvuru ortBasvuru, EnumOrtBasvuruHareketDurumu durum) {
//        OrtBasvuruHareket hareket =ortBasvuru.getOrtBasvuruHareketList().stream()
//                .filter(x-> x.isAktif() && x.getDurum()==durum)
//                .findFirst()
//                .orElse(null);
//        if(hareket==null){
//            hareket = OrtBasvuruHareket.builder()
//                    .ortBasvuru(ortBasvuru)
//                    .durum(durum)
//                    .build();
//            getEntityManager().persist(hareket);
//            ortBasvuru.getOrtBasvuruHareketList().add(hareket);
//        }
//        return ortBasvuru;
//    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(OrtBasvuru ortBasvuru, boolean addAppointment, LocalDateTime appointmentDate) {
        if (ortBasvuru.getBasvuruDurumu() == EnumOrtBasvuruDurumu.OLUMLU) {
            ortBasvuru.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.OLUMLU);
            ortBasvuruHareketService.addHistory(ortBasvuru);
        }

        if (addAppointment) {
            OrtRandevu randevu = OrtRandevu.builder()
                    .randevuTarihi(appointmentDate)
                    .ortHasta(ortBasvuru.getOrtHasta())
                    .konu("Ölçü alımı")
                    .aciklama("Başvuruya istinaden randevu verilmiştir.")
                    .build();
            getEntityManager().persist(randevu);
            ortBasvuru.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.OLCU_ICIN_RANDEVU_VERILDI);
            ortBasvuruHareketService.addHistory(ortBasvuru);
        }

        edit(ortBasvuru);
    }

//    private void addHistory(OrtBasvuru ortBasvuru, EnumOrtBasvuruHareketDurumu durum) {
//        boolean alreadyExists = ortBasvuru.getOrtBasvuruHareketList().stream()
//                .anyMatch(x -> x.isAktif() && x.getDurum() == durum);
//
//        if (!alreadyExists) {
//            OrtBasvuruHareket hareket = OrtBasvuruHareket.builder()
//                    .ortBasvuru(ortBasvuru)
//                    .durum(durum)
//                    .build();
//            ortBasvuru.getOrtBasvuruHareketList().add(hareket);
//        }
//    }

//    public void pay(OrtBasvuru ortBasvuru) {
//        if (StringUtil.isNotBlank(ortBasvuru.getMakbuzNo())) {
//            ortBasvuruHareketService.addHistory(ortBasvuru, EnumOrtBasvuruHareketDurumu.ODEME_ALINDI);
//            ortBasvuru.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.ODEME_ALINDI);
//        }
//        edit(ortBasvuru);
//
//    }
//
//    public void saveSutKodu(OrtBasvuru ortBasvuru) {
//        ortBasvuruHareketService.addHistory(ortBasvuru, EnumOrtBasvuruHareketDurumu.SUT_KODU_VERILDI);
//        ortBasvuru.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.SUT_KODU_VERILDI);
//        edit(ortBasvuru);
//    }

    public void saveDurum(OrtBasvuru ortBasvuru, EnumOrtBasvuruHareketDurumu durum) {
        ortBasvuru.setBasvuruHareketDurumu(durum);
        ortBasvuruHareketService.addHistory(ortBasvuru);
        edit(ortBasvuru);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveNew(OrtBasvuru ortBasvuru) {
        ortBasvuru.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.BEKLEMEDE);
        ortBasvuruHareketService.addHistory(ortBasvuru);
        create(ortBasvuru);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveDelivery(OrtBasvuru basvuru, SyKullanici syKullanici) {
        basvuru.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.URUNLER_TESLIM_EDILDI);
        ortBasvuruHareketService.addHistory(basvuru);
        for (OrtBasvuruMalzemeTeslimi teslim : basvuru.getOrtBasvuruMalzemeTeslimiList()) {
            teslim.setStoktanDus(true);
            teslim = getEntityManager().merge(teslim);
            OrtStok ortStok = teslim.getOrtStok();
            OrtStokHareket stokHareket = stokHareketService.createHareket(syKullanici,
                    ortStok,
                    teslim.getTeslimTarihi(),
                    teslim.getAciklama(),
                    teslim.getMiktar(),
                    EnumOrtStokHareketTur.HASTA_ICIN_KULLANIM,
                    teslim.getId(),
                    EnumGirisCikis.CIKIS,
                    EnumOrtStokHareketTablo.ORTBASVURU_MALZEME_TESLIMI,
                    teslim.isAktif());

            stokHareket = stokHareketService.refreshEdit(stokHareket);
            if (ortStok.getOrtStokHareketList() == null) ortStok.setOrtStokHareketList(new ArrayList<>());
            if (!ortStok.getOrtStokHareketList().contains(stokHareket)) {
                ortStok.getOrtStokHareketList().add(stokHareket);
            }
            stokService.setStokMiktar(ortStok);
        }

        edit(basvuru);
    }
}
