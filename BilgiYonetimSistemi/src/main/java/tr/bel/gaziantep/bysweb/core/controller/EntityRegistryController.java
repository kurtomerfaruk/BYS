package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.dtos.EntityInfo;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.EnumUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.12.2025 09:44
 */
@Named
@ApplicationScoped
@Slf4j
public class EntityRegistryController implements Serializable {
    @Serial
    private static final long serialVersionUID = -6722194815426011135L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Getter
    private List<EntityInfo> entities;
    @Getter
    private List<EntityInfo> enums;

    @PostConstruct
    public void init() {
        entities = em.getMetamodel()
                .getEntities()
                .stream()
                .map(e-> new EntityInfo(e.getName(),e.getJavaType().getName()))
                .sorted(Comparator.comparing(EntityInfo::getEntityName))
                .toList();
        try {
            List<Class<?>> enumList = EnumUtil.findEnumsInPackage("tr.bel.gaziantep.bysweb.core.enums");
            enums = enumList.stream()
                    .map(e->new EntityInfo(e.getName().substring(e.getName().lastIndexOf(".")+1),e.getName()))
                    .sorted(Comparator.comparing(EntityInfo::getEntityName))
                    .toList();
        }catch (Exception e) {
            log.error(null,e);
        }
    }

}
