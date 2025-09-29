package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcu;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcuSablonAlan;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtOlcuSablonAlanService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.Serial;
import java.util.LinkedHashMap;
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
    private OrtOlcuSablonAlanService ortOlcuSablonAlanService;

    @Getter
    @Setter
    private List<OrtOlcuSablonAlan> ortOlcuSablonAlanList;
    @Getter
    @Setter
    private OrtOlcuSablonAlan ortOlcuSablonAlan;
    @Getter
    @Setter
    private Map<String, String> values = new LinkedHashMap<>();
    @Getter
    @Setter
    private Integer imageWidth;
    @Getter
    @Setter
    private Integer imageHeight;

    @Getter
    private Integer selectedId;

    public OrtOlcuController() {
        super(OrtOlcu.class);
    }

    public void setSelectedId(Integer selectedId) {
        this.selectedId = selectedId;
        this.ortOlcuSablonAlan = ortOlcuSablonAlanService.find(selectedId);
    }

    @Override
    @PostConstruct
    public void init() {
        super.init();
        ortOlcuSablonAlanList = ortOlcuSablonAlanService.findByOrtOlcuSablonId(1);

        for (OrtOlcuSablonAlan ortOlcuSablonAlan : ortOlcuSablonAlanList) {
            values.put(ortOlcuSablonAlan.getTanim(),"");
        }
        try {
            BufferedImage img = ImageIO.read( FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/images/ayak_bilek.png"));
            imageWidth = img.getWidth();
            imageHeight = img.getHeight();
            this.ortOlcuSablonAlan = ortOlcuSablonAlanService.find(1);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
    }
}
