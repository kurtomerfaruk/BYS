package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyDuyuruTur;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyDuyuru;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyDuyuruKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyDuyuruKullaniciService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyDuyuruService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyKullaniciService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.08.2025 09:00
 */
@Named
@ViewScoped
@Slf4j
public class SyDuyuruController extends AbstractController<SyDuyuru> {

    @Serial
    private static final long serialVersionUID = -3722560327086570519L;

    @Inject
    private SyDuyuruService service;
    @Inject
    private SyKullaniciService syKullaniciService;
    @Inject
    private SyDuyuruKullaniciService syDuyuruKullaniciService;
    @Inject
    @Push(channel = "notificationChannel")
    private PushContext pushContext;
    @Inject
    private FilterOptionService filterOptionService;

    public SyDuyuruController() {
        super(SyDuyuru.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case DUYURU_TUR -> {
                return filterOptionService.getSyDuyuruTurs();
            }
            case EVET_HAYIR -> {
                return filterOptionService.getEvetHayirs();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public SyDuyuru prepareCreate(ActionEvent event) {
        SyDuyuru newItem;
        try {
            newItem = SyDuyuru.class.getDeclaredConstructor().newInstance();
            newItem.setYayinlanmaTarihi(LocalDateTime.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void confirm(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                this.getSelected().setOnaylandi(true);
                this.getSelected().setOnaylanmaTarihi(LocalDateTime.now());
                this.getSelected().setOnaylayanSyKullanici(Util.getSyKullanici());

                //save(event);
                service.edit(this.getSelected());

                if (this.getSelected().getDuyuruTur().equals(EnumSyDuyuruTur.KULLANICILAR)) {
                    List<SyKullanici> kullaniciList = syKullaniciService.findAll();
                    for (SyKullanici syKullanici : kullaniciList) {
                        SyDuyuruKullanici duyuruKullanici = SyDuyuruKullanici.builder()
                                .syDuyuru(this.getSelected())
                                .syKullanici(syKullanici)
                                .okundu(Boolean.FALSE)
                                .build();
                        syDuyuruKullaniciService.create(duyuruKullanici);
                        syKullanici.getSyDuyuruKullaniciList().add(duyuruKullanici);
                    }
                    HttpSession session = Util.getSession();
                    List<SyKullanici> kullanicis = (List<SyKullanici>) session.getAttribute("syKullanicilar");
                    Collection<Integer> kullaniciIds = kullanicis.stream().map(SyKullanici::getId).collect(Collectors.toList());
                    pushContext.send(new FacesMessage("Yeni Duyuru"), kullaniciIds);
                }

            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage("kayitOnaylanirkenHata");
            } finally {
                this.setSelected(null);
            }
        }
    }

    public List<SyDuyuru> getTopFivePublic() {
        return service.getTopFive();
    }

    public List<SyDuyuruKullanici> getDuyurus() {
        SyKullanici syKullanici = Util.getSyKullanici();
        return syDuyuruKullaniciService.findByKullanici(syKullanici);
    }

}
