package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyGorusmeKonu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 10:59
 */
@Named
@ViewScoped
@Slf4j
public class EyGorusmeKonuController extends AbstractController<EyGorusmeKonu> {
    @Serial
    private static final long serialVersionUID = -7757006508200117365L;

    public EyGorusmeKonuController() {
        super(EyGorusmeKonu.class);
    }
}
