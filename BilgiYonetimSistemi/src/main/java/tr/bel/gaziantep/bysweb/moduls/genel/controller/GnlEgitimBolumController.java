package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlEgitimBolum;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.07.2025 09:44
 */
@Named
@ViewScoped
@Slf4j
public class GnlEgitimBolumController extends AbstractController<GnlEgitimBolum> {

    @Serial
    private static final long serialVersionUID = 7540195173508195246L;

    public GnlEgitimBolumController() {
        super(GnlEgitimBolum.class);
    }
}
