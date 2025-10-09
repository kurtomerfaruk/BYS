package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuruMalzemeTeslimi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.10.2025 11:06
 */
@Named
@ViewScoped
@Slf4j
public class OrtBasvuruMalzemeTeslimiController extends AbstractController<OrtBasvuruMalzemeTeslimi> {

    @Serial
    private static final long serialVersionUID = -1175701547163371972L;

    public OrtBasvuruMalzemeTeslimiController() {
        super(OrtBasvuruMalzemeTeslimi.class);
    }
}
