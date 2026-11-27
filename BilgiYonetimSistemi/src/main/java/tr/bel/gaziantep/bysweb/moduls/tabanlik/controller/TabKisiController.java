package tr.bel.gaziantep.bysweb.moduls.tabanlik.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractKisiController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIl;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabKisi;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabKisiService;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 27.11.2026 10:20
 */
@Named
@ViewScoped
@Slf4j
public class TabKisiController extends AbstractKisiController<TabKisi> {

    @Serial
    private static final long serialVersionUID = 4493998220316610995L;

    @Inject
    private TabKisiService service;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private boolean sehirDisi;

    public TabKisiController() {
        super(TabKisi.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            case IL -> {
                return filterOptionService.getGnlIls();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public TabKisi prepareCreate(ActionEvent event) {
        TabKisi newItem = super.prepareCreate(event);
        GnlKisi kisi = GnlKisi.builder()
                .kayitTarihi(LocalDate.now())
                .gnlIl(new GnlIl())
                .gnlIlce(new GnlIlce())
                .gnlMahalle(new GnlMahalle())
                .eklemeYeri(EnumModul.TABANLIK).build();
        newItem.setGnlKisi(kisi);
        return newItem;
    }

    public void getTcKimlik() {
        fetchTcKimlik(
                TabKisi::getGnlKisi,
                TabKisi::setGnlKisi,
                EnumModul.TABANLIK
        );
        sehirDisi = this.getSelected().getGnlKisi().getGnlIl() == null || this.getSelected().getGnlKisi().getGnlIl().getId() != 27;
    }

    /**
     * Aynı TC kimlik numarasına sahip başka bir aktif hasta kaydı olup olmadığını kontrol eder.
     * Create ve Edit sayfaları ortak {@link #save} metodunu kullandığı için hem ekleme
     * hem de güncelleme sırasında mükerrer kayıt engellenir.
     */
    private boolean isDuplicatePerson() {
        if (this.getSelected() == null || this.getSelected().getGnlKisi() == null) {
            return false;
        }

        String tcKimlikNo = this.getSelected().getGnlKisi().getTcKimlikNo();
        if (StringUtil.isBlank(tcKimlikNo)) {
            return false;
        }

        TabKisi existing = service.findByTcKimlikNo(tcKimlikNo);
        if (existing != null && !Objects.equals(existing.getId(), this.getSelected().getId())) {
            FacesUtil.addErrorMessage(existing.getGnlKisi().getAdSoyad() + " T.C. kimlik numarasına ait zaten bir hasta kaydı bulunmaktadır.");
            return true;
        }

        return false;
    }

    @Override
    public void save(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                if (isDuplicatePerson()) {
                    return;
                }
                super.save(event);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }
}
