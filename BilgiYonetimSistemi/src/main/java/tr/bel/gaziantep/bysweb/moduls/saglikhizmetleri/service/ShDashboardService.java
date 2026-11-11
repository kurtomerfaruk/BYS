package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.*;
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
 * @version 1.28.0
 * @since 11.11.2026 11:22
 */
@Stateless
public class ShDashboardService implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 4672947371934217461L;

    private static final String DASHBOARD_PERMISSION = "saglik_hizmetleri_dashboard:goster";

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
            throw new SecurityException("Sağlık Hizmetleri dashboard görüntüleme yetkiniz bulunmamaktadır.");
        }
    }

    public long toplamDanisanSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(k) FROM ShKisi k WHERE k.aktif = true", Long.class).getSingleResult();
    }

    public long bugunYeniKayitSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(k) FROM ShKisi k WHERE k.aktif = true " +
                                "AND k.gelisTarihi IS NOT NULL " +
                                "AND k.gelisTarihi >= :baslangic AND k.gelisTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long buAyYeniKayitSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(k) FROM ShKisi k WHERE k.aktif = true " +
                                "AND k.gelisTarihi IS NOT NULL " +
                                "AND k.gelisTarihi >= :baslangic AND k.gelisTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamKontrolSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(k) FROM ShKisiKontrol k WHERE k.aktif = true", Long.class).getSingleResult();
    }

    public long bugunKontrolSayisi() {
        checkDashboardPermission();
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(k) FROM ShKisiKontrol k WHERE k.aktif = true " +
                                "AND k.tarih IS NOT NULL " +
                                "AND k.tarih >= :today AND k.tarih < :tomorrow",
                        Long.class)
                .setParameter("today", today)
                .setParameter("tomorrow", tomorrow)
                .getSingleResult();
    }

    public long buAyKontrolSayisi() {
        checkDashboardPermission();
        LocalDate baslangic = LocalDate.now().withDayOfMonth(1);
        LocalDate bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(k) FROM ShKisiKontrol k WHERE k.aktif = true " +
                                "AND k.tarih IS NOT NULL " +
                                "AND k.tarih >= :baslangic AND k.tarih < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamTahlilSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(t) FROM ShKisiKanTahlilSonuc t WHERE t.aktif = true", Long.class).getSingleResult();
    }

    public long bugunTahlilSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(t) FROM ShKisiKanTahlilSonuc t WHERE t.aktif = true " +
                                "AND t.tarih IS NOT NULL " +
                                "AND t.tarih >= :baslangic AND t.tarih < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long buAyTahlilSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(t) FROM ShKisiKanTahlilSonuc t WHERE t.aktif = true " +
                                "AND t.tarih IS NOT NULL " +
                                "AND t.tarih >= :baslangic AND t.tarih < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamObeziteAnketSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(a) FROM ShObeziteAnket a WHERE a.aktif = true", Long.class).getSingleResult();
    }

    public long buAyObeziteAnketSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(a) FROM ShObeziteAnket a WHERE a.aktif = true " +
                                "AND a.anketTarihi IS NOT NULL " +
                                "AND a.anketTarihi >= :baslangic AND a.anketTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamRandevuSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(r) FROM ShRandevu r WHERE r.aktif = true", Long.class).getSingleResult();
    }

    public long bugunRandevuSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(r) FROM ShRandevu r WHERE r.aktif = true " +
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
                        "SELECT COUNT(r) FROM ShRandevu r WHERE r.aktif = true " +
                                "AND r.randevuTarihi IS NOT NULL " +
                                "AND r.randevuTarihi >= :baslangic AND r.randevuTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public List<ShKisi> sonDanisanlar() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k FROM ShKisi k WHERE k.aktif = true ORDER BY k.gelisTarihi DESC",
                        ShKisi.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<ShKisiKontrol> sonKontroller() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k FROM ShKisiKontrol k WHERE k.aktif = true ORDER BY k.tarih DESC",
                        ShKisiKontrol.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<ShKisiKanTahlilSonuc> sonTahliller() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t FROM ShKisiKanTahlilSonuc t WHERE t.aktif = true ORDER BY t.tarih DESC",
                        ShKisiKanTahlilSonuc.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<ShRandevu> sonRandevular() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT r FROM ShRandevu r WHERE r.aktif = true ORDER BY r.randevuTarihi DESC",
                        ShRandevu.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<ShObeziteAnket> sonObeziteAnketleri() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT a FROM ShObeziteAnket a WHERE a.aktif = true ORDER BY a.anketTarihi DESC",
                        ShObeziteAnket.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Object[]> aylikDanisanTrendi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().minusMonths(11).withDayOfMonth(1).atStartOfDay();
        return em.createQuery(
                        "SELECT FUNCTION('YEAR', k.gelisTarihi), FUNCTION('MONTH', k.gelisTarihi), COUNT(k) " +
                                "FROM ShKisi k " +
                                "WHERE k.aktif = true AND k.gelisTarihi IS NOT NULL AND k.gelisTarihi >= :baslangic " +
                                "GROUP BY FUNCTION('YEAR', k.gelisTarihi), FUNCTION('MONTH', k.gelisTarihi) " +
                                "ORDER BY FUNCTION('YEAR', k.gelisTarihi), FUNCTION('MONTH', k.gelisTarihi)",
                        Object[].class)
                .setParameter("baslangic", baslangic)
                .getResultList();
    }

    public List<Object[]> danismanlikHizmetiDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k.danismanlikHizmeti, COUNT(k) FROM ShKisi k " +
                                "WHERE k.aktif = true AND k.danismanlikHizmeti IS NOT NULL " +
                                "GROUP BY k.danismanlikHizmeti",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> verilenHizmetDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k.verilenHizmet, COUNT(k) FROM ShKisi k " +
                                "WHERE k.aktif = true AND k.verilenHizmet IS NOT NULL " +
                                "GROUP BY k.verilenHizmet",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> cinsiyetDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k.gnlKisi.cinsiyet, COUNT(k) " +
                                "FROM ShKisi k " +
                                "WHERE k.aktif = true AND k.gnlKisi IS NOT NULL AND k.gnlKisi.cinsiyet IS NOT NULL " +
                                "GROUP BY k.gnlKisi.cinsiyet " +
                                "ORDER BY COUNT(k) DESC",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> yasDagilimi() {
        checkDashboardPermission();
        List<LocalDate> dogumTarihleri = em.createQuery(
                        "SELECT k.gnlKisi.dogumTarihi FROM ShKisi k " +
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
                                "FROM ShKisi k " +
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
}

