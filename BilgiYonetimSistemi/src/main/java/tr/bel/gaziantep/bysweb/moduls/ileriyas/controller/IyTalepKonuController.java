package tr.bel.gaziantep.bysweb.moduls.ileriyas.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.entity.IyTalepKonu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.11.0
 * @since 15.06.2026 09:00
 */
@Named
@ViewScoped
@Slf4j
public class IyTalepKonuController extends AbstractController<IyTalepKonu> {

    @Serial
    private static final long serialVersionUID = 6912745496254552487L;

    public IyTalepKonuController() {
        super(IyTalepKonu.class);
    }
}
