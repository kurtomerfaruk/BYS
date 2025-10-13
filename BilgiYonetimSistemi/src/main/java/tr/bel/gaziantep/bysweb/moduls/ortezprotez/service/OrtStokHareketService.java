package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtStokHareketTablo;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtStokHareketTur;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStok;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStokHareket;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.10.2025 16:07
 */
@Stateless
public class OrtStokHareketService extends AbstractService<OrtStokHareket> {

    @Serial
    private static final long serialVersionUID = -4203984261432299048L;

    public OrtStokHareketService() {
        super(OrtStokHareket.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


    public OrtStokHareket createHareket(SyKullanici syKullanici,
                                        OrtStok ortStok,
                                        LocalDateTime tarih,
                                        String aciklama,
                                        BigDecimal miktar,
                                        EnumOrtStokHareketTur hareketTur,
                                        Integer islemId,
                                        EnumGirisCikis durum,
                                        EnumOrtStokHareketTablo tablo,
                                        boolean aktif) {
        OrtStokHareket hareket = findByIslemIdByHareketTur(islemId, hareketTur, ortStok);
        if (hareket == null) hareket = new OrtStokHareket();
        if (hareket.getId() == null) {
            hareket.setEkleyen(syKullanici);
            hareket.setEklemeTarihi(LocalDateTime.now());
        } else {
            hareket.setGuncelleyen(syKullanici);
            hareket.setGuncellemeTarihi(LocalDateTime.now());
        }
        hareket.setOrtStok(ortStok);
        hareket.setTarih(tarih);
        hareket.setAciklama(aciklama);
        hareket.setMiktar(miktar);
        hareket.setHareketTur(hareketTur);
        hareket.setIslemId(islemId);
        hareket.setDurum(durum);
        hareket.setTablo(tablo);
        hareket.setAktif(aktif);

        return hareket;
    }

    private OrtStokHareket findByIslemIdByHareketTur(Integer islemId, EnumOrtStokHareketTur hareketTur, OrtStok ortStok) {
        return (OrtStokHareket) getEntityManager().createNamedQuery("OrtStokHareket.findByIslemIdByHareketTur")
                .setParameter("islemId", islemId)
                .setParameter("hareketTur", hareketTur)
                .setParameter("ortStok", ortStok)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }
}
