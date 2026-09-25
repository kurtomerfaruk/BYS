package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlUnvan;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlPersonelService;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkPersonel;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 22.09.2026 14:21
 */
@Named
@ViewScoped
@Slf4j
public class PkPersonelController extends AbstractController<PkPersonel> {

    @Serial
    private static final long serialVersionUID = -8834885649649411798L;

    @Inject
    private KpsController kpsController;
    @Inject
    private GnlKisiService gnlKisiService;
    @Inject
    private GnlPersonelService gnlPersonelService;

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


}
