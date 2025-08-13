package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalepKonu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 15:21
 */
@Named
@ViewScoped
public class EyTalepKonuController extends AbstractController<EyTalepKonu> {

    @Serial
    private static final long serialVersionUID = -8445275624630220491L;

    public EyTalepKonuController() {
        super(EyTalepKonu.class);
    }
}
