package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkAileDurum;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.29.0
 * @since 12.11.2026 09:19
 */
@Named
@ViewScoped
@Slf4j
public class PkAileDurumController extends AbstractController<PkAileDurum> {

    @Serial
    private static final long serialVersionUID = -8155619000523503399L;

    public PkAileDurumController() {
        super(PkAileDurum.class);
    }
}
