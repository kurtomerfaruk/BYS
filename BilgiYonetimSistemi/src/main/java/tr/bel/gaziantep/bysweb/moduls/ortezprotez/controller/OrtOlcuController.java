package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.ErrorType;
import tr.bel.gaziantep.bysweb.core.exception.BysBusinessException;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.*;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtOlcuService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 26.09.2025 09:18
 */
@Named
@ViewScoped
@Slf4j
public class OrtOlcuController extends AbstractController<OrtOlcu> {

    @Serial
    private static final long serialVersionUID = -5791447196918662554L;

    @Inject
    private OrtOlcuService service;

    @Getter
    @Setter
    private OrtOlcuSablon ortOlcuSablon;
    @Getter
    @Setter
    private List<OrtOlcuSablon> ortOlcuSablonList = new ArrayList<>();
    @Getter
    @Setter
    private OrtOlcuSablon selectedSablon;
    @Getter
    @Setter
    private List<OrtOlcuDeger> currentOlcuDegerList;
    @Getter
    @Setter
    private Map<Integer, List<OrtOlcuDeger>> olcuMap = new HashMap<>();

    public OrtOlcuController() {
        super(OrtOlcu.class);
    }



    @Override
    public OrtOlcu prepareCreate(ActionEvent event) {
        OrtOlcu newItem;
        try {
            newItem = OrtOlcu.class.getDeclaredConstructor().newInstance();
            ortOlcuSablon = new OrtOlcuSablon();
            olcuMap = new HashMap<>();
            selectedSablon = new OrtOlcuSablon();
            ortOlcuSablonList = new ArrayList<>();
            newItem.setOrtBasvuru(new OrtBasvuru());
            newItem.setTarih(LocalDateTime.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenOrtBasvuru(SelectEvent<OrtBasvuru> event) {
        OrtBasvuru ortBasvuru = event.getObject();
        this.getSelected().setOrtBasvuru(ortBasvuru);
    }

    public void addTemplate() {
        if (ortOlcuSablon == null || StringUtil.isBlank(ortOlcuSablon.getTanim())) {
            return;
        }

        boolean alreadyExists = ortOlcuSablonList.stream().anyMatch(x -> x.getTanim().equals(ortOlcuSablon.getTanim()));
        if (!alreadyExists) {
            ortOlcuSablonList.add(ortOlcuSablon);
            ortOlcuSablon = new OrtOlcuSablon();
        }

    }

    public void removeTemplate(OrtOlcuSablon sablon) {
        if (sablon != null) {
            ortOlcuSablonList.removeIf(x -> x.getTanim().equals(sablon.getTanim()));
        }
    }


    public void prepareOlcuDialog(OrtOlcuSablon sablon) {
        this.selectedSablon = sablon;
        if (!olcuMap.containsKey(sablon.getId())) {
            List<OrtOlcuDeger> list = new ArrayList<>();
            for (OrtOlcuSablonAlan alan : sablon.getOrtOlcuSablonAlanList()) {
                OrtOlcuDeger deger = OrtOlcuDeger.builder()
                        .ortOlcu(this.getSelected())
                        .ortOlcuSablonAlan(alan)
                        .build();
                list.add(deger);
            }
            olcuMap.put(sablon.getId(), list);
        }

        this.currentOlcuDegerList = olcuMap.get(sablon.getId());
    }

    @Override
    public void save(ActionEvent event) {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_SECILEMEDI);
        }
        try {
            service.save(this.getSelected(),olcuMap);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}
