package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyYetki;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 16:37
 */
@Stateless
public class SyYetkiService extends AbstractService<SyYetki> {

    @Serial
    private static final long serialVersionUID = -6152787044387066387L;

    public SyYetkiService() {
        super(SyYetki.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "aciklama";
    }


    /**
     * Kullanıcının rol veya doğrudan kullanıcı yetkisi üzerinden verilen yetkiye
     * sahip olup olmadığını kontrol eder. Servis katmanı yetki denetimlerinde kullanılır.
     */
    public boolean hasPermission(SyKullanici kullanici, String yetkiAnahtari) {
        if (kullanici == null || yetkiAnahtari == null || yetkiAnahtari.isBlank()) {
            return false;
        }
        Long rolYetkiSayisi = getEntityManager().createQuery(
                        "SELECT COUNT(y) FROM SyYetki y " +
                                "WHERE y.aktif = true AND y.yetki = :yetki AND y.id IN " +
                                "(SELECT ry.syYetki.id FROM SyRolYetki ry WHERE ry.aktif = true AND ry.syRol.id IN " +
                                "(SELECT kr.syRol.id FROM SyKullaniciRol kr WHERE kr.aktif = true AND kr.syKullanici.id = :kullaniciId))",
                        Long.class)
                .setParameter("yetki", yetkiAnahtari)
                .setParameter("kullaniciId", kullanici.getId())
                .getSingleResult();
        if (rolYetkiSayisi != null && rolYetkiSayisi > 0) {
            return true;
        }
        Long kullaniciYetkiSayisi = getEntityManager().createQuery(
                        "SELECT COUNT(y) FROM SyYetki y " +
                                "WHERE y.aktif = true AND y.yetki = :yetki AND y.id IN " +
                                "(SELECT ky.syYetki.id FROM SyKullaniciYetki ky WHERE ky.aktif = true AND ky.syKullanici.id = :kullaniciId)",
                        Long.class)
                .setParameter("yetki", yetkiAnahtari)
                .setParameter("kullaniciId", kullanici.getId())
                .getSingleResult();
        return kullaniciYetkiSayisi != null && kullaniciYetkiSayisi > 0;
    }
}
