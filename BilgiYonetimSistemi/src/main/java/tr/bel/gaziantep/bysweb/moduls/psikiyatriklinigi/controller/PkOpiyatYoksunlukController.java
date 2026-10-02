package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkOpiyatYoksunluk;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 1.10.2026 16:29
 */
@Named
@ViewScoped
@Slf4j
public class PkOpiyatYoksunlukController extends AbstractController<PkOpiyatYoksunluk> {

    @Serial
    private static final long serialVersionUID = -5127236404586561555L;

    public PkOpiyatYoksunlukController() {
        super(PkOpiyatYoksunluk.class);
    }
}
