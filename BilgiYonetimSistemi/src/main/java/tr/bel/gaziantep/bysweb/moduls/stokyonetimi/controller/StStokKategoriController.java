package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokKategori;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 10:48
 */
@Named
@ViewScoped
@Slf4j
public class StStokKategoriController extends AbstractController<StStokKategori> {

    @Serial
    private static final long serialVersionUID = -5925587332064642582L;

    public StStokKategoriController() {
        super(StStokKategori.class);
    }
}
