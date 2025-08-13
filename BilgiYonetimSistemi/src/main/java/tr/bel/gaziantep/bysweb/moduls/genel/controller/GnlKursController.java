package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKurs;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 3.07.2025 08:26
 */
@Named
@ViewScoped
@Slf4j
public class GnlKursController extends AbstractController<GnlKurs> {

    @Serial
    private static final long serialVersionUID = 7752026277410335L;

    public GnlKursController() {
        super(GnlKurs.class);
    }
}
