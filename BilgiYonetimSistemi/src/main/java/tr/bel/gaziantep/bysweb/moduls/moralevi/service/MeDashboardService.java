package tr.bel.gaziantep.bysweb.moduls.moralevi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeEtkinlik;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeTalep;
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
 * @since 11.11.2026 10:47
 */
@Stateless
public class MeDashboardService implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -6975931845706209207L;

    private static final String DASHBOARD_PERMISSION = "moral_evi_dashboard:goster";

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
            throw new SecurityException("Moral Evi dashboard görüntüleme yetkiniz bulunmamaktadır.");
        }
    }

    public long toplamKisiSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(k) FROM MeKisi k WHERE k.aktif = true", Long.class).getSingleResult();
    }

    public long bugunYeniKayitSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(k) FROM MeKisi k WHERE k.aktif = true " +
                                "AND k.kayitTarihi IS NOT NULL " +
                                "AND k.kayitTarihi >= :baslangic AND k.kayitTarihi < :bitis",
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
                        "SELECT COUNT(k) FROM MeKisi k WHERE k.aktif = true " +
                                "AND k.kayitTarihi IS NOT NULL " +
                                "AND k.kayitTarihi >= :baslangic AND k.kayitTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamEtkinlikSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(e) FROM MeEtkinlik e WHERE e.aktif = true", Long.class).getSingleResult();
    }

    public long yaklasanEtkinlikSayisi() {
        checkDashboardPermission();
        LocalDate today = LocalDate.now();
        LocalDate bitis = today.plusDays(30);
        return em.createQuery(
                        "SELECT COUNT(e) FROM MeEtkinlik e WHERE e.aktif = true " +
                                "AND e.tarih IS NOT NULL " +
                                "AND e.tarih >= :today AND e.tarih <= :bitis",
                        Long.class)
                .setParameter("today", today)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long buAyEtkinlikSayisi() {
        checkDashboardPermission();
        LocalDate baslangic = LocalDate.now().withDayOfMonth(1);
        LocalDate bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(e) FROM MeEtkinlik e WHERE e.aktif = true " +
                                "AND e.tarih IS NOT NULL " +
                                "AND e.tarih >= :baslangic AND e.tarih < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long etkinlikKatilimSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(k) FROM MeEtkinlikKisi k WHERE k.aktif = true", Long.class).getSingleResult();
    }

    public long toplamGirisCikisSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(g) FROM MeGirisCikis g WHERE g.aktif = true", Long.class).getSingleResult();
    }

    public long bugunGirisCikisSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(g) FROM MeGirisCikis g WHERE g.aktif = true " +
                                "AND g.girisTarihi IS NOT NULL " +
                                "AND g.girisTarihi >= :baslangic AND g.girisTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long buAyGirisCikisSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(g) FROM MeGirisCikis g WHERE g.aktif = true " +
                                "AND g.girisTarihi IS NOT NULL " +
                                "AND g.girisTarihi >= :baslangic AND g.girisTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamTalepSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(t) FROM MeTalep t WHERE t.aktif = true", Long.class).getSingleResult();
    }

    public long bekleyenTalepSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(t) FROM MeTalep t WHERE t.aktif = true " +
                        "AND t.durum = tr.bel.gaziantep.bysweb.core.enums.moralevi.EnumMeTalepDurumu.BEKLIYOR",
                Long.class).getSingleResult();
    }

    public long buAyTalepSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(t) FROM MeTalep t WHERE t.aktif = true " +
                                "AND t.tarih IS NOT NULL " +
                                "AND t.tarih >= :baslangic AND t.tarih < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamAnketSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(a) FROM MeAnket a WHERE a.aktif = true", Long.class).getSingleResult();
    }

    public long toplamPersonelSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(p) FROM tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MePersonel p WHERE p.aktif = true", Long.class).getSingleResult();
    }

    public List<MeKisi> sonKisiler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k FROM MeKisi k WHERE k.aktif = true ORDER BY k.kayitTarihi DESC",
                        MeKisi.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<MeEtkinlik> sonEtkinlikler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT e FROM MeEtkinlik e WHERE e.aktif = true ORDER BY e.tarih DESC",
                        MeEtkinlik.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<MeGirisCikis> sonGirisCikislar() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT g FROM MeGirisCikis g WHERE g.aktif = true ORDER BY g.girisTarihi DESC",
                        MeGirisCikis.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<MeTalep> sonTalepler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t FROM MeTalep t WHERE t.aktif = true ORDER BY t.tarih DESC",
                        MeTalep.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<MeTalep> bekleyenTalepler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t FROM MeTalep t WHERE t.aktif = true " +
                                "AND t.durum = tr.bel.gaziantep.bysweb.core.enums.moralevi.EnumMeTalepDurumu.BEKLIYOR " +
                                "ORDER BY t.tarih DESC",
                        MeTalep.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Object[]> aylikKisiTrendi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().minusMonths(11).withDayOfMonth(1).atStartOfDay();
        return em.createQuery(
                        "SELECT FUNCTION('YEAR', k.kayitTarihi), FUNCTION('MONTH', k.kayitTarihi), COUNT(k) " +
                                "FROM MeKisi k " +
                                "WHERE k.aktif = true AND k.kayitTarihi IS NOT NULL AND k.kayitTarihi >= :baslangic " +
                                "GROUP BY FUNCTION('YEAR', k.kayitTarihi), FUNCTION('MONTH', k.kayitTarihi) " +
                                "ORDER BY FUNCTION('YEAR', k.kayitTarihi), FUNCTION('MONTH', k.kayitTarihi)",
                        Object[].class)
                .setParameter("baslangic", baslangic)
                .getResultList();
    }

    public List<Object[]> aylikEtkinlikTrendi() {
        checkDashboardPermission();
        LocalDate baslangic = LocalDate.now().minusMonths(11).withDayOfMonth(1);
        return em.createQuery(
                        "SELECT FUNCTION('YEAR', e.tarih), FUNCTION('MONTH', e.tarih), COUNT(e) " +
                                "FROM MeEtkinlik e " +
                                "WHERE e.aktif = true AND e.tarih IS NOT NULL AND e.tarih >= :baslangic " +
                                "GROUP BY FUNCTION('YEAR', e.tarih), FUNCTION('MONTH', e.tarih) " +
                                "ORDER BY FUNCTION('YEAR', e.tarih), FUNCTION('MONTH', e.tarih)",
                        Object[].class)
                .setParameter("baslangic", baslangic)
                .getResultList();
    }

    public List<Object[]> talepDurumuDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t.durum, COUNT(t) FROM MeTalep t WHERE t.aktif = true GROUP BY t.durum",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> anketMemnuniyetDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT a.hizmetlerdenMemnun, COUNT(a) FROM MeAnket a WHERE a.aktif = true GROUP BY a.hizmetlerdenMemnun",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> cinsiyetDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k.eyKisi.gnlKisi.cinsiyet, COUNT(k) " +
                                "FROM MeKisi k " +
                                "WHERE k.aktif = true AND k.eyKisi IS NOT NULL AND k.eyKisi.gnlKisi IS NOT NULL " +
                                "AND k.eyKisi.gnlKisi.cinsiyet IS NOT NULL " +
                                "GROUP BY k.eyKisi.gnlKisi.cinsiyet " +
                                "ORDER BY COUNT(k) DESC",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> yasDagilimi() {
        checkDashboardPermission();
        List<LocalDate> dogumTarihleri = em.createQuery(
                        "SELECT k.eyKisi.gnlKisi.dogumTarihi FROM MeKisi k " +
                                "WHERE k.aktif = true AND k.eyKisi IS NOT NULL AND k.eyKisi.gnlKisi IS NOT NULL " +
                                "AND k.eyKisi.gnlKisi.dogumTarihi IS NOT NULL",
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
                        "SELECT k.eyKisi.gnlKisi.gnlIlce.tanim, COUNT(k) " +
                                "FROM MeKisi k " +
                                "WHERE k.aktif = true " +
                                "AND k.eyKisi IS NOT NULL AND k.eyKisi.gnlKisi IS NOT NULL " +
                                "AND k.eyKisi.gnlKisi.gnlIl IS NOT NULL AND k.eyKisi.gnlKisi.gnlIl.id = 27 " +
                                "AND k.eyKisi.gnlKisi.gnlIlce IS NOT NULL " +
                                "AND k.eyKisi.gnlKisi.gnlMahalle IS NOT NULL " +
                                "GROUP BY k.eyKisi.gnlKisi.gnlIlce.tanim " +
                                "ORDER BY COUNT(k) DESC",
                        Object[].class)
                .getResultList();
    }
}
