package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Function;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.*;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 14:42
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SyKullaniciService extends AbstractService<SyKullanici> {

    @Serial
    private static final long serialVersionUID = -8962450162815646089L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public SyKullaniciService() {
        super(SyKullanici.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SyKullanici findByKullaniciAdiByParola(String kullaniciAdi, String parola) {
        return (SyKullanici) getEntityManager().createNamedQuery("SyKullanici.findByKullaniciAdiByParola")
                .setParameter("kullaniciAdi", kullaniciAdi)
                .setParameter("parola", Function.encrypt(parola))
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public SyKullanici findByKullaniciAdi(String kullaniciAdi) {
        return (SyKullanici) getEntityManager().createNamedQuery("SyKullanici.findByKullaniciAdi")
                .setParameter("kullaniciAdi", kullaniciAdi)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(SyKullanici syKullanici, List<SyRol> rols, List<SyYetki> yetkis) {
        syKullanici.getSyKullaniciRols().forEach(kullaniciRol -> kullaniciRol.setAktif(false));
        Map<SyRol, SyKullaniciRol> rolYetkiMap = syKullanici.getSyKullaniciRols().stream()
                .collect(Collectors.toMap(SyKullaniciRol::getSyRol, java.util.function.Function.identity()));
        for (SyRol rol : rols) {
            SyKullaniciRol kullaniciRol = rolYetkiMap.get(rol);
            if (kullaniciRol == null) {
                kullaniciRol = SyKullaniciRol.builder()
                        .syRol(rol)
                        .syKullanici(syKullanici)
                        .build();
                kullaniciRol.setAktif(true);
                syKullanici.getSyKullaniciRols().add(kullaniciRol);
            } else {
                kullaniciRol.setAktif(true);
            }
        }

        syKullanici.getSyKullaniciYetkis().forEach(kullaniciYetki -> kullaniciYetki.setAktif(false));
        Map<SyYetki, SyKullaniciYetki> kullaniciYetkiMap = syKullanici.getSyKullaniciYetkis().stream()
                .collect(Collectors.toMap(SyKullaniciYetki::getSyYetki, java.util.function.Function.identity()));
        for (SyYetki yetki : yetkis) {
            SyKullaniciYetki kullaniciYetki = kullaniciYetkiMap.get(yetki);
            if (kullaniciYetki == null) {
                kullaniciYetki = SyKullaniciYetki.builder()
                        .syYetki(yetki)
                        .syKullanici(syKullanici)
                        .build();
                kullaniciYetki.setAktif(true);
                syKullanici.getSyKullaniciYetkis().add(kullaniciYetki);
            } else {
                kullaniciYetki.setAktif(true);
            }
        }

        edit(syKullanici);
    }



    public List<SyKullanici> findByKilitli() {
        return getEntityManager().createNamedQuery("SyKullanici.findByKilitli").getResultList();
    }
}
