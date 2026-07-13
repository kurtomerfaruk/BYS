package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyerKurs;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.16.0
 * @since 8.07.2026 13:43
 */
@Named
@ViewScoped
@Slf4j
public class EkmKursiyerKursController extends AbstractController<EkmKursiyerKurs> {
    @Serial
    private static final long serialVersionUID = 8839743139598615238L;

    public EkmKursiyerKursController() {
        super(EkmKursiyerKurs.class);
    }
}
