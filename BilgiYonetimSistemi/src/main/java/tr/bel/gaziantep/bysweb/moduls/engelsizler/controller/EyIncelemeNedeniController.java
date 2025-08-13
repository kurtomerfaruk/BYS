package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyIncelemeNedeni;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 11:17
 */
@Named
@ViewScoped
@Slf4j
public class EyIncelemeNedeniController extends AbstractController<EyIncelemeNedeni> {

    @Serial
    private static final long serialVersionUID = 248223544518240588L;

    public EyIncelemeNedeniController() {
        super(EyIncelemeNedeni.class);
    }
}
