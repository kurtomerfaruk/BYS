package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStTransferDurum;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 11:41
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class StTransferIslemService {

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Inject
    private StTransferService transferService;
    @Inject
    private StTransferKalemService kalemService;
    @Inject
    private StStokIslemService islemService;
    @Inject
    private StStokMiktarService miktarService;
    @Inject
    private StStokService stokService;
//    @Inject
//    private StKullaniciDepoYetkiService yetkiService;

    public StTransfer talepOlustur(StTransfer transfer, List<StTransferKalem> kalemler) {
        if (transfer == null) throw new IllegalArgumentException("Transfer zorunludur");
        transfer.setDurum(EnumStTransferDurum.ONAY_BEKLIYOR);
        transfer.setTalepTarihi(LocalDateTime.now());
        transferService.create(transfer);
        if (kalemler != null) {
            for (StTransferKalem kalem : kalemler) {
                kalem.setStTransfer(transfer);
                if (kalem.getSevkMiktar() == null) kalem.setSevkMiktar(BigDecimal.ZERO);
                if (kalem.getTeslimMiktar() == null) kalem.setTeslimMiktar(BigDecimal.ZERO);
                kalemService.create(kalem);
            }
        }
        return transfer;
    }

    public void confirm(StTransfer transfer, SyKullanici onaylayan) {
        StTransfer t = em.find(StTransfer.class, transfer.getId(), LockModeType.PESSIMISTIC_WRITE);
        if (t == null) throw new IllegalArgumentException("Transfer bulunamadý");
        t.setOnaylayanSyKullanici(onaylayan);
        transferService.edit(t);
    }

    public void dispatch(StTransfer transfer, SyKullanici sevkEden) {
        StTransfer t = em.find(StTransfer.class, transfer.getId(), LockModeType.PESSIMISTIC_WRITE);
        if (t == null) throw new IllegalArgumentException("Transfer bulunamadý");
        StDepo kaynakDepo = t.getKaynakDepo();
        StDepo hedefDepo = t.getHedefDepo();

//        if (sevkEden != null && !yetkiService.hasYetki(sevkEden, kaynakDepo, tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStYetkiTuru.ISLEM)) {
//            throw new SecurityException("Kaynak depo iþlem yetkisi bulunamadý");
//        }

        for (StTransferKalem kalem : t.getStTransferKalemList()) {
            BigDecimal kalan = kalem.getMiktar().subtract(kalem.getSevkMiktar() == null ? BigDecimal.ZERO : kalem.getSevkMiktar());
            if (kalan.compareTo(BigDecimal.ZERO) <= 0) continue;

            StStokHareket hareket = new StStokHareket();
            hareket.setStStok(stokService.find(kalem.getStStok().getId()));
            hareket.setStDepo(kaynakDepo);
            hareket.setStDepoLokasyon(kalem.getKaynakStDepoLokasyon());
            hareket.setHedefDepo(hedefDepo);
            hareket.setHedefLokasyon(kalem.getHedefStDepoLokasyon());
            hareket.setHareketTuru(EnumStHareketTuru.DEPO_TRANSFERI);
            hareket.setDurum(EnumGirisCikis.CIKIS);
            hareket.setMiktar(kalan);
            hareket.setStBirim(kalem.getStBirim());
            hareket.setIslemTarihi(LocalDateTime.now());
            hareket.setSyKullanici(sevkEden);
            hareket.setStTransfer(t);
            hareket.setBelgeTuru("STTRANSFER");
            hareket.setBelgeId(t.getId());
            hareket.setBelgeNo(t.getTransferNo());
            islemService.createMovement(hareket);

            StStokBakiye hedefBakiye = miktarService.getOrCreateBakiye(hareket.getStStok(), hedefDepo, kalem.getHedefStDepoLokasyon(), null,
                    LockModeType.PESSIMISTIC_WRITE);
            hedefBakiye.setTransferdekiMiktar(hedefBakiye.getTransferdekiMiktar().add(kalan));
            miktarService.save(hedefBakiye);

            kalem.setSevkMiktar(kalem.getMiktar());
            kalemService.edit(kalem);
        }

        t.setSevkEdenSyKullanici(sevkEden);
        t.setSevkTarihi(LocalDateTime.now());
        t.setDurum(EnumStTransferDurum.SEVKEDILDI);
        transferService.edit(t);
    }

    public void pickUp(StTransfer transfer, SyKullanici teslimAlan) {
        StTransfer t = em.find(StTransfer.class, transfer.getId(), LockModeType.PESSIMISTIC_WRITE);
        if (t == null) throw new IllegalArgumentException("Transfer bulunamadý");
        StDepo hedefDepo = t.getHedefDepo();

//        if (teslimAlan != null && !yetkiService.hasYetki(teslimAlan, hedefDepo, tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStYetkiTuru.ISLEM)) {
//            throw new SecurityException("Hedef depo iþlem yetkisi bulunamadý");
//        }

        boolean allDelivered = true;
        for (StTransferKalem kalem : t.getStTransferKalemList()) {
            BigDecimal kalan = (kalem.getSevkMiktar() == null ? BigDecimal.ZERO : kalem.getSevkMiktar())
                    .subtract(kalem.getTeslimMiktar() == null ? BigDecimal.ZERO : kalem.getTeslimMiktar());
            if (kalan.compareTo(BigDecimal.ZERO) <= 0) {
                if (kalem.getTeslimMiktar().compareTo(kalem.getSevkMiktar()) < 0) allDelivered = false;
                continue;
            }
            allDelivered = false;

            StStokHareket hareket = new StStokHareket();
            hareket.setStStok(stokService.find(kalem.getStStok().getId()));
            hareket.setStDepo(hedefDepo);
            hareket.setStDepoLokasyon(kalem.getHedefStDepoLokasyon());
            hareket.setHareketTuru(EnumStHareketTuru.DEPO_TRANSFERI);
            hareket.setDurum(EnumGirisCikis.GIRIS);
            hareket.setMiktar(kalan);
            hareket.setStBirim(kalem.getStBirim());
            hareket.setIslemTarihi(LocalDateTime.now());
            hareket.setSyKullanici(teslimAlan);
            hareket.setStTransfer(t);
            hareket.setBelgeTuru("STTRANSFER");
            hareket.setBelgeId(t.getId());
            hareket.setBelgeNo(t.getTransferNo());
            islemService.createMovement(hareket);

            StStokBakiye hedefBakiye = miktarService.findBakiye(hareket.getStStok(), hedefDepo, kalem.getHedefStDepoLokasyon(), null,
                    LockModeType.PESSIMISTIC_WRITE);
            if (hedefBakiye != null) {
                hedefBakiye.setTransferdekiMiktar(hedefBakiye.getTransferdekiMiktar().subtract(kalan));
                if (hedefBakiye.getTransferdekiMiktar().compareTo(BigDecimal.ZERO) < 0) {
                    hedefBakiye.setTransferdekiMiktar(BigDecimal.ZERO);
                }
                miktarService.save(hedefBakiye);
            }

            kalem.setTeslimMiktar(kalem.getSevkMiktar());
            kalemService.edit(kalem);
        }

        for (StTransferKalem kalem : t.getStTransferKalemList()) {
            BigDecimal sevk = kalem.getSevkMiktar() == null ? BigDecimal.ZERO : kalem.getSevkMiktar();
            BigDecimal teslim = kalem.getTeslimMiktar() == null ? BigDecimal.ZERO : kalem.getTeslimMiktar();
            if (teslim.compareTo(sevk) < 0) allDelivered = false;
        }

        t.setTeslimAlanGnlPersonel(teslimAlan.getGnlPersonel());
        t.setTeslimTarihi(LocalDateTime.now());
        if (allDelivered) t.setDurum(EnumStTransferDurum.TAMAMLANDI);
        else t.setDurum(EnumStTransferDurum.KISMEN_TESLIM);
        transferService.edit(t);
    }

    public void cancel(StTransfer transfer) {
        StTransfer t = em.find(StTransfer.class, transfer.getId(), LockModeType.PESSIMISTIC_WRITE);
        if (t == null) throw new IllegalArgumentException("Transfer bulunamadý");
        if (t.getDurum() == EnumStTransferDurum.SEVKEDILDI || t.getDurum() == EnumStTransferDurum.KISMEN_TESLIM) {
            throw new IllegalStateException("Sevk edilmiþ transfer iptal edilemez");
        }
        t.setDurum(EnumStTransferDurum.IPTAL);
        transferService.edit(t);
    }
}