package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkBireyselGorusme;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHasta;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHastaTedaviSekli;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkTalep;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.25.0
 * @since 30.10.2026 09:51
 */
@Stateless
public class PkDashboardService implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 2587504517540515153L;

    private static final String DASHBOARD_PERMISSION = "psikiyatri_klinigi_dashboard:goster";

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public void checkDashboardPermission() {
        SyKullanici kullanici = Util.getSyKullanici();
        if (kullanici == null) {
            throw new SecurityException("Oturum bulunamadı.");
        }

        Long rolYetkiSayisi = em.createQuery(
                        "SELECT COUNT(ry) FROM SyRolYetki ry " +
                                "WHERE ry.aktif = true " +
                                "AND ry.syYetki.yetki = :yetki " +
                                "AND ry.syRol IN (SELECT kr.syRol FROM SyKullaniciRol kr WHERE kr.syKullanici = :kullanici AND kr.aktif = true)",
                        Long.class)
                .setParameter("yetki", DASHBOARD_PERMISSION)
                .setParameter("kullanici", kullanici)
                .getSingleResult();

        if (rolYetkiSayisi != null && rolYetkiSayisi > 0) {
            return;
        }

        Long dogrudanYetkiSayisi = em.createQuery(
                        "SELECT COUNT(ky) FROM SyKullaniciYetki ky " +
                                "WHERE ky.aktif = true " +
                                "AND ky.syKullanici = :kullanici " +
                                "AND ky.syYetki.yetki = :yetki",
                        Long.class)
                .setParameter("kullanici", kullanici)
                .setParameter("yetki", DASHBOARD_PERMISSION)
                .getSingleResult();

        if (dogrudanYetkiSayisi == null || dogrudanYetkiSayisi == 0) {
            throw new SecurityException("Psikiyatri Kliniği dashboard görüntüleme yetkiniz bulunmamaktadır.");
        }
    }

    public long toplamDanisanSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(p) FROM PkHasta p WHERE p.aktif = true", Long.class)
                .getSingleResult();
    }

    public long aktifDanisanSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT COUNT(t) FROM PkHastaTedaviSekli t " +
                                "WHERE t.aktif = true AND t.taburcuTarihi IS NULL",
                        Long.class)
                .getSingleResult();
    }

    public long bugunYeniBasvuruSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(p) FROM PkHasta p " +
                                "WHERE p.aktif = true " +
                                "AND p.basvuruTarihi IS NOT NULL " +
                                "AND p.basvuruTarihi >= :baslangic AND p.basvuruTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long buAyYeniBasvuruSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(p) FROM PkHasta p " +
                                "WHERE p.aktif = true " +
                                "AND p.basvuruTarihi IS NOT NULL " +
                                "AND p.basvuruTarihi >= :baslangic AND p.basvuruTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long bugunGorusmeSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(g) FROM PkBireyselGorusme g " +
                                "WHERE g.aktif = true " +
                                "AND g.tarih IS NOT NULL " +
                                "AND g.tarih >= :baslangic AND g.tarih < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long buAyGorusmeSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(g) FROM PkBireyselGorusme g " +
                                "WHERE g.aktif = true " +
                                "AND g.tarih IS NOT NULL " +
                                "AND g.tarih >= :baslangic AND g.tarih < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long bekleyenTalepSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT COUNT(t) FROM PkTalep t " +
                                "WHERE t.aktif = true AND t.durum = tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu.BEKLIYOR",
                        Long.class)
                .getSingleResult();
    }

    public long buAyTaburcuSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(t) FROM PkHastaTedaviSekli t " +
                                "WHERE t.aktif = true " +
                                "AND t.taburcuTarihi IS NOT NULL " +
                                "AND t.taburcuTarihi >= :baslangic AND t.taburcuTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long yatiliDanisanSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT COUNT(t) FROM PkHastaTedaviSekli t " +
                                "WHERE t.aktif = true AND t.taburcuTarihi IS NULL " +
                                "AND t.tedaviSekli = tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkTedaviSekli.YATILI",
                        Long.class)
                .getSingleResult();
    }

    public long ayaktanDanisanSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT COUNT(t) FROM PkHastaTedaviSekli t " +
                                "WHERE t.aktif = true AND t.taburcuTarihi IS NULL " +
                                "AND t.tedaviSekli = tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkTedaviSekli.AYAKTAN",
                        Long.class)
                .getSingleResult();
    }

    public long verilenHizmetSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(v) FROM PkVerilenHizmet v WHERE v.aktif = true", Long.class)
                .getSingleResult();
    }

    public long hemsireGozlemFormuSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(h) FROM tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHemsireGozlemFormu h WHERE h.aktif = true", Long.class)
                .getSingleResult();
    }

    public long revirAnemnezSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(r) FROM PkRevirAnemnez r WHERE r.aktif = true", Long.class)
                .getSingleResult();
    }

    public long naranonSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(n) FROM tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkNaranon n WHERE n.aktif = true", Long.class)
                .getSingleResult();
    }

    public long aktifPersonelSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(p) FROM PkPersonel p WHERE p.aktif = true", Long.class)
                .getSingleResult();
    }

    public long izinliPersonelSayisi() {
        checkDashboardPermission();
        LocalDate today = LocalDate.now();
        return em.createQuery(
                        "SELECT COUNT(i) FROM tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkIzin i " +
                                "WHERE i.aktif = true " +
                                "AND i.izinBaslangic IS NOT NULL AND i.izinBitis IS NOT NULL " +
                                "AND i.izinBaslangic <= :today AND i.izinBitis >= :today",
                        Long.class)
                .setParameter("today", today)
                .getSingleResult();
    }

    public long kontrolZamaniYaklasanDanisanSayisi() {
        checkDashboardPermission();
        // Varsayım: son görüşmesi 30 günden eski olan aktif danışanlar.
        LocalDateTime esik = LocalDateTime.now().minusDays(30);
        return em.createQuery(
                        "SELECT COUNT(DISTINCT g.pkAile) FROM PkBireyselGorusme g " +
                                "WHERE g.aktif = true AND g.tarih IS NOT NULL AND g.tarih < :esik",
                        Long.class)
                .setParameter("esik", esik)
                .getSingleResult();
    }

    public List<PkHasta> sonEklenenDanisanlar() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT p FROM PkHasta p WHERE p.aktif = true ORDER BY p.eklemeTarihi DESC",
                        PkHasta.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<PkBireyselGorusme> sonGorusmeler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT g FROM PkBireyselGorusme g WHERE g.aktif = true ORDER BY g.tarih DESC",
                        PkBireyselGorusme.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<PkTalep> bekleyenTalepler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t FROM PkTalep t WHERE t.aktif = true " +
                                "AND t.durum = tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu.BEKLIYOR " +
                                "ORDER BY t.tarih DESC",
                        PkTalep.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<PkHastaTedaviSekli> sonTaburcuIslemleri() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t FROM PkHastaTedaviSekli t " +
                                "WHERE t.aktif = true AND t.taburcuTarihi IS NOT NULL " +
                                "ORDER BY t.taburcuTarihi DESC",
                        PkHastaTedaviSekli.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Object[]> aylikBasvuruTrendi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().minusMonths(11).withDayOfMonth(1).atStartOfDay();
        return em.createQuery(
                        "SELECT FUNCTION('YEAR', p.basvuruTarihi), FUNCTION('MONTH', p.basvuruTarihi), COUNT(p) " +
                                "FROM PkHasta p " +
                                "WHERE p.aktif = true AND p.basvuruTarihi IS NOT NULL AND p.basvuruTarihi >= :baslangic " +
                                "GROUP BY FUNCTION('YEAR', p.basvuruTarihi), FUNCTION('MONTH', p.basvuruTarihi) " +
                                "ORDER BY FUNCTION('YEAR', p.basvuruTarihi), FUNCTION('MONTH', p.basvuruTarihi)",
                        Object[].class)
                .setParameter("baslangic", baslangic)
                .getResultList();
    }

    public List<Object[]> hizmetTuruDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT h.pkHizmetTur.tanim, COUNT(h) FROM PkVerilenHizmet h " +
                                "WHERE h.aktif = true " +
                                "GROUP BY h.pkHizmetTur.tanim " +
                                "ORDER BY COUNT(h) DESC",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> tedaviSekliDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t.tedaviSekli, COUNT(t) FROM PkHastaTedaviSekli t " +
                                "WHERE t.aktif = true AND t.taburcuTarihi IS NULL " +
                                "GROUP BY t.tedaviSekli",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> personelIsYuku() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT g.gorusmeYapanPkPersonel.gnlPersonel.gnlKisi.ad,g.gorusmeYapanPkPersonel.gnlPersonel.gnlKisi.soyad, COUNT(g) " +
                                "FROM PkBireyselGorusme g " +
                                "WHERE g.aktif = true AND g.gorusmeYapanPkPersonel IS NOT NULL " +
                                "GROUP BY g.gorusmeYapanPkPersonel.gnlPersonel.gnlKisi.ad,g.gorusmeYapanPkPersonel.gnlPersonel.gnlKisi.soyad " +
                                "ORDER BY COUNT(g) DESC",
                        Object[].class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Object[]> taburcuNedeniDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t.pkTaburcuNedeni.tanim, COUNT(t) FROM PkHastaTedaviSekli t " +
                                "WHERE t.aktif = true AND t.taburcuTarihi IS NOT NULL AND t.pkTaburcuNedeni IS NOT NULL " +
                                "GROUP BY t.pkTaburcuNedeni.tanim " +
                                "ORDER BY COUNT(t) DESC",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> cinsiyetDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k.gnlKisi.cinsiyet, COUNT(k) " +
                                "FROM PkHasta k " +
                                "WHERE k.aktif = true AND k.gnlKisi IS NOT NULL AND k.gnlKisi.cinsiyet IS NOT NULL " +
                                "GROUP BY k.gnlKisi.cinsiyet " +
                                "ORDER BY COUNT(k) DESC",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> yasDagilimi() {
        checkDashboardPermission();
        List<LocalDate> dogumTarihleri = em.createQuery(
                        "SELECT k.gnlKisi.dogumTarihi FROM PkHasta k " +
                                "WHERE k.aktif = true AND k.gnlKisi IS NOT NULL AND k.gnlKisi.dogumTarihi IS NOT NULL",
                        LocalDate.class)
                .getResultList();

        Map<String, Long> gruplar = new LinkedHashMap<>();
        gruplar.put("0-17", 0L);
        gruplar.put("18-29", 0L);
        gruplar.put("30-44", 0L);
        gruplar.put("45-59", 0L);
        gruplar.put("60-74", 0L);
        gruplar.put("75+", 0L);

        LocalDate today = LocalDate.now();
        for (LocalDate dogumTarihi : dogumTarihleri) {
            int yas = Period.between(dogumTarihi, today).getYears();
            String grup;
            if (yas < 18) {
                grup = "0-17";
            } else if (yas < 30) {
                grup = "18-29";
            } else if (yas < 45) {
                grup = "30-44";
            } else if (yas < 60) {
                grup = "45-59";
            } else if (yas < 75) {
                grup = "60-74";
            } else {
                grup = "75+";
            }
            gruplar.put(grup, gruplar.get(grup) + 1);
        }

        List<Object[]> sonuc = new ArrayList<>();
        for (Map.Entry<String, Long> entry : gruplar.entrySet()) {
            sonuc.add(new Object[]{entry.getKey(), entry.getValue()});
        }
        return sonuc;
    }

    public List<Object[]> ilceDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k.gnlKisi.gnlIlce.tanim, COUNT(k) " +
                                "FROM PkHasta k " +
                                "WHERE k.aktif = true " +
                                "AND k.gnlKisi IS NOT NULL " +
                                "AND k.gnlKisi.gnlIl IS NOT NULL AND k.gnlKisi.gnlIl.id = 27 " +
                                "AND k.gnlKisi.gnlIlce IS NOT NULL " +
                                "AND k.gnlKisi.gnlMahalle IS NOT NULL " +
                                "GROUP BY k.gnlKisi.gnlIlce.tanim " +
                                "ORDER BY COUNT(k) DESC",
                        Object[].class)
                .getResultList();
    }


    // Yardımcı: boş liste döndürmek için
    public List<Object[]> bosListe() {
        return new ArrayList<>();
    }
}