package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbBasvuru;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbTalep;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbVerilecekHizmet;
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
 * @since 4.11.2026 10:14
 */
@Stateless
public class EdbDashboardService implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String DASHBOARD_PERMISSION = "evim_dunyalara_bedel_dashboard:goster";

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
            throw new SecurityException("Evim Dünyalara Bedel dashboard görüntüleme yetkiniz bulunmamaktadır.");
        }
    }

    public long toplamBasvuruSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(b) FROM EdbBasvuru b WHERE b.aktif = true", Long.class).getSingleResult();
    }

    public long bugunBasvuruSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(b) FROM EdbBasvuru b WHERE b.aktif = true " +
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
                        "SELECT COUNT(b) FROM EdbBasvuru b WHERE b.aktif = true " +
                                "AND b.basvuruTarihi IS NOT NULL " +
                                "AND b.basvuruTarihi >= :baslangic AND b.basvuruTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamTalepSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(t) FROM EdbTalep t WHERE t.aktif = true", Long.class).getSingleResult();
    }

    public long bekleyenTalepSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(t) FROM EdbTalep t WHERE t.aktif = true " +
                        "AND t.talepDurumu = tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbBasvuruDurumu.INCELEME_BEKLEYEN",
                Long.class).getSingleResult();
    }

    public long buAyTalepSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(t) FROM EdbTalep t WHERE t.aktif = true " +
                                "AND t.tarih IS NOT NULL " +
                                "AND t.tarih >= :baslangic AND t.tarih < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamVerilecekHizmetSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(v) FROM EdbVerilecekHizmet v WHERE v.aktif = true", Long.class).getSingleResult();
    }

    public long bekleyenVerilecekHizmetSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(v) FROM EdbVerilecekHizmet v WHERE v.aktif = true " +
                        "AND v.durum = tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbDurum.BEKLIYOR",
                Long.class).getSingleResult();
    }

    public long tamamlananVerilecekHizmetSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(v) FROM EdbVerilecekHizmet v WHERE v.aktif = true " +
                        "AND v.durum = tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbDurum.TAMAMLANDI",
                Long.class).getSingleResult();
    }

    public long buAyVerilecekHizmetSayisi() {
        checkDashboardPermission();
        LocalDate baslangic = LocalDate.now().withDayOfMonth(1);
        LocalDate bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(v) FROM EdbVerilecekHizmet v WHERE v.aktif = true " +
                                "AND v.hizmetTarihi IS NOT NULL " +
                                "AND v.hizmetTarihi >= :baslangic AND v.hizmetTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamMemnuniyetAnketiSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(a) FROM EdbMemnuniyetAnketi a WHERE a.aktif = true", Long.class).getSingleResult();
    }

    public long toplamEkipSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(e) FROM tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbEkip e WHERE e.aktif = true", Long.class).getSingleResult();
    }

    public long toplamPersonelSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(p) FROM tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbPersonel p WHERE p.aktif = true", Long.class).getSingleResult();
    }

    public List<EdbBasvuru> sonBasvurular() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT b FROM EdbBasvuru b WHERE b.aktif = true ORDER BY b.basvuruTarihi DESC",
                        EdbBasvuru.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<EdbTalep> sonTalepler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t FROM EdbTalep t WHERE t.aktif = true ORDER BY t.tarih DESC",
                        EdbTalep.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<EdbVerilecekHizmet> sonVerilecekHizmetler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT v FROM EdbVerilecekHizmet v WHERE v.aktif = true ORDER BY v.hizmetTarihi DESC",
                        EdbVerilecekHizmet.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<EdbTalep> bekleyenTalepler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t FROM EdbTalep t WHERE t.aktif = true " +
                                "AND t.talepDurumu = tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbBasvuruDurumu.INCELEME_BEKLEYEN " +
                                "ORDER BY t.tarih DESC",
                        EdbTalep.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Object[]> aylikBasvuruTrendi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().minusMonths(11).withDayOfMonth(1).atStartOfDay();
        return em.createQuery(
                        "SELECT FUNCTION('YEAR', b.basvuruTarihi), FUNCTION('MONTH', b.basvuruTarihi), COUNT(b) " +
                                "FROM EdbBasvuru b " +
                                "WHERE b.aktif = true AND b.basvuruTarihi IS NOT NULL AND b.basvuruTarihi >= :baslangic " +
                                "GROUP BY FUNCTION('YEAR', b.basvuruTarihi), FUNCTION('MONTH', b.basvuruTarihi) " +
                                "ORDER BY FUNCTION('YEAR', b.basvuruTarihi), FUNCTION('MONTH', b.basvuruTarihi)",
                        Object[].class)
                .setParameter("baslangic", baslangic)
                .getResultList();
    }

    public List<Object[]> talepDurumuDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t.talepDurumu, COUNT(t) FROM EdbTalep t WHERE t.aktif = true GROUP BY t.talepDurumu",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> cinsiyetDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT b.gnlKisi.cinsiyet, COUNT(b) " +
                                "FROM EdbBasvuru b " +
                                "WHERE b.aktif = true AND b.gnlKisi IS NOT NULL AND b.gnlKisi.cinsiyet IS NOT NULL " +
                                "GROUP BY b.gnlKisi.cinsiyet " +
                                "ORDER BY COUNT(b) DESC",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> yasDagilimi() {
        checkDashboardPermission();
        List<LocalDate> dogumTarihleri = em.createQuery(
                        "SELECT b.gnlKisi.dogumTarihi FROM EdbBasvuru b " +
                                "WHERE b.aktif = true AND b.gnlKisi IS NOT NULL AND b.gnlKisi.dogumTarihi IS NOT NULL",
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
                        "SELECT b.gnlKisi.gnlIlce.tanim, COUNT(b) " +
                                "FROM EdbBasvuru b " +
                                "WHERE b.aktif = true " +
                                "AND b.gnlKisi IS NOT NULL " +
                                "AND b.gnlKisi.gnlIl IS NOT NULL AND b.gnlKisi.gnlIl.id = 27 " +
                                "AND b.gnlKisi.gnlIlce IS NOT NULL " +
                                "AND b.gnlKisi.gnlMahalle IS NOT NULL " +
                                "GROUP BY b.gnlKisi.gnlIlce.tanim " +
                                "ORDER BY COUNT(b) DESC",
                        Object[].class)
                .getResultList();
    }
}
