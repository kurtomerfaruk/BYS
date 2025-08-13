package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlUnvan;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 07:57
 */
@Named
@ViewScoped
@Slf4j
public class GnlUnvanController extends AbstractController<GnlUnvan> {

    @Serial
    private static final long serialVersionUID = -7687563548972872594L;

    public GnlUnvanController() {
        super(GnlUnvan.class);
    }
}
