package tr.bel.gaziantep.bysweb.moduls.aktifyasam.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AySaglikBilgi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 13:57
 */
@Named
@ViewScoped
@Slf4j
public class AySaglikBilgiController extends AbstractController<AySaglikBilgi> {

    @Serial
    private static final long serialVersionUID = -1723160282936151652L;

    public AySaglikBilgiController() {
        super(AySaglikBilgi.class);
    }
}
