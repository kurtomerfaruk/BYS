package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullaniciGiris;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullaniciGirisSayfa;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * Sistem yöneticisi paneli (dashboard) agregasyon sorguları.
 * Tüm metotlar salt okunur çalışır; sadece mevcut log tablolarını
 * (SYKULLANICI, SYKULLANICI_GIRIS, SYKULLANICI_GIRIS_SAYFA, SYKULLANICI_ROL) kullanır.
 *
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.25.0
 * @since 27.10.2026 11:19
 */
@Stateless
public class SyDashboardService implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 6416453166782854648L;

    private static final String HATALI_GIRIS = "Hatalı giriş";

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public long toplamKullaniciSayisi() {
        return em.createQuery("SELECT COUNT(s) FROM SyKullanici s WHERE s.aktif = true", Long.class)
                .getSingleResult();
    }

    public long kilitliKullaniciSayisi() {
        return em.createQuery("SELECT COUNT(s) FROM SyKullanici s WHERE s.aktif = true AND s.kilitli = true", Long.class)
                .getSingleResult();
    }

    public long parolaDegistirilecekKullaniciSayisi() {
        return em.createQuery("SELECT COUNT(s) FROM SyKullanici s WHERE s.aktif = true AND s.kilitli = false AND s.parolaDegistirilsin = true", Long.class)
                .getSingleResult();
    }

    public long hataliGirisSayisi(LocalDateTime baslangic) {
        return em.createQuery("SELECT COUNT(s) FROM SyKullaniciGiris s WHERE s.girisZamani >= :baslangic AND s.aciklama = :aciklama", Long.class)
                .setParameter("baslangic", baslangic)
                .setParameter("aciklama", HATALI_GIRIS)
                .getSingleResult();
    }

    /**
     * Başarılı giriş kayıtlarını (zaman, kullanıcı id) çiftleri olarak döndürür.
     * Trend grafiği ve günlük KPI hesaplamaları bu liste üzerinden bellekte gruplanır.
     */
    public List<Object[]> basariliGirisKayitlari(LocalDateTime baslangic) {
        return em.createQuery("SELECT s.girisZamani, s.syKullanici.id FROM SyKullaniciGiris s"
                        + " WHERE s.girisZamani >= :baslangic AND s.aciklama <> :aciklama", Object[].class)
                .setParameter("baslangic", baslangic)
                .setParameter("aciklama", HATALI_GIRIS)
                .getResultList();
    }

    public List<LocalDateTime> sayfaHareketZamanlari(LocalDateTime baslangic) {
        return em.createQuery("SELECT s.eklemeTarihi FROM SyKullaniciGirisSayfa s"
                        + " WHERE s.eklemeTarihi >= :baslangic", LocalDateTime.class)
                .setParameter("baslangic", baslangic)
                .getResultList();
    }

    /**
     * Sayfa hareket kayıtlarını (link, kullanıcı id, zaman) üçlüleri olarak döndürür.
     * Modül bazlı kullanım istatistikleri link ön ekine göre bellekte gruplanır.
     */
    public List<Object[]> sayfaHareketKayitlari(LocalDateTime baslangic) {
        return em.createQuery("SELECT s.link, s.syKullanici.id, s.eklemeTarihi FROM SyKullaniciGirisSayfa s"
                        + " WHERE s.eklemeTarihi >= :baslangic", Object[].class)
                .setParameter("baslangic", baslangic)
                .getResultList();
    }

    public List<Object[]> rolDagilimi() {
        return em.createQuery("SELECT s.syRol.rolAdi, COUNT(s) FROM SyKullaniciRol s"
                        + " WHERE s.aktif = true GROUP BY s.syRol.rolAdi ORDER BY COUNT(s) DESC", Object[].class)
                .getResultList();
    }

    public List<SyKullaniciGiris> sonGirisler(int adet) {
        return em.createQuery("SELECT s FROM SyKullaniciGiris s JOIN FETCH s.syKullanici"
                        + " ORDER BY s.girisZamani DESC", SyKullaniciGiris.class)
                .setMaxResults(adet)
                .getResultList();
    }

    public List<SyKullaniciGirisSayfa> sonSayfaHareketleri(int adet) {
        return em.createQuery("SELECT s FROM SyKullaniciGirisSayfa s JOIN FETCH s.syKullanici"
                        + " ORDER BY s.eklemeTarihi DESC", SyKullaniciGirisSayfa.class)
                .setMaxResults(adet)
                .getResultList();
    }
}