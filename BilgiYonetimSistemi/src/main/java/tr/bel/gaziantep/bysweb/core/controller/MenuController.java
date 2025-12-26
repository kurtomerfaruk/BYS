package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullaniciGirisSayfa;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyKullaniciGirisSayfaService;

import java.io.Serial;
import java.util.Map;
import java.util.Set;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 10:01
 */

@Named
@ViewScoped
@Slf4j
public class MenuController implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -4385432633087470640L;

    @Inject
    private SyKullaniciGirisSayfaService syKullaniciGirisSayfaService;

    private static final Set<String> CONTROLLER_LIST= Set.of("menuController", "syDuyuruController", "syKullaniciController", "org.omnifaces.cdi.push.Socket");

    @Setter
    @Getter
    private String pageLink;

    public MenuController() {
        pageLink = "mainPage";
    }

    public void setPageLink(String pageLink) {
        clearViewScopedByAnnotation();
        this.pageLink = pageLink;
        addEntire(pageLink);
    }

    private void addEntire(String link) {
        try {
            SyKullaniciGirisSayfa sayfa = SyKullaniciGirisSayfa.builder().link(link).syKullanici(Util.getSyKullanici()).build();
            syKullaniciGirisSayfaService.create(sayfa);
        }catch (Exception e) {
            log.error(e.getMessage());
            FacesUtil.addErrorMessage(e.getMessage());
        }
    }

    public void clearViewScopedByAnnotation() {
        Map<String, Object> viewMap = FacesContext.getCurrentInstance()
                .getViewRoot()
                .getViewMap();

        viewMap.entrySet().removeIf(entry -> !CONTROLLER_LIST.contains(entry.getKey()));

//        viewMap.entrySet().removeIf(entry ->
//                !entry.getValue().getClass().isAnnotationPresent(KeepViewScoped.class)
//        );
    }
}
