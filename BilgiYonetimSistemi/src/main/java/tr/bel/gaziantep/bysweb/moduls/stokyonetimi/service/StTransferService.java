package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.primefaces.model.FilterMeta;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyKullaniciTuru;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStIslemLogTuru;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStTransferDurum;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    @Inject
    private StDepoYetkiService depoYetkiService;

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

    public void sevkEt(StTransfer transfer, SyKullanici kullanici) {
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

            createTransferHareket(managed, kalem, kullanici,
                    EnumStHareketTuru.TRANSFER_CIKIS, EnumGirisCikis.CIKIS,
                    managed.getKaynakDepo(), kalan,
                    "Transfer sevk edildi: " + managed.getTransferNo());
        }
        managed.setDurum(EnumStTransferDurum.SEVK_EDILDI);
        managed.setSevkTarihi(LocalDateTime.now());
        managed.setSevkIslemiYapanSyKullanici(kullanici);
        managed.setSevkEdenKisi(transfer.getSevkEdenKisi());
        managed.setSevkAciklama(transfer.getSevkAciklama());
        stIslemLogService.log(EnumStIslemLogTuru.TRANSFER, kullanici, managed.getKaynakDepo(),
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
        checkHedefDepoYetkisi(managed, teslimAlan);
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
            createTransferHareket(managed, kalem, teslimAlan,
                    EnumStHareketTuru.TRANSFER_GIRIS, EnumGirisCikis.GIRIS,
                    managed.getHedefDepo(), kalan,
                    "Transfer teslim alındı: " + managed.getTransferNo());
        }
        managed.setDurum(EnumStTransferDurum.TAMAMLANDI);
        managed.setTeslimTarihi(LocalDateTime.now());
        managed.setTeslimAlanSyKullanici(teslimAlan);
        managed.setTeslimAlanKisi(transfer.getTeslimAlanKisi());
        managed.setTeslimAciklama(transfer.getTeslimAciklama());
        stIslemLogService.log(EnumStIslemLogTuru.TRANSFER, teslimAlan, managed.getHedefDepo(),
                "StTransfer", managed.getTransferNo(), "Teslim alındı ve onaylandı");
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

    private void checkHedefDepoYetkisi(StTransfer transfer, SyKullanici kullanici) {
        if (kullanici == null) {
            throw new IllegalArgumentException("Teslim işlemi için kullanıcı bilgisi bulunamadı.");
        }
        if (kullanici.getKullaniciTuru() == EnumSyKullaniciTuru.SISTEM_YONETICISI) {
            return;
        }
        if (transfer.getHedefDepo() == null || transfer.getHedefDepo().getId() == null) {
            throw new IllegalArgumentException("Hedef depo bilgisi bulunamadı.");
        }
        StDepo hedefDepo = em.find(StDepo.class, transfer.getHedefDepo().getId());
        if (depoYetkiService.findByKullaniciByDepo(kullanici, hedefDepo) == null) {
            throw new IllegalArgumentException("Bu transferi yalnızca hedef depo yetkilisi teslim alıp onaylayabilir.");
        }
    }

    private void createTransferHareket(StTransfer transfer, StTransferKalem kalem, SyKullanici kullanici,
                                       EnumStHareketTuru hareketTuru, EnumGirisCikis yon,
                                       StDepo depo, BigDecimal miktar, String aciklama) {
        StStokHareket hareket = new StStokHareket();
        hareket.setStStok(kalem.getStStok());
        hareket.setStDepo(depo);
        hareket.setHareketTuru(hareketTuru);
        hareket.setYon(yon);
        hareket.setMiktar(miktar);
        hareket.setIslemTarihi(LocalDateTime.now());
        hareket.setAciklama(aciklama);
//        hareket.setEkleyenSyKullanici(kullanici);
        em.persist(hareket);
        em.flush();
        hareket.setHareketNo("STH-" + String.format("%08d", hareket.getId()));
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

    /**
     * Transfer listesi yalnızca kullanıcının yetkili olduğu depoları gösterir.
     * Kaynak veya hedef depo yetkisi olan kullanıcı transferi görebilir.
     */
    private List<Integer> getYetkiliDepoIds() {
        SyKullanici kullanici = Util.getSyKullanici();
        if (kullanici == null) {
            return List.of();
        }
        if (kullanici.getKullaniciTuru() == EnumSyKullaniciTuru.SISTEM_YONETICISI) {
            return null; // null = tüm depolar yetkili (filtre uygulanmaz)
        }
        return depoYetkiService.findByKullanici(kullanici).stream()
                .map(StDepoYetki::getStDepo)
                .filter(Objects::nonNull)
                .map(StDepo::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private Predicate yetkiliDepoPredicate(CriteriaBuilder cb, Root<StTransfer> root) {
        List<Integer> depoIds = getYetkiliDepoIds();
        if (depoIds == null) {
            return cb.conjunction();
        }
        if (depoIds.isEmpty()) {
            return cb.disjunction();
        }
        return cb.or(
                root.get("kaynakDepo").get("id").in(depoIds),
                root.get("hedefDepo").get("id").in(depoIds)
        );
    }

    @Override
    protected Predicate getFilterCondition(CriteriaBuilder cb, Root<StTransfer> root, Map<String, FilterMeta> filters, Map<String, Join<?, ?>> joins) {
        Predicate filterCondition = super.getFilterCondition(cb, root, filters == null ? Map.of() : filters, joins);
        return cb.or(filterCondition, yetkiliDepoPredicate(cb, root));
    }
}