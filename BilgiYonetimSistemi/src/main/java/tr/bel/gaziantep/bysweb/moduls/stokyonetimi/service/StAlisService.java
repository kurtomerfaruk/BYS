package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStIslemLogTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.*;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 16:10
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StAlisService extends AbstractService<StAlis> {

    @Serial
    private static final long serialVersionUID = 6760274692488802540L;

    @Inject
    private StStokHareketService hareketService;
    @Inject
    private StIslemLogService stIslemLogService;

    public StAlisService() {
        super(StAlis.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StAlis alisOlustur(StAlis alis, SyKullanici kullanici) {
        validate(alis);
        if (alis.getTarih() == null) {
            alis.setTarih(LocalDateTime.now());
        }
        resolveRefs(alis);
        if (alis.getKalemler() == null) {
            alis.setKalemler(new java.util.ArrayList<>());
        }
        for (StAlisKalem k : alis.getKalemler()) {
            k.setStAlis(alis);
        }
        create(alis);
        flush();

        // hareketler
        for (StAlisKalem k : alis.getKalemler()) {
            StStokHareket hareket = new StStokHareket();
            hareket.setStStok(k.getStStok());
            hareket.setStDepo(alis.getStDepo());
            hareket.setStDepoLokasyon(alis.getStDepoLokasyon());
            hareket.setHareketTuru(EnumStHareketTuru.SATIN_ALMA);
            hareket.setYon(EnumGirisCikis.GIRIS);
            hareket.setMiktar(k.getMiktar());
            hareket.setBirimMaliyet(k.getBirimMaliyet());
            hareket.setBelgeTuru(alis.getBelgeTuru().getDisplayValue());
            hareket.setBelgeNo(alis.getBelgeNo());
            hareket.setIslemTarihi(alis.getTarih());
            hareket.setAciklama(k.getAciklama());
            hareket.setEkleyen(kullanici);
            hareketService.hareketEkle(hareket);
        }

        stIslemLogService.log(EnumStIslemLogTuru.HAREKET, kullanici, alis.getStDepo(),
                "StAlis", alis.getBelgeNo(),
                "Alış girişi: " + alis.getBelgeTuru().getDisplayValue() + " / " + alis.getKalemler().size() + " kalem");

        return alis;
    }

    private void validate(StAlis alis) {
        if (alis == null) {
            throw new IllegalArgumentException("Alış bilgisi boş");
        }
        if (alis.getStDepo() == null) {
            throw new IllegalArgumentException("Alış için hedef depo seçin");
        }
        if (alis.getKalemler() == null || alis.getKalemler().isEmpty()) {
            throw new IllegalArgumentException("En az bir alış kalemi eklenmelidir.");
        }
        for (StAlisKalem k : alis.getKalemler()) {
            if (k.getStStok() == null) {
                throw new IllegalArgumentException("Alış kalemi için stok seçin");
            }
            if (k.getMiktar() == null || k.getMiktar().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Miktar sıfırdan büyük olmalıdır.");
            }
        }
    }

    private void resolveRefs(StAlis alis) {
        if (alis.getStDepo() != null && alis.getStDepo().getId() != null) {
            alis.setStDepo(em.find(StDepo.class, alis.getStDepo().getId()));
        }
        if (alis.getStDepoLokasyon() != null && alis.getStDepoLokasyon().getId() != null) {
            alis.setStDepoLokasyon(em.find(StDepoLokasyon.class, alis.getStDepoLokasyon().getId()));
        }
        if (alis.getStTedarikci() != null && alis.getStTedarikci().getId() != null) {
            alis.setStTedarikci(em.find(StTedarikci.class, alis.getStTedarikci().getId()));
        }
        if (alis.getKalemler() != null) {
            for (StAlisKalem k : alis.getKalemler()) {
                if (k.getStStok() != null && k.getStStok().getId() != null) {
                    k.setStStok(em.find(StStok.class, k.getStStok().getId()));
                }
            }
        }
    }
}
