package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyRol;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyRolYetki;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyYetki;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 16:37
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SyRolService extends AbstractService<SyRol> {

    @Serial
    private static final long serialVersionUID = -8850564793887055960L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public SyRolService() {
        super(SyRol.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(SyRol rol, List<SyYetki> yetkis) {
        rol.getSyRolYetkis().forEach(rolYetki -> rolYetki.setAktif(false));
        Map<SyYetki, SyRolYetki> rolYetkiMap = rol.getSyRolYetkis().stream()
                .collect(Collectors.toMap(SyRolYetki::getSyYetki, Function.identity()));
        for (SyYetki yetki : yetkis) {
            SyRolYetki rolYetki = rolYetkiMap.get(yetki);
            if (rolYetki == null) {
                rolYetki = SyRolYetki.builder()
                        .syRol(rol)
                        .syYetki(yetki)
                        .build();
                rolYetki.setAktif(true);
                rol.getSyRolYetkis().add(rolYetki);
            } else {
                rolYetki.setAktif(true);
            }
        }
        edit(rol);
    }
}
