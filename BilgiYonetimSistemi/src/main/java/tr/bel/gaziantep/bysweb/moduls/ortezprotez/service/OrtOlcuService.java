package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruHareketDurumu;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtHasta;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcu;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcuDeger;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtRandevu;

import java.io.Serial;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 26.09.2025 09:21
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrtOlcuService extends AbstractService<OrtOlcu> {

    @Serial
    private static final long serialVersionUID = -6216224428824408829L;

    @Inject
    private OrtBasvuruHareketService basvuruHareketService;

    public OrtOlcuService() {
        super(OrtOlcu.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(OrtOlcu ortOlcu, Map<Integer, List<OrtOlcuDeger>> olcuMap) {
        ortOlcu = refreshEdit(ortOlcu);
        for (Map.Entry<Integer, List<OrtOlcuDeger>> entry : olcuMap.entrySet()) {
            for (OrtOlcuDeger d : entry.getValue()) {
                d.setOrtOlcu(ortOlcu);
                getEntityManager().merge(d);
            }
        }
        addHistory(ortOlcu);
    }

    private void addHistory(OrtOlcu ortOlcu) {
        ortOlcu.getOrtBasvuru().setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.OLCU_SONRASI_RANDEVU_VERILDI);
        basvuruHareketService.addHistory(ortOlcu.getOrtBasvuru());
        getEntityManager().merge(ortOlcu.getOrtBasvuru());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void approve(OrtOlcu ortOlcu) {
        edit(ortOlcu);
        OrtHasta hasta = getEntityManager().find(OrtHasta.class, ortOlcu.getOrtBasvuru().getOrtHasta().getId());
        OrtRandevu randevu = OrtRandevu.builder()
                .randevuTarihi(ortOlcu.getRandevuTarihi())
                .ortHasta(hasta)
                .konu("Ölçü alındı.")
                .aciklama("Ölçü alındıktan sonra uygun olmadığından dolayı yeni randevu verildi.")
                .build();
        getEntityManager().persist(randevu);

        addHistory(ortOlcu);
    }
}
