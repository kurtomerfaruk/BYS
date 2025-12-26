package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullaniciGirisSayfa;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 26.12.2025 15:50
 */
@Named
@ViewScoped
public class SyKullaniciGirisSayfaController extends AbstractController<SyKullaniciGirisSayfa> {

    @Serial
    private static final long serialVersionUID = 421874451116921102L;

    public SyKullaniciGirisSayfaController() {
        super(SyKullaniciGirisSayfa.class);
    }
}
