package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBelgeTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtMalzemeTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTeslimat;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 26.11.2026 08:35
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrtTeslimatService extends AbstractService<OrtTeslimat> {

    @Serial
    private static final long serialVersionUID = 8158116101832565699L;

    @Inject
    private OrtTalepBelgeService ortTalepBelgeService;
    @Inject
    private OrtMalzemeTalepService ortMalzemeTalepService;

    public OrtTeslimatService() {
        super(OrtTeslimat.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<OrtTeslimat> findByTalep(OrtTalep talep) {
        if (talep == null || talep.getId() == null) {
            return List.of();
        }
        return em.createQuery(
                        "SELECT t FROM OrtTeslimat t WHERE t.aktif=true AND t.ortTalep.id=:talepId ORDER BY t.teslimTarihi DESC",
                        OrtTeslimat.class)
                .setParameter("talepId", talep.getId())
                .getResultList();
    }

    public boolean existsByTalep(OrtTalep talep) {
        return !findByTalep(talep).isEmpty();
    }

    /**
     * Teslimat kaydını oluşturur ve onaylı malzeme talepleri için fiili stok çıkışını
     * (StStokHareket / URUN_CIKISI) aynı transaction'da yapar.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public OrtTeslimat createDelivery(OrtTalep talep, String aciklama) {
        if (talep == null || talep.getId() == null) {
            throw new IllegalArgumentException("Teslimat yapılacak talep bulunamadı.");
        }
        if (!ortTalepBelgeService.existsByType(talep, EnumOrtBelgeTuru.TESLIM_TUTANAGI)) {
            throw new IllegalArgumentException("Teslimat öncesinde imzalı teslim tutanağı sisteme yüklenmelidir.");
        }

        GnlKisi teslimAlan = talep.getOrtKisi() == null ? null : talep.getOrtKisi().getGnlKisi();
        OrtTeslimat teslimat = OrtTeslimat.builder()
                .ortTalep(talep)
                .teslimTarihi(LocalDateTime.now())
                .teslimEden(talep.getTeknikerOrtPersonel())
                .teslimAlan(teslimAlan)
                .kaydeden(Util.getSyKullanici())
                .aciklama(aciklama)
                .build();
        create(teslimat);
        flush();

        // Onaylı malzeme taleplerinin rezervini fiili çıkışa çevir.
        List<OrtMalzemeTalep> talepler = ortMalzemeTalepService.findByTalep(talep);
        for (OrtMalzemeTalep mt : talepler) {
            ortMalzemeTalepService.createStockIssue(mt.getId());
        }
        flush();
        return teslimat;
    }
}
