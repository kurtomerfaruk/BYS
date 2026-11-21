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
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyGenelAyar;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyGenelAyarService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBakiye;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 11:52
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StStokHareketService extends AbstractService<StStokHareket> {

    @Serial
    private static final long serialVersionUID = 1202600117312L;

    @Inject
    private StStokBakiyeService bakiyeService;
    @Inject
    private SyGenelAyarService syGenelAyarService;
    @Inject
    private StIslemLogService stIslemLogService;
    @Inject
    private StMaliyetKatmaniService stMaliyetKatmaniService;

    public StStokHareketService() {
        super(StStokHareket.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Yeni hareketi ve bakiye degisimini tek transactionda yapar.
     */
    public StStokHareket hareketEkle(StStokHareket hareket) {
        validate(hareket);

        if (hareket.getIslemTarihi() == null) {
            hareket.setIslemTarihi(LocalDateTime.now());
        }
        if (hareket.getBirimMaliyet() != null && hareket.getToplamMaliyet() == null) {
            hareket.setToplamMaliyet(hareket.getMiktar().multiply(hareket.getBirimMaliyet()));
        }

        create(hareket);
        flush();
        hareket.setHareketNo("STH-" + String.format("%08d", hareket.getId()));

        StStokBakiye bakiye = bakiyeService.getOrCreate(
                hareket.getStStok(),
                hareket.getStDepo(),
                hareket.getSkt());

        BigDecimal delta = hareket.getYon() == EnumGirisCikis.GIRIS
                ? hareket.getMiktar()
                : hareket.getMiktar().negate();
        checkNegativeStock(hareket.getYon(), bakiye.getMiktar().add(delta));
        bakiye.setMiktar(bakiye.getMiktar().add(delta));

        if (hareket.getYon() == EnumGirisCikis.GIRIS) {
            stMaliyetKatmaniService.katmanOlustur(hareket);
        } else {
            stMaliyetKatmaniService.katmanTuket(hareket);
        }

        stIslemLogService.log(
                EnumStIslemLogTuru.HAREKET,
                hareket.getEkleyen(),
                hareket.getStDepo(),
                "StStokHareket",
                hareket.getHareketNo(),
                "Hareket: " + hareket.getHareketTuru().getDisplayValue()
                        + " / " + hareket.getYon().getDisplayValue()
                        + " / " + hareket.getMiktar());

        return hareket;
    }

    /**
     * Mevcut stok hareketini yerinde gunceller. Bakiye, eski miktarin cikarilip
     * yeni miktarin eklenmesiyle net fark kadar duzeltilir; ayni depo icin tek
     * bakiye satiri uzerinde islem yapilir. Yeni hareket kaydi olusturulmaz.
     */
    public StStokHareket updateStockMovement(StStokHareket mevcut, StStokHareket yeni, SyKullanici kullanici) {
        if (mevcut == null || mevcut.getId() == null) {
            throw new IllegalArgumentException("Guncellenecek stok hareketi bulunamadi.");
        }
        validate(yeni);

        StStokHareket managed = em.find(StStokHareket.class, mevcut.getId());
        if (managed == null) {
            throw new IllegalArgumentException("Guncellenecek stok hareketi bulunamadi.");
        }

        BigDecimal oldQty = managed.getMiktar() == null ? BigDecimal.ZERO : managed.getMiktar();
        BigDecimal newQty = yeni.getMiktar() == null ? BigDecimal.ZERO : yeni.getMiktar();
        BigDecimal oldCost = managed.getBirimMaliyet();
        EnumGirisCikis yon = managed.getYon() == null ? yeni.getYon() : managed.getYon();
        int sign = yon == EnumGirisCikis.GIRIS ? 1 : -1;
        BigDecimal oldDelta = oldQty.multiply(BigDecimal.valueOf(sign));
        BigDecimal newDelta = newQty.multiply(BigDecimal.valueOf(sign));

        StStok oldStok = managed.getStStok();
        StDepo oldDepo = managed.getStDepo();
        LocalDate oldSkt = managed.getSkt();

        StStok newStok = yeni.getStStok() == null || yeni.getStStok().getId() == null
                ? oldStok
                : em.find(StStok.class, yeni.getStStok().getId());
        StDepo newDepo = yeni.getStDepo() == null || yeni.getStDepo().getId() == null
                ? oldDepo
                : em.find(StDepo.class, yeni.getStDepo().getId());
        LocalDate newSkt = yeni.getSkt();

        boolean sameBalance = Objects.equals(
                oldStok == null ? null : oldStok.getId(),
                newStok == null ? null : newStok.getId())
                && Objects.equals(
                oldDepo == null ? null : oldDepo.getId(),
                newDepo == null ? null : newDepo.getId())
                && Objects.equals(oldSkt, newSkt);

        if (sameBalance) {
            StStokBakiye bakiye = bakiyeService.getOrCreate(oldStok, oldDepo, oldSkt);
            BigDecimal finalQty = bakiye.getMiktar().subtract(oldDelta).add(newDelta);
            checkNegativeStock(yon, finalQty);
            bakiye.setMiktar(finalQty);
        } else {
            StStokBakiye oldBalance = bakiyeService.getOrCreate(oldStok, oldDepo, oldSkt);
            oldBalance.setMiktar(oldBalance.getMiktar().subtract(oldDelta));

            StStokBakiye newBalance = bakiyeService.getOrCreate(newStok, newDepo, newSkt);
            BigDecimal finalQty = newBalance.getMiktar().add(newDelta);
            checkNegativeStock(yon, finalQty);
            newBalance.setMiktar(finalQty);
        }

        managed.setStStok(newStok);
        managed.setStDepo(newDepo);
        managed.setSkt(newSkt);
        managed.setMiktar(newQty);
        managed.setBirimMaliyet(yeni.getBirimMaliyet());
        managed.setToplamMaliyet(yeni.getBirimMaliyet() == null ? null : newQty.multiply(yeni.getBirimMaliyet()));
        managed.setBelgeTuru(yeni.getBelgeTuru());
        managed.setBelgeNo(yeni.getBelgeNo());
        managed.setIslemTarihi(yeni.getIslemTarihi() == null ? LocalDateTime.now() : yeni.getIslemTarihi());
        managed.setAciklama(yeni.getAciklama());
        if (kullanici != null) {
            managed.setGuncelleyen(kullanici);
        }

        stMaliyetKatmaniService.updateCostLayer(managed, oldQty, oldCost);
        em.flush();
        stIslemLogService.log(
                EnumStIslemLogTuru.HAREKET,
                kullanici,
                managed.getStDepo(),
                "StStokHareket",
                managed.getHareketNo(),
                "Hareket güncelleme: " + managed.getHareketTuru().getDisplayValue()
                        + " / " + oldQty + " -> " + newQty);
        return managed;
    }

    private void checkNegativeStock(EnumGirisCikis yon, BigDecimal finalQty) {
        if (yon != EnumGirisCikis.CIKIS || finalQty.compareTo(BigDecimal.ZERO) >= 0) {
            return;
        }
        SyGenelAyar ayar = syGenelAyarService.findByTanim("StokNegatifStokIzinVer");
        boolean izinVer = ayar != null && "true".equalsIgnoreCase(ayar.getDeger());
        if (!izinVer) {
            throw new IllegalArgumentException("Negatif stok oluşturulamaz (Sistem Yöneticiniz ile görüşünüz).");
        }
    }

    /**
     * Kalem satiri belgeden cikarildiginda hareketi pasif hale getirir
     * ve bakiyeyi eski miktar kadar duzeltir. Yeni hareket kaydi olusturmaz.
     */
    public void cancelStockMovement(StStokHareket hareket, SyKullanici kullanici) {
        if (hareket == null || hareket.getId() == null) {
            return;
        }
        StStokHareket managed = em.find(StStokHareket.class, hareket.getId());
        if (managed == null || !managed.isAktif()) {
            return;
        }
        BigDecimal qty = managed.getMiktar() == null ? BigDecimal.ZERO : managed.getMiktar();
        int sign = managed.getYon() == EnumGirisCikis.GIRIS ? 1 : -1;
        StStokBakiye bakiye = bakiyeService.getOrCreate(managed.getStStok(), managed.getStDepo(), managed.getSkt());
        bakiye.setMiktar(bakiye.getMiktar().subtract(qty.multiply(BigDecimal.valueOf(sign))));
        managed.setAktif(false);
        if (kullanici != null) {
            managed.setGuncelleyen(kullanici);
        }
        stMaliyetKatmaniService.deactivateCostLayer(managed);
        em.flush();
    }

    /**
     * Belgeye ait aktif hareketleri dondurur.
     */
    public List<StStokHareket> findActiveByPurchaseDocument(String belgeTuru, String belgeNo) {
        if (belgeNo == null || belgeNo.isBlank()) {
            return List.of();
        }
        return em.createQuery(
                        "SELECT h FROM StStokHareket h WHERE h.belgeNo = :belgeNo "
                                + "AND h.aktif = true AND h.tersHareket IS NULL "
                                + "ORDER BY h.id",
                        StStokHareket.class)
                .setParameter("belgeNo", belgeNo.trim())
                .getResultList();
    }

    /**
     * Orijinal hareketin tersini olusturur. Ters kayit yeni bir hareket olarak
     * saklanir; orijinal silinmez/degistirilmez.
     */
    public StStokHareket tersHareketOlustur(StStokHareket orijinal) {
        String mesaj = "Bu hareket için ters kayıt zaten oluşturulmuştur.";
        orijinal = find(orijinal.getId());
        if (orijinal == null || orijinal.getId() == null) {
            throw new IllegalArgumentException(mesaj);
        }
        if (orijinal.getTersHareket() != null) {
            throw new IllegalArgumentException(mesaj);
        }
        Long tersSayisi = em.createQuery(
                        "SELECT COUNT(h) FROM StStokHareket h WHERE h.tersHareket = :orijinal AND h.aktif = true",
                        Long.class)
                .setParameter("orijinal", orijinal)
                .getSingleResult();
        if (tersSayisi != null && tersSayisi > 0) {
            throw new IllegalArgumentException(mesaj);
        }

        StStokHareket ters = new StStokHareket();
        ters.setStStok(orijinal.getStStok());
        ters.setStDepo(orijinal.getStDepo());
        ters.setHareketTuru(orijinal.getHareketTuru());
        ters.setYon(orijinal.getYon() == EnumGirisCikis.GIRIS ? EnumGirisCikis.CIKIS : EnumGirisCikis.GIRIS);
        ters.setMiktar(orijinal.getMiktar());
        ters.setBirimMaliyet(orijinal.getBirimMaliyet());
        ters.setToplamMaliyet(orijinal.getToplamMaliyet());
        ters.setSkt(orijinal.getSkt());
        ters.setBelgeTuru(orijinal.getBelgeTuru());
        ters.setBelgeNo(orijinal.getBelgeNo());
        ters.setIslemTarihi(LocalDateTime.now());
        ters.setTersHareket(orijinal);
        ters.setAciklama("Ters kayıt: " + orijinal.getHareketNo());

        return hareketEkle(ters);
    }

    private void validate(StStokHareket hareket) {
        if (hareket == null || hareket.getStStok() == null) {
            throw new IllegalArgumentException("Stok hareketi için stok seçin");
        }
        if (hareket.getStDepo() == null) {
            throw new IllegalArgumentException("Stok hareketi için depo seçin");
        }
        if (hareket.getHareketTuru() == null) {
            throw new IllegalArgumentException("Stok hareketi için hareket türü seçin");
        }
        if (hareket.getYon() == null) {
            throw new IllegalArgumentException("Stok hareketi için yönü seçin");
        }
        if (hareket.getMiktar() == null) {
            throw new IllegalArgumentException("Stok hareketi için miktar girin");
        }
        if (hareket.getMiktar().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Miktar sıfırdan büyük olmalıdır.");
        }
    }

    /**
     * Hareket turunun varsayilan yonunu dondurur; UI'da yonu otomatik secmek icin kullanilir.
     */
    public EnumGirisCikis varsayilanYon(EnumStHareketTuru hareketTuru) {
        return hareketTuru == null ? null : hareketTuru.getVarsayilanYon();
    }

    public List<StStokHareket> findByStok(StStok stStok) {
        if (stStok == null || stStok.getId() == null) {
            return new java.util.ArrayList<>();
        }
        jakarta.persistence.criteria.CriteriaBuilder cb = em.getCriteriaBuilder();
        jakarta.persistence.criteria.CriteriaQuery<StStokHareket> cq = cb.createQuery(StStokHareket.class);
        jakarta.persistence.criteria.Root<StStokHareket> root = cq.from(StStokHareket.class);
        cq.where(cb.and(cb.equal(root.get("stStok"), stStok), cb.isTrue(root.get("aktif"))));
        cq.orderBy(cb.desc(root.get("islemTarihi")));
        return em.createQuery(cq).getResultList();
    }
}
