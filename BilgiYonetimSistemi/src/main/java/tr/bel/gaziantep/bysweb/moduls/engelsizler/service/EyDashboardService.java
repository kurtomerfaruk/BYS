package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyAracCihazTeslimi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyAracTamir;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalep;
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
 * @since 30.10.2026 10:20
 */
@Stateless
public class EyDashboardService implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -9156801889763071791L;

    private static final String DASHBOARD_PERMISSION = "engelsizler_dashboard:goster";

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
            throw new SecurityException("Engelsizler dashboard görüntüleme yetkiniz bulunmamaktadır.");
        }
    }

    public long toplamKisiSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(k) FROM EyKisi k WHERE k.aktif = true", Long.class).getSingleResult();
    }

    public long bugunYeniKayitSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1);
        return em.createQuery(
                        "SELECT COUNT(k) FROM EyKisi k WHERE k.aktif = true " +
                                "AND k.eklemeTarihi IS NOT NULL " +
                                "AND k.eklemeTarihi >= :baslangic AND k.eklemeTarihi < :bitis",
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
                        "SELECT COUNT(k) FROM EyKisi k WHERE k.aktif = true " +
                                "AND k.eklemeTarihi IS NOT NULL " +
                                "AND k.eklemeTarihi >= :baslangic AND k.eklemeTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamTalepSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(t) FROM EyTalep t WHERE t.aktif = true", Long.class).getSingleResult();
    }

    public long bekleyenTalepSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(t) FROM EyTalep t WHERE t.aktif = true " +
                        "AND t.durum = tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu.BEKLIYOR",
                Long.class).getSingleResult();
    }

    public long buAyTalepSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(t) FROM EyTalep t WHERE t.aktif = true " +
                                "AND t.tarih IS NOT NULL " +
                                "AND t.tarih >= :baslangic AND t.tarih < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamTamirSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(t) FROM EyAracTamir t WHERE t.aktif = true", Long.class).getSingleResult();
    }

    public long buAyTamirSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(t) FROM EyAracTamir t WHERE t.aktif = true " +
                                "AND t.kayitTarihi IS NOT NULL " +
                                "AND t.kayitTarihi >= :baslangic AND t.kayitTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long toplamCihazTeslimSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(c) FROM EyAracCihazTeslimi c WHERE c.aktif = true", Long.class).getSingleResult();
    }

    public long buAyCihazTeslimSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(c) FROM EyAracCihazTeslimi c WHERE c.aktif = true " +
                                "AND c.verilisTarihi IS NOT NULL " +
                                "AND c.verilisTarihi >= :baslangic AND c.verilisTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long sosyalIncelemeRaporuSayisi() {
        checkDashboardPermission();
        return em.createQuery("SELECT COUNT(r) FROM EySosyalIncelemeRaporu r WHERE r.aktif = true", Long.class).getSingleResult();
    }

    public long buAySosyalIncelemeSayisi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime bitis = baslangic.plusMonths(1);
        return em.createQuery(
                        "SELECT COUNT(r) FROM EySosyalIncelemeRaporu r WHERE r.aktif = true " +
                                "AND r.raporTarihi IS NOT NULL " +
                                "AND r.raporTarihi >= :baslangic AND r.raporTarihi < :bitis",
                        Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("bitis", bitis)
                .getSingleResult();
    }

    public long kullanilanCihazSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(c) FROM tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisiKullandigiCihaz c " +
                        "WHERE c.aktif = true AND c.secili = true",
                Long.class).getSingleResult();
    }

    public long engelGrubuSayisi() {
        checkDashboardPermission();
        return em.createQuery(
                "SELECT COUNT(g) FROM tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu g WHERE g.aktif = true",
                Long.class).getSingleResult();
    }

    public List<EyKisi> sonKisiler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k FROM EyKisi k WHERE k.aktif = true ORDER BY k.eklemeTarihi DESC",
                        EyKisi.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<EyTalep> sonTalepler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t FROM EyTalep t WHERE t.aktif = true ORDER BY t.tarih DESC",
                        EyTalep.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<EyTalep> bekleyenTalepler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t FROM EyTalep t WHERE t.aktif = true " +
                                "AND t.durum = tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu.BEKLIYOR " +
                                "ORDER BY t.tarih DESC",
                        EyTalep.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<EyAracTamir> sonTamirler() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t FROM EyAracTamir t WHERE t.aktif = true ORDER BY t.kayitTarihi DESC",
                        EyAracTamir.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<EyAracCihazTeslimi> sonCihazTeslimleri() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT c FROM EyAracCihazTeslimi c WHERE c.aktif = true ORDER BY c.verilisTarihi DESC",
                        EyAracCihazTeslimi.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Object[]> aylikTalepTrendi() {
        checkDashboardPermission();
        LocalDateTime baslangic = LocalDate.now().minusMonths(11).withDayOfMonth(1).atStartOfDay();
        return em.createQuery(
                        "SELECT FUNCTION('YEAR', t.tarih), FUNCTION('MONTH', t.tarih), COUNT(t) " +
                                "FROM EyTalep t " +
                                "WHERE t.aktif = true AND t.tarih IS NOT NULL AND t.tarih >= :baslangic " +
                                "GROUP BY FUNCTION('YEAR', t.tarih), FUNCTION('MONTH', t.tarih) " +
                                "ORDER BY FUNCTION('YEAR', t.tarih), FUNCTION('MONTH', t.tarih)",
                        Object[].class)
                .setParameter("baslangic", baslangic)
                .getResultList();
    }

    public List<Object[]> engelGrubuDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT g.eyEngelGrubu.tanim, COUNT(g) " +
                                "FROM tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisiEngelGrubu g " +
                                "WHERE g.aktif = true AND g.secili = true AND g.eyEngelGrubu IS NOT NULL " +
                                "GROUP BY g.eyEngelGrubu.tanim " +
                                "ORDER BY COUNT(g) DESC",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> talepDurumuDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t.durum, COUNT(t) FROM EyTalep t WHERE t.aktif = true GROUP BY t.durum",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> talepTuruDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT t.talepTuru, COUNT(t) FROM EyTalep t WHERE t.aktif = true GROUP BY t.talepTuru",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> cinsiyetDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k.gnlKisi.cinsiyet, COUNT(k) " +
                                "FROM EyKisi k " +
                                "WHERE k.aktif = true AND k.gnlKisi IS NOT NULL AND k.gnlKisi.cinsiyet IS NOT NULL " +
                                "GROUP BY k.gnlKisi.cinsiyet " +
                                "ORDER BY COUNT(k) DESC",
                        Object[].class)
                .getResultList();
    }

    public List<Object[]> ilceDagilimi() {
        checkDashboardPermission();
        return em.createQuery(
                        "SELECT k.gnlKisi.gnlIlce.tanim, COUNT(k) " +
                                "FROM EyKisi k " +
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

    public List<Object[]> yasDagilimi() {
        checkDashboardPermission();
        List<LocalDate> dogumTarihleri = em.createQuery(
                        "SELECT k.gnlKisi.dogumTarihi FROM EyKisi k " +
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
}
