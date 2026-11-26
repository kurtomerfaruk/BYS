package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtMalzemeDurumu;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtMalzemeTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtMalzemeTalepDetay;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokBakiyeService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokHareketService;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Ortez-Protez malzeme talebi ve depo onay akışı.
 *
 * <p>Stok ile tam entegrasyon: kalemler StStok/StDepo referansı taşır; onayda
 * StStokBakiye.rezerveMiktar artırılır, fiili çıkışta StStokHareket (URUN_CIKISI)
 * yazılır. Tüm işlemler tek transaction'da ve kilitli bakiye üzerinde yapılır.</p>
 *
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.11.2026
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrtMalzemeTalepService extends AbstractService<OrtMalzemeTalep> {

    @Serial
    private static final long serialVersionUID = -1304811183724913035L;

    @Inject
    private StStokBakiyeService bakiyeService;
    @Inject
    private StStokHareketService stokHareketService;

    public OrtMalzemeTalepService() {
        super(OrtMalzemeTalep.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<OrtMalzemeTalep> findByTalep(OrtTalep talep) {
        if (talep == null || talep.getId() == null) {
            return List.of();
        }
        return em.createQuery(
                        "SELECT m FROM OrtMalzemeTalep m WHERE m.aktif=true AND m.ortTalep.id=:talepId ORDER BY m.talepTarihi DESC",
                        OrtMalzemeTalep.class)
                .setParameter("talepId", talep.getId())
                .getResultList();
    }

    /**
     * Teknikerin malzeme talebini oluşturur.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public OrtMalzemeTalep createRequest(OrtTalep talep, String aciklama, List<OrtMalzemeTalepDetay> detaylar) {
        if (talep == null || talep.getId() == null) {
            throw new IllegalArgumentException("Malzeme talebi oluşturulacak talep bulunamadı.");
        }
        if (detaylar == null || detaylar.isEmpty()) {
            throw new IllegalArgumentException("Malzeme talebine en az bir kalem eklenmelidir.");
        }
        for (OrtMalzemeTalepDetay d : detaylar) {
            if (d.getStStok() == null || d.getStDepo() == null) {
                throw new IllegalArgumentException("Her malzeme kalemi için stok ve depo seçilmelidir.");
            }
            if (d.getIstenenMiktar() == null || d.getIstenenMiktar().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Malzeme miktarı sıfırdan büyük olmalıdır.");
            }
        }

        OrtMalzemeTalep malzemeTalep = OrtMalzemeTalep.builder()
                .ortTalep(talep)
                .talepEdenOrtPersonel(talep.getTeknikerOrtPersonel())
                .talepTarihi(LocalDateTime.now())
                .durum(EnumOrtMalzemeDurumu.BEKLIYOR)
                .aciklama(aciklama)
                .build();
        create(malzemeTalep);
        flush();

        for (OrtMalzemeTalepDetay d : detaylar) {
            d.setOrtMalzemeTalep(malzemeTalep);
            em.persist(d);
        }
        flush();
        return malzemeTalep;
    }

    /**
     * Depo yöneticisi onayı: her kalem için müsait stok kontrolü yapılır ve rezerve edilir.
     * Yetki kontrolü servis katmanında yapılır.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void approve(Integer malzemeTalepId, SyKullanici kullanici, String aciklama) {
        if (!hasPermission(kullanici, "orttalep:depo_onay")) {
            throw new IllegalArgumentException("Malzeme talebini onaylama yetkiniz bulunmuyor.");
        }
        OrtMalzemeTalep mt = em.find(OrtMalzemeTalep.class, malzemeTalepId);
        if (mt == null) {
            throw new IllegalArgumentException("Malzeme talebi bulunamadı.");
        }
        if (mt.getDurum() != EnumOrtMalzemeDurumu.BEKLIYOR) {
            throw new IllegalArgumentException("Sadece bekleyen malzeme talepleri onaylanabilir.");
        }

        List<OrtMalzemeTalepDetay> detaylar = findDetails(mt);
        if (detaylar.isEmpty()) {
            throw new IllegalArgumentException("Onaylanacak malzeme kalemi bulunamadı.");
        }

        for (OrtMalzemeTalepDetay d : detaylar) {
            BigDecimal miktar = d.getOnaylananMiktar() != null ? d.getOnaylananMiktar() : d.getIstenenMiktar();
            if (miktar == null || miktar.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Onaylanan miktar sıfırdan büyük olmalıdır.");
            }
            bakiyeService.reserve(d.getStStok(), d.getStDepo(), null, miktar);
            d.setOnaylananMiktar(miktar);
        }

        mt.setDurum(EnumOrtMalzemeDurumu.ONAYLANDI);
        mt.setOnaylayan(kullanici);
        mt.setOnayTarihi(LocalDateTime.now());
        if (aciklama != null && !aciklama.isBlank()) {
            mt.setAciklama(aciklama);
        }
        edit(mt);
        flush();
    }

    /**
     * Depo yöneticisi reddi: rezerve edilmiş bir miktar varsa geri alınır.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void reject(Integer malzemeTalepId, SyKullanici kullanici, String gerekce) {
        if (!hasPermission(kullanici, "orttalep:depo_onay")) {
            throw new IllegalArgumentException("Malzeme talebini reddetme yetkiniz bulunmuyor.");
        }
        if (gerekce == null || gerekce.isBlank()) {
            throw new IllegalArgumentException("Malzeme talebini reddetmek için gerekçe zorunludur.");
        }
        OrtMalzemeTalep mt = em.find(OrtMalzemeTalep.class, malzemeTalepId);
        if (mt == null) {
            throw new IllegalArgumentException("Malzeme talebi bulunamadı.");
        }
        if (mt.getDurum() != EnumOrtMalzemeDurumu.BEKLIYOR) {
            throw new IllegalArgumentException("Sadece bekleyen malzeme talepleri reddedilebilir.");
        }

        if (mt.getDurum() == EnumOrtMalzemeDurumu.ONAYLANDI) {
            for (OrtMalzemeTalepDetay d : findDetails(mt)) {
                bakiyeService.releaseReservation(d.getStStok(), d.getStDepo(), null, d.getOnaylananMiktar());
            }
        }

        mt.setDurum(EnumOrtMalzemeDurumu.REDDEDILDI);
        mt.setOnaylayan(kullanici);
        mt.setOnayTarihi(LocalDateTime.now());
        mt.setRedGerekcesi(gerekce);
        edit(mt);
        flush();
    }

    /**
     * Teslimat sırasında onaylanan kalemler için fiili stok çıkışı yapar.
     * Her kalem için StStokHareket (URUN_CIKISI / CIKIS) oluşturur ve rezervi düşürür.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createStockIssue(Integer malzemeTalepId) {
        OrtMalzemeTalep mt = em.find(OrtMalzemeTalep.class, malzemeTalepId);
        if (mt == null || mt.getDurum() != EnumOrtMalzemeDurumu.ONAYLANDI) {
            return;
        }
        for (OrtMalzemeTalepDetay d : findDetails(mt)) {
            if (d.getStStokHareket() != null) {
                continue; // zaten çıkış yapılmış
            }
            BigDecimal miktar = d.getOnaylananMiktar() == null ? d.getIstenenMiktar() : d.getOnaylananMiktar();
            if (miktar == null || miktar.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            StStokHareket hareket = new StStokHareket();
            hareket.setStStok(d.getStStok());
            hareket.setStDepo(d.getStDepo());
            hareket.setHareketTuru(EnumStHareketTuru.URUN_CIKISI);
            hareket.setYon(EnumGirisCikis.CIKIS);
            hareket.setMiktar(miktar);
            hareket.setBelgeTuru("ORTTALEP");
            hareket.setBelgeNo(mt.getOrtTalep() == null ? null : mt.getOrtTalep().getTalepNo());
            hareket.setAciklama("Ortez-Protez teslimat - " + (d.getAciklama() == null ? "" : d.getAciklama()));
            stokHareketService.hareketEkle(hareket);

            bakiyeService.consumeReservation(d.getStStok(), d.getStDepo(), null, miktar);
            d.setStStokHareket(hareket);
            em.merge(d);
        }
        flush();
    }

    public List<OrtMalzemeTalepDetay> findDetails(OrtMalzemeTalep mt) {
        if (mt == null || mt.getId() == null) {
            return List.of();
        }
        return em.createQuery(
                        "SELECT d FROM OrtMalzemeTalepDetay d WHERE d.aktif=true AND d.ortMalzemeTalep.id=:id",
                        OrtMalzemeTalepDetay.class)
                .setParameter("id", mt.getId())
                .getResultList();
    }

    public OrtMalzemeTalep findById(Integer id) {
        return em.find(OrtMalzemeTalep.class, id);
    }

    private boolean hasPermission(SyKullanici kullanici, String yetki) {
        // SyYetkiService dependency'siz kontrol: kullanici yetkileri üzerinden basit sorgu
        if (kullanici == null || kullanici.getId() == null) {
            return false;
        }
        Long rolYetki = em.createQuery(
                        "SELECT COUNT(y) FROM SyYetki y WHERE y.aktif=true AND y.yetki=:yetki AND y.id IN " +
                                "(SELECT ry.syYetki.id FROM SyRolYetki ry WHERE ry.aktif=true AND ry.syRol.id IN " +
                                "(SELECT kr.syRol.id FROM SyKullaniciRol kr WHERE kr.aktif=true AND kr.syKullanici.id=:uid))",
                        Long.class)
                .setParameter("yetki", yetki)
                .setParameter("uid", kullanici.getId())
                .getSingleResult();
        if (rolYetki != null && rolYetki > 0) {
            return true;
        }
        Long kisiYetki = em.createQuery(
                        "SELECT COUNT(y) FROM SyYetki y WHERE y.aktif=true AND y.yetki=:yetki AND y.id IN " +
                                "(SELECT ky.syYetki.id FROM SyKullaniciYetki ky WHERE ky.aktif=true AND ky.syKullanici.id=:uid)",
                        Long.class)
                .setParameter("yetki", yetki)
                .setParameter("uid", kullanici.getId())
                .getSingleResult();
        return kisiYetki != null && kisiYetki > 0;
    }

    /**
     * Mevcut kullanıcı (controller'dan gelen) için yetki kontrolü yardımcısı.
     */
    public boolean canApprove() {
        return hasPermission(Util.getSyKullanici(), "orttalep:depo_onay");
    }
}
