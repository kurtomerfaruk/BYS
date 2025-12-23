package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporOzelFiltre;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 22.12.2025 16:21
 */
@Named
@ViewScoped
@Slf4j
public class GnlRaporOzelFiltreController extends AbstractController<GnlRaporOzelFiltre> {

    @Serial
    private static final long serialVersionUID = -7645931446571640326L;

    public GnlRaporOzelFiltreController() {
        super(GnlRaporOzelFiltre.class);
    }
}
