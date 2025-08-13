package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:38
 */
@Named
@ViewScoped
@Slf4j
public class EyEngelGrubuController extends AbstractController<EyEngelGrubu> {

    @Serial
    private static final long serialVersionUID = -3255244913223754710L;

    public EyEngelGrubuController() {
        super(EyEngelGrubu.class);
    }
}
