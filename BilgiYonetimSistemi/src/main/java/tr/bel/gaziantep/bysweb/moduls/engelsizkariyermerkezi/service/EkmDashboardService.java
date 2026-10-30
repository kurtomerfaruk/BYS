package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmIsBasvuru;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyer;
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
 * @since 30.10.2026 11:12
 */
@Stateless
public class EkmDashboardService implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 7076833933290411097L;

    private static final String DASHBOARD_PERMISSION = "engelsiz_kariyer_merkezi_dashboard:goster";

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
            throw new SecurityException("Engelsiz Kariyer Merkezi dashboard görüntüleme yetkiniz bulunmamaktadır.");
        }
    }

    public long toplamBasvuruSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(b) FROM EkmIsBasvuru b WHERE b.aktif = true", Long.class).getSingleResult();
    }

    public long bugunBasvuruSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(b) FROM EkmIsBasvuru b WHERE b.aktif = true " +
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
                        "SELECT COUNT(b) FROM EkmIsBasvuru b WHERE b.aktif = true " +
                                "AND b.basvuruTarihi IS NOT NULL " +
                                "AND b.basvuruTarihi >= :baslangic AND b.basvuruTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamKursiyerSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(k) FROM EkmKursiyer k WHERE k.aktif = true", Long.class).getSingleResult();
    }

    public long aktifKursiyerSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(k) FROM EkmKursiyer k WHERE k.aktif = true " +
                        "AND k.devamDurumu = tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDevamDurumu.DEVAM_EDIYOR",
                Long.class).getSingleResult();
    }

    public long toplamPozisyonSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(p) FROM tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmIsPozisyon p WHERE p.aktif = true", Long.class).getSingleResult();
    }

    public long toplamGirisCikisSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(g) FROM EkmGirisCikis g WHERE g.aktif = true", Long.class).getSingleResult();
    }

    public long buAyGirisCikisSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(g) FROM EkmGirisCikis g WHERE g.aktif = true " +
                                "AND g.girisTarihi IS NOT NULL " +
                                "AND g.girisTarihi >= :baslangic AND g.girisTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamKursKaydiSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(k) FROM EkmKursiyerKurs k WHERE k.aktif = true AND k.secili = true",
                Long.class).getSingleResult();
    }

    public List<EkmIsBasvuru> sonBasvurular() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT b FROM EkmIsBasvuru b WHERE b.aktif = true ORDER BY b.basvuruTarihi DESC",
                        EkmIsBasvuru.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<EkmKursiyer> sonKursiyerler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k FROM EkmKursiyer k WHERE k.aktif = true ORDER BY k.kayitTarihi DESC",
                        EkmKursiyer.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<EkmGirisCikis> sonGirisCikislar() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT g FROM EkmGirisCikis g WHERE g.aktif = true ORDER BY g.girisTarihi DESC",
                        EkmGirisCikis.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Object[]> aylikBasvuruTrendi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().minusMonths(11).withDayOfMonth(1).atStartOfDay();
        return em.createQuery(
                        "SELECT FUNCTION('YEAR', b.basvuruTarihi), FUNCTION('MONTH', b.basvuruTarihi), COUNT(b) " +
                                "FROM EkmIsBasvuru b " +
                                "WHERE b.aktif = true AND b.basvuruTarihi IS NOT NULL AND b.basvuruTarihi >= :baslangic " +
                                "GROUP BY FUNCTION('YEAR', b.basvuruTarihi), FUNCTION('MONTH', b.basvuruTarihi) " +
                                "ORDER BY FUNCTION('YEAR', b.basvuruTarihi), FUNCTION('MONTH', b.basvuruTarihi)",
                        Object[].class)
                .setParameter("baslangic", baslangic)
                .getResultList();
    }

    public List<Object[]> pozisyonDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT b.ekmIsPozisyon.tanim, COUNT(b) " +
                                "FROM EkmIsBasvuru b " +
                                "WHERE b.aktif = true AND b.ekmIsPozisyon IS NOT NULL " +
                                "GROUP BY b.ekmIsPozisyon.tanim " +
                                "ORDER BY COUNT(b) DESC",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> kursDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k.gnlKurs.tanim, COUNT(k) " +
                                "FROM EkmKursiyerKurs k " +
                                "WHERE k.aktif = true AND k.secili = true AND k.gnlKurs IS NOT NULL " +
                                "GROUP BY k.gnlKurs.tanim " +
                                "ORDER BY COUNT(k) DESC",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> cinsiyetDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT b.eyKisi.gnlKisi.cinsiyet, COUNT(b) " +
                                "FROM EkmIsBasvuru b " +
                                "WHERE b.aktif = true AND b.eyKisi IS NOT NULL AND b.eyKisi.gnlKisi IS NOT NULL " +
                                "AND b.eyKisi.gnlKisi.cinsiyet IS NOT NULL " +
                                "GROUP BY b.eyKisi.gnlKisi.cinsiyet " +
                                "ORDER BY COUNT(b) DESC",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> yasDagilimi() {
        checkDashboardPermission();
        List<LocalDate> dogumTarihleri = em.createQuery(
                        "SELECT b.eyKisi.gnlKisi.dogumTarihi FROM EkmIsBasvuru b " +
                                "WHERE b.aktif = true AND b.eyKisi IS NOT NULL AND b.eyKisi.gnlKisi IS NOT NULL " +
                                "AND b.eyKisi.gnlKisi.dogumTarihi IS NOT NULL",
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
                        "SELECT b.eyKisi.gnlKisi.gnlIlce.tanim, COUNT(b) " +
                                "FROM EkmIsBasvuru b " +
                                "WHERE b.aktif = true " +
                                "AND b.eyKisi IS NOT NULL AND b.eyKisi.gnlKisi IS NOT NULL " +
                                "AND b.eyKisi.gnlKisi.gnlIl IS NOT NULL AND b.eyKisi.gnlKisi.gnlIl.id = 27 " +
                                "AND b.eyKisi.gnlKisi.gnlIlce IS NOT NULL " +
                                "AND b.eyKisi.gnlKisi.gnlMahalle IS NOT NULL " +
                                "GROUP BY b.eyKisi.gnlKisi.gnlIlce.tanim " +
                                "ORDER BY COUNT(b) DESC",
                        Object[].class)
                .getResultList();
    }
}
