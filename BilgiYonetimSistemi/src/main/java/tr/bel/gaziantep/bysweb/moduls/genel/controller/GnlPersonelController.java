package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlBirim;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonelBirim;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlPersonelService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 14:25
 */
@Named
@ViewScoped
@Slf4j
public class GnlPersonelController extends AbstractController<GnlPersonel> {

    @Serial
    private static final long serialVersionUID = -2538277770254843862L;

    @Inject
    private GnlPersonelService service;

    @Getter
    @Setter
    private List<GnlBirim> gnlBirims;

    public GnlPersonelController() {
        super(GnlPersonel.class);
    }

    @Override
    public GnlPersonel prepareCreate(ActionEvent event) {
        GnlPersonel newItem;
        try {
            newItem = GnlPersonel.class.getDeclaredConstructor().newInstance();
            newItem.setGnlKisi(new GnlKisi());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    public void personelSecKapat(GnlPersonel personel) {
        PrimeFaces.current().dialog().closeDynamic(personel);
    }

    public void onRowDblSelect(SelectEvent<GnlPersonel> event) {
        GnlPersonel gnlPersonel = event.getObject();
        personelSecKapat(gnlPersonel);
    }

    public void secilenGnlKisi(SelectEvent<GnlKisi> event) {
        GnlKisi gnlKisi = event.getObject();
        this.getSelected().setGnlKisi(gnlKisi);
    }

    public void create(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                gnlBirimCheck();
                service.create(this.getSelected());
                FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            } catch (Exception ex) {
                log.error(null,ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void update(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                gnlBirimCheck();
                service.edit(this.getSelected());
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            } catch (Exception ex) {
               log.error(null,ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    private void gnlBirimCheck() {
        List<GnlPersonelBirim> personelBirimList = this.getSelected().getGnlPersonelBirimList();

        personelBirimList.forEach(pb -> pb.setSecili(false));

        Map<Integer, GnlPersonelBirim> personelBirimMap = personelBirimList.stream()
                .filter(pb -> pb.getGnlBirim() != null)
                .collect(Collectors.toMap(pb -> pb.getGnlBirim().getId(), pb -> pb));

        for (GnlBirim gnlBirim : gnlBirims) {
            GnlPersonelBirim existing = personelBirimMap.get(gnlBirim.getId());
            if (existing != null) {
                existing.setSecili(true);
            } else {
                GnlPersonelBirim yeni = GnlPersonelBirim.builder()
                        .secili(true)
                        .gnlBirim(gnlBirim)
                        .gnlPersonel(this.getSelected())
                        .build();
                personelBirimList.add(yeni);
                personelBirimMap.put(gnlBirim.getId(), yeni);
            }
        }
    }

    public void readInfo() {
        if (this.getSelected() != null) {
            gnlBirims = new ArrayList<>();
            this.getSelected().getGnlPersonelBirimList().forEach(gnlPersonelBirim -> gnlBirims.add(gnlPersonelBirim.getGnlBirim()));
        }
    }
}
