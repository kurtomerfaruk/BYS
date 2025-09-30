package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.file.UploadedFile;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Function;
import tr.bel.gaziantep.bysweb.core.utils.ImageUtil;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcuSablon;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtOlcuSablonAlanService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtOlcuSablonService;

import java.io.File;
import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 26.09.2025 10:50
 */
@Named
@ViewScoped
@Slf4j
public class OrtOlcuSablonController extends AbstractController<OrtOlcuSablon> {

    @Serial
    private static final long serialVersionUID = 6458571228506153728L;

    @Inject
    private OrtOlcuSablonService service;
    @Inject
    private OrtOlcuSablonAlanService ortOlcuSablonAlanService;
    @Inject
    private OrtOlcuSablonImageController ortOlcuSablonImageController;


    public OrtOlcuSablonController() {
        super(OrtOlcuSablon.class);
    }

    @Override
    public void save(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                String path = createFilePath(ortOlcuSablonImageController.getFile());
                this.getSelected().setResimYolu(path);
                service.edit(this.getSelected());
                ImageUtil.saveImage(ortOlcuSablonImageController.getFile(), path);
                ortOlcuSablonAlanService.detectAndSaveFields(this.getSelected(),path);
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            } catch (Exception e) {
                log.error(e.getMessage());
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    private String createFilePath(UploadedFile file) {
        String path = Function.isWindows() ? Constants.ORTEZ_PROTEZ_OLCU_SABLON_KLASORU : Constants.ORTEZ_PROTEZ_OLCU_SABLON_KLASORU_MAC;
        File folder = new File(path + this.getSelected().getId());

        if (!folder.exists()) {
            folder.mkdir();
        }

        String dosyaAdi = file.getFileName();

        return folder + File.separator + dosyaAdi;

    }

}
