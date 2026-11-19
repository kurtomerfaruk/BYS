package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStIslemLogTuru;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStTransferDurum;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBakiye;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StTransfer;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StTransferKalem;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 15:58
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StTransferService extends AbstractService<StTransfer> {

    @Serial
    private static final long serialVersionUID = -52785271279729807L;

    @Inject
    private StStokBakiyeService bakiyeService;
    @Inject
    private StIslemLogService stIslemLogService;

    public StTransferService() {
        super(StTransfer.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StTransfer transferOlustur(StTransfer transfer) {
        validate(transfer);
        if (transfer.getTalepTarihi() == null) {
            transfer.setTalepTarihi(LocalDateTime.now());
        }
        transfer.setDurum(EnumStTransferDurum.TASLAK);
        resolveRefs(transfer);
        create(transfer);
        flush();
        transfer.setTransferNo("TRF-" + String.format("%08d", transfer.getId()));
        return transfer;
    }

    public void onayla(StTransfer transfer, SyKullanici onaylayan) {
        if (transfer == null || transfer.getId() == null) {
            throw new IllegalArgumentException("İşlem için bir transfer seçin");
        }
        StTransfer managed = em.find(StTransfer.class, transfer.getId());
        if (managed.getDurum() != EnumStTransferDurum.TASLAK) {
            throw new IllegalArgumentException("Bu transfer onaylanamaz");
        }
        managed.setDurum(EnumStTransferDurum.ONAYLANDI);
        managed.setOnayTarihi(LocalDateTime.now());
        managed.setOnaylayanSyKullanici(onaylayan);
        stIslemLogService.log(EnumStIslemLogTuru.TRANSFER, onaylayan, managed.getKaynakDepo(),
                "StTransfer", managed.getTransferNo(), "Onaylandı");
    }

    public void reddet(StTransfer transfer, SyKullanici onaylayan) {
        if (transfer == null || transfer.getId() == null) {
            throw new IllegalArgumentException("İşlem için bir transfer seçin");
        }
        StTransfer managed = em.find(StTransfer.class, transfer.getId());
        if (managed.getDurum() != EnumStTransferDurum.TASLAK) {
            throw new IllegalArgumentException("Bu transfer reddedilemez");
        }
        managed.setDurum(EnumStTransferDurum.REDDEDILDI);
        managed.setOnayTarihi(LocalDateTime.now());
        managed.setOnaylayanSyKullanici(onaylayan);
        stIslemLogService.log(EnumStIslemLogTuru.TRANSFER, onaylayan, managed.getKaynakDepo(),
                "StTransfer", managed.getTransferNo(), "Reddedildi");
    }

    public void sevkEt(StTransfer transfer) {
        if (transfer == null || transfer.getId() == null) {
            throw new IllegalArgumentException("İşlem için bir transfer seçin");
        }
        StTransfer managed = em.find(StTransfer.class, transfer.getId());
        if (managed.getDurum() != EnumStTransferDurum.ONAYLANDI) {
            throw new IllegalArgumentException("Bu transfer sevkedilemez");
        }
        // bakiye kontrolu ve guncelleme
        for (StTransferKalem kalem : managed.getKalemler()) {
            BigDecimal kalan = kalem.getMiktar().subtract(kalem.getSevkMiktari());
            if (kalan.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            StStokBakiye kaynakBakiye = bakiyeService.getOrCreate(kalem.getStStok(), managed.getKaynakDepo(), null);
            if (kaynakBakiye.getMiktar().compareTo(kalan) < 0) {
                throw new IllegalArgumentException("Kaynak depoda yetersiz bakiye");
            }
            kaynakBakiye.setMiktar(kaynakBakiye.getMiktar().subtract(kalan));

            StStokBakiye hedefBakiye = bakiyeService.getOrCreate(kalem.getStStok(), managed.getHedefDepo(), null);
            hedefBakiye.setTransferdekiMiktar(hedefBakiye.getTransferdekiMiktar().add(kalan));

            kalem.setSevkMiktari(kalem.getMiktar());
        }
        managed.setDurum(EnumStTransferDurum.SEVK_EDILDI);
        managed.setSevkTarihi(LocalDateTime.now());
        stIslemLogService.log(EnumStIslemLogTuru.TRANSFER, null, managed.getKaynakDepo(),
                "StTransfer", managed.getTransferNo(), "Sevk edildi");
    }

    public void teslimEt(StTransfer transfer, SyKullanici teslimAlan) {
        if (transfer == null || transfer.getId() == null) {
            throw new IllegalArgumentException("İşlem için bir transfer seçin");
        }
        StTransfer managed = em.find(StTransfer.class, transfer.getId());
        if (managed.getDurum() != EnumStTransferDurum.SEVK_EDILDI) {
            throw new IllegalArgumentException("Bu transfer teslim edilemez");
        }
        for (StTransferKalem kalem : managed.getKalemler()) {
            BigDecimal kalan = kalem.getSevkMiktari().subtract(kalem.getTeslimMiktari());
            if (kalan.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            StStokBakiye hedefBakiye = bakiyeService.getOrCreate(kalem.getStStok(), managed.getHedefDepo(), null);
            BigDecimal yeniTransferdeki = hedefBakiye.getTransferdekiMiktar().subtract(kalan);
            if (yeniTransferdeki.compareTo(BigDecimal.ZERO) < 0) {
                yeniTransferdeki = BigDecimal.ZERO;
            }
            hedefBakiye.setTransferdekiMiktar(yeniTransferdeki);
            hedefBakiye.setMiktar(hedefBakiye.getMiktar().add(kalan));

            kalem.setTeslimMiktari(kalem.getSevkMiktari());
        }
        managed.setDurum(EnumStTransferDurum.TAMAMLANDI);
        managed.setTeslimTarihi(LocalDateTime.now());
        managed.setTeslimAlanSyKullanici(teslimAlan);
        stIslemLogService.log(EnumStIslemLogTuru.TRANSFER, teslimAlan, managed.getHedefDepo(),
                "StTransfer", managed.getTransferNo(), "Teslim edildi");
    }

    public void iptalEt(StTransfer transfer) {
        if (transfer == null || transfer.getId() == null) {
            throw new IllegalArgumentException("İşlem için bir transfer seçin");
        }
        StTransfer managed = em.find(StTransfer.class, transfer.getId());
        EnumStTransferDurum durum = managed.getDurum();
        if (durum == EnumStTransferDurum.TAMAMLANDI || durum == EnumStTransferDurum.IPTAL
                || durum == EnumStTransferDurum.REDDEDILDI) {
            throw new IllegalArgumentException("Bu transfer iptal edilemez");
        }
        // sevk yapilmis ise bakiyeyi geri al
        if (durum == EnumStTransferDurum.SEVK_EDILDI) {
            for (StTransferKalem kalem : managed.getKalemler()) {
                BigDecimal kalan = kalem.getSevkMiktari().subtract(kalem.getTeslimMiktari());
                if (kalan.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                StStokBakiye kaynakBakiye = bakiyeService.getOrCreate(kalem.getStStok(), managed.getKaynakDepo(), null);
                kaynakBakiye.setMiktar(kaynakBakiye.getMiktar().add(kalan));

                StStokBakiye hedefBakiye = bakiyeService.getOrCreate(kalem.getStStok(), managed.getHedefDepo(), null);
                BigDecimal yeniTransferdeki = hedefBakiye.getTransferdekiMiktar().subtract(kalan);
                if (yeniTransferdeki.compareTo(BigDecimal.ZERO) < 0) {
                    yeniTransferdeki = BigDecimal.ZERO;
                }
                hedefBakiye.setTransferdekiMiktar(yeniTransferdeki);
            }
        }
        managed.setDurum(EnumStTransferDurum.IPTAL);
        stIslemLogService.log(EnumStIslemLogTuru.TRANSFER, null, managed.getKaynakDepo(),
                "StTransfer", managed.getTransferNo(), "İptal edildi");
    }

    private void validate(StTransfer transfer) {
        if (transfer == null) {
            throw new IllegalArgumentException("Transfer bilgisi boş");
        }
        if (transfer.getKaynakDepo() == null) {
            throw new IllegalArgumentException("Kaynak depo seçin");
        }
        if (transfer.getHedefDepo() == null) {
            throw new IllegalArgumentException("Hedef depo seçin");
        }
        if (Objects.equals(transfer.getKaynakDepo().getId(), transfer.getHedefDepo().getId())) {
            throw new IllegalArgumentException("Kaynak ve hedef depo aynı olamaz.");
        }
        if (transfer.getKalemler() == null || transfer.getKalemler().isEmpty()) {
            throw new IllegalArgumentException("En az bir transfer kalemi eklenmelidir.");
        }
        for (StTransferKalem kalem : transfer.getKalemler()) {
            if (kalem.getStStok() == null) {
                throw new IllegalArgumentException("Transfer kalemi için stok seçin");
            }
            if (kalem.getMiktar() == null || kalem.getMiktar().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Miktar sıfırdan büyük olmalıdır.");
            }
        }
    }

    private void resolveRefs(StTransfer transfer) {
        if (transfer.getKaynakDepo() != null && transfer.getKaynakDepo().getId() != null) {
            transfer.setKaynakDepo(em.find(StDepo.class, transfer.getKaynakDepo().getId()));
        }
        if (transfer.getHedefDepo() != null && transfer.getHedefDepo().getId() != null) {
            transfer.setHedefDepo(em.find(StDepo.class, transfer.getHedefDepo().getId()));
        }
    }
}