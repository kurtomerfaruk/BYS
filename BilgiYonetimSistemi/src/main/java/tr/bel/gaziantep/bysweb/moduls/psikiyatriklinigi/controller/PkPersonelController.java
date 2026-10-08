package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlUnvan;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkPersonel;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service.PkPersonelService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 22.09.2026 14:21
 */
@Named
@ViewScoped
@Slf4j
public class PkPersonelController extends AbstractController<PkPersonel> {

    @Serial
    private static final long serialVersionUID = -8834885649649411798L;

    @Inject
    private PkPersonelService service;

    public PkPersonelController() {
        super(PkPersonel.class);
    }

    @Override
    public PkPersonel prepareCreate(ActionEvent event) {
        PkPersonel newItem;
        try {
            newItem = PkPersonel.class.getDeclaredConstructor().newInstance();
            GnlPersonel personel = new GnlPersonel();
            personel.setGnlKisi(new GnlKisi());
            personel.setGnlUnvan(new GnlUnvan());
            newItem.setGnlPersonel(personel);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void personelSecKapat(PkPersonel pkPersonel) {
        PrimeFaces.current().dialog().closeDynamic(pkPersonel);
    }

    public void onRowDblSelect(SelectEvent<PkPersonel> event) {
        personelSecKapat(event.getObject());
    }

//    public void getTcKimlik() {
//        try {
//            if (this.getSelected() != null) {
//                GnlKisi gnlKisi = this.getSelected().getGnlPersonel().getGnlKisi();
//                GnlKisi kisiFromMernis = kpsController.findByTcKimlikNo(gnlKisi, EnumModul.PSIKIYATRI_KLINIGI);
//                if (kisiFromMernis != null) {
//                    GnlKisi existingKisi = gnlKisiService.findByTckimlikNoByDogumTarihi(kisiFromMernis.getTcKimlikNo(), kisiFromMernis.getDogumTarihi());
//                    if (existingKisi != null) {
//                        kisiFromMernis.setId(existingKisi.getId());
//                    }
//
//                    GnlPersonel existingPersonel = kisiFromMernis.getId() == null ? null : gnlPersonelService.findByGnlKisi(kisiFromMernis);
//                    GnlPersonel personel;
//                    if (existingPersonel != null) {
//                        existingPersonel.setGnlKisi(kisiFromMernis);
//                        personel = existingPersonel;
//                    } else {
//                        personel = new GnlPersonel();
//                        personel.setGnlKisi(kisiFromMernis);
//                    }
//                    this.getSelected().setGnlPersonel(personel);
//                }
//            }
//        } catch (Exception ex) {
//            log.error(null, ex);
//            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
//        }
//    }

    public List<PkPersonel> getPersonelsByUnvan(String unvan){
        return service.getPersonelsByUnvan(unvan);
    }


}
