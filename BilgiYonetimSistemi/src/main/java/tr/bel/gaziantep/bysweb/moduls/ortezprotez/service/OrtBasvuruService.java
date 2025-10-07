package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruHareketDurumu;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuru;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtRandevu;

import java.io.Serial;
import java.time.LocalDateTime;

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

    @Inject
    private OrtBasvuruHareketService ortBasvuruHareketService;

    public OrtBasvuruService() {
        super(OrtBasvuru.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

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
            ortBasvuruHareketService.addHistory(ortBasvuru, EnumOrtBasvuruHareketDurumu.OLUMLU);
        }

        if (addAppointment) {
            OrtRandevu randevu = OrtRandevu.builder()
                    .randevuTarihi(appointmentDate)
                    .ortHasta(ortBasvuru.getOrtHasta())
                    .konu("Ölçü alımı")
                    .aciklama("Başvuruya istinaden randevu verilmiştir.")
                    .build();
            getEntityManager().persist(randevu);

            ortBasvuruHareketService.addHistory(ortBasvuru, EnumOrtBasvuruHareketDurumu.OLCU_ICIN_RANDEVU_VERILDI);
            ortBasvuru.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.OLCU_ICIN_RANDEVU_VERILDI);
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

    public void saveDurum(OrtBasvuru ortBasvuru,EnumOrtBasvuruHareketDurumu durum) {
        ortBasvuruHareketService.addHistory(ortBasvuru, durum);
        ortBasvuru.setBasvuruHareketDurumu(durum);
        edit(ortBasvuru);
    }
}
