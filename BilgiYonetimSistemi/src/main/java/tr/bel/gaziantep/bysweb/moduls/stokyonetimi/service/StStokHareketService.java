package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyGenelAyar;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyGenelAyarService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBakiye;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
        // negatif stok kontrolu (ayar ile)
        if (hareket.getYon() == EnumGirisCikis.CIKIS) {
            BigDecimal yeniMiktar = bakiye.getMiktar().add(delta);
            if (yeniMiktar.compareTo(BigDecimal.ZERO) < 0) {
                SyGenelAyar ayar = syGenelAyarService.findByTanim("StokNegatifStokIzinVer");
                boolean izinVer = ayar != null && "true".equalsIgnoreCase(ayar.getDeger());
                if (!izinVer) {
                    throw new IllegalArgumentException("Negatif stok oluşturulamaz (Sistem Yöneticiniz ile görüşünüz).");
                }
            }
        }
        bakiye.setMiktar(bakiye.getMiktar().add(delta));

        // maliyet motoru (FIFO/LIFO/Ağırlıklı Ortalama)
        if (hareket.getYon() == EnumGirisCikis.GIRIS) {
            stMaliyetKatmaniService.katmanOlustur(hareket);
        } else {
            stMaliyetKatmaniService.katmanTuket(hareket);
        }


        // islem logu
        stIslemLogService.log(
                tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStIslemLogTuru.HAREKET,
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

    public java.util.List<StStokHareket> findByStok(StStok stStok) {
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
