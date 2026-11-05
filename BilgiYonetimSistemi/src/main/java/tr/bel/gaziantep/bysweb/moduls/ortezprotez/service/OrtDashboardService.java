package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuru;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuruMalzemeTeslimi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtFizikTedavi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtRandevu;
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
 * @version 1.27.0
 * @since 5.11.2026 08:45
 */
@Stateless
public class OrtDashboardService implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 2007968748554406074L;

    private static final String DASHBOARD_PERMISSION = "ortez_protez_dashboard:goster";

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
            throw new SecurityException("Ortez-Protez dashboard görüntüleme yetkiniz bulunmamaktadır.");
        }
    }

    public long toplamHastaSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(h) FROM OrtHasta h WHERE h.aktif = true", Long.class).getSingleResult();
    }

    public long toplamBasvuruSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(b) FROM OrtBasvuru b WHERE b.aktif = true", Long.class).getSingleResult();
    }

    public long bekleyenBasvuruSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(b) FROM OrtBasvuru b WHERE b.aktif = true " +
                        "AND b.basvuruDurumu = tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruDurumu.BEKLEMEDE",
                Long.class).getSingleResult();
    }

    public long bugunBasvuruSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(b) FROM OrtBasvuru b WHERE b.aktif = true " +
                                "AND b.basvuruTarihi IS NOT NULL " +
                                "AND b.basvuruTarihi >= :baslangic AND b.basvuruTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long buAyBasvuruSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(b) FROM OrtBasvuru b WHERE b.aktif = true " +
                                "AND b.basvuruTarihi IS NOT NULL " +
                                "AND b.basvuruTarihi >= :baslangic AND b.basvuruTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamRandevuSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(r) FROM OrtRandevu r WHERE r.aktif = true", Long.class).getSingleResult();
    }

    public long bugunRandevuSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(r) FROM OrtRandevu r WHERE r.aktif = true " +
                                "AND r.randevuTarihi IS NOT NULL " +
                                "AND r.randevuTarihi >= :baslangic AND r.randevuTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long buAyRandevuSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(r) FROM OrtRandevu r WHERE r.aktif = true " +
                                "AND r.randevuTarihi IS NOT NULL " +
                                "AND r.randevuTarihi >= :baslangic AND r.randevuTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamFizikTedaviSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(f) FROM OrtFizikTedavi f WHERE f.aktif = true", Long.class).getSingleResult();
    }

    public long bekleyenFizikTedaviSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(f) FROM OrtFizikTedavi f WHERE f.aktif = true " +
                        "AND f.durum = tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtFizikTedaviDurum.BEKLIYOR",
                Long.class).getSingleResult();
    }

    public long tamamlananFizikTedaviSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(f) FROM OrtFizikTedavi f WHERE f.aktif = true " +
                        "AND f.durum = tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtFizikTedaviDurum.TAMAMLANDI",
                Long.class).getSingleResult();
    }

    public long toplamMalzemeTalepSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(m) FROM OrtMalzemeTalep m WHERE m.aktif = true", Long.class).getSingleResult();
    }

    public long teslimEdilmemisMalzemeTalepSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(m) FROM OrtMalzemeTalep m WHERE m.aktif = true AND m.teslimEdildi = false",
                Long.class).getSingleResult();
    }

    public long toplamStokSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(s) FROM tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStok s WHERE s.aktif = true", Long.class).getSingleResult();
    }

    public long toplamProjeSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(p) FROM tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtProje p WHERE p.aktif = true", Long.class).getSingleResult();
    }

    public List<OrtBasvuru> sonBasvurular() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT b FROM OrtBasvuru b WHERE b.aktif = true ORDER BY b.basvuruTarihi DESC",
                        OrtBasvuru.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<OrtRandevu> sonRandevular() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT r FROM OrtRandevu r WHERE r.aktif = true ORDER BY r.randevuTarihi DESC",
                        OrtRandevu.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<OrtFizikTedavi> sonFizikTedaviler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT f FROM OrtFizikTedavi f WHERE f.aktif = true ORDER BY f.tarih DESC",
                        OrtFizikTedavi.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<OrtBasvuruMalzemeTeslimi> sonMalzemeTeslimleri() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t FROM OrtBasvuruMalzemeTeslimi t WHERE t.aktif = true ORDER BY t.teslimTarihi DESC",
                        OrtBasvuruMalzemeTeslimi.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<OrtBasvuru> bekleyenBasvurular() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT b FROM OrtBasvuru b WHERE b.aktif = true " +
                                "AND b.basvuruDurumu = tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruDurumu.BEKLEMEDE " +
                                "ORDER BY b.basvuruTarihi DESC",
                        OrtBasvuru.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Object[]> aylikBasvuruTrendi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().minusMonths(11).withDayOfMonth(1).atStartOfDay();
        return em.createQuery(
                        "SELECT FUNCTION('YEAR', b.basvuruTarihi), FUNCTION('MONTH', b.basvuruTarihi), COUNT(b) " +
                                "FROM OrtBasvuru b " +
                                "WHERE b.aktif = true AND b.basvuruTarihi IS NOT NULL AND b.basvuruTarihi >= :baslangic " +
                                "GROUP BY FUNCTION('YEAR', b.basvuruTarihi), FUNCTION('MONTH', b.basvuruTarihi) " +
                                "ORDER BY FUNCTION('YEAR', b.basvuruTarihi), FUNCTION('MONTH', b.basvuruTarihi)",
                        Object[].class)
                .setParameter("baslangic", baslangic)
                .getResultList();
    }

    public List<Object[]> basvuruDurumuDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT b.basvuruDurumu, COUNT(b) FROM OrtBasvuru b WHERE b.aktif = true GROUP BY b.basvuruDurumu",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> fizikTedaviDurumuDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT f.durum, COUNT(f) FROM OrtFizikTedavi f WHERE f.aktif = true GROUP BY f.durum",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> cinsiyetDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT h.gnlKisi.cinsiyet, COUNT(h) " +
                                "FROM OrtHasta h " +
                                "WHERE h.aktif = true AND h.gnlKisi IS NOT NULL AND h.gnlKisi.cinsiyet IS NOT NULL " +
                                "GROUP BY h.gnlKisi.cinsiyet " +
                                "ORDER BY COUNT(h) DESC",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> yasDagilimi() {
        checkDashboardPermission();
        List<LocalDate> dogumTarihleri = em.createQuery(
                        "SELECT h.gnlKisi.dogumTarihi FROM OrtHasta h " +
                                "WHERE h.aktif = true AND h.gnlKisi IS NOT NULL AND h.gnlKisi.dogumTarihi IS NOT NULL",
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
                        "SELECT h.gnlKisi.gnlIlce.tanim, COUNT(h) " +
                                "FROM OrtHasta h " +
                                "WHERE h.aktif = true " +
                                "AND h.gnlKisi IS NOT NULL " +
                                "AND h.gnlKisi.gnlIl IS NOT NULL AND h.gnlKisi.gnlIl.id = 27 " +
                                "AND h.gnlKisi.gnlIlce IS NOT NULL " +
                                "AND h.gnlKisi.gnlMahalle IS NOT NULL " +
                                "GROUP BY h.gnlKisi.gnlIlce.tanim " +
                                "ORDER BY COUNT(h) DESC",
                        Object[].class)
                .getResultList();
    }
}

