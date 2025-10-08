package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtFizikTedavi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 8.10.2025 15:31
 */
@Named
@ViewScoped
@Slf4j
public class OrtFizikTedaviController extends AbstractController<OrtFizikTedavi> {

    @Serial
    private static final long serialVersionUID = 4739534507739056360L;

    public OrtFizikTedaviController() {
        super(OrtFizikTedavi.class);
    }
}
