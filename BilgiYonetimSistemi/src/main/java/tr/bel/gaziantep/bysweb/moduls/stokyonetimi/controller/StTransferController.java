package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStTransferDurum;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StTransfer;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 10:09
 */
@Named
@ViewScoped
@Slf4j
public class StTransferController extends AbstractController<StTransfer> {

    @Serial
    private static final long serialVersionUID = 8834037836498684165L;

    public StTransferController() {
        super(StTransfer.class);
    }

    @Override
    public StTransfer prepareCreate(ActionEvent event) {
        StTransfer transfer = super.prepareCreate(event);
        if (transfer != null) {
            transfer.setDurum(EnumStTransferDurum.TASLAK);
            transfer.setTalepTarihi(LocalDateTime.now());
            transfer.setTalepEdenSyKullanici(Util.getSyKullanici());
        }
        return transfer;
    }
}
