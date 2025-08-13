package tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.entity.EkmIsPozisyon;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.07.2025 10:24
 */
@Named
@ViewScoped
@Slf4j
public class EkmIsPozisyonController extends AbstractController<EkmIsPozisyon> {

    @Serial
    private static final long serialVersionUID = 9082785903781306606L;

    public EkmIsPozisyonController() {
        super(EkmIsPozisyon.class);
    }
}
