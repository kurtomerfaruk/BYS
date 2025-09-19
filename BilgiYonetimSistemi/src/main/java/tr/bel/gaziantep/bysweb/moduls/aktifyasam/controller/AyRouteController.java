package tr.bel.gaziantep.bysweb.moduls.aktifyasam.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractRouteController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyKisi;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.service.AyKisiService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.09.2025 08:57
 */
@Named
@ViewScoped
@Slf4j
public class AyRouteController extends AbstractRouteController<AyKisi> implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 8921464330298730717L;

    @Inject
    private AyKisiService ayKisiService;

    @Override
    protected String getLatLng(AyKisi kisi) {
        return kisi.getGnlKisi().getLatLng();
    }

    @Override
    protected String getTcKimlikNo(AyKisi kisi) {
        return kisi.getGnlKisi().getTcKimlikNo();
    }

    @Override
    protected GnlKisi getGnlKisi(AyKisi kisi) {
        return kisi.getGnlKisi();
    }

    @Override
    protected void setGnlKisi(AyKisi kisi, GnlKisi gnlKisi) {
        kisi.setGnlKisi(gnlKisi);
    }

    @Override
    protected EnumModul getModul() {
        return EnumModul.AKTIF_YASAM;
    }

    @Override
    protected void saveEntity(AyKisi kisi) {
        ayKisiService.edit(kisi);
    }
}
