package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyTablo;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyTabloService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 15:56
 */
@Named
@ViewScoped
@Slf4j
public class SyTabloController extends AbstractController<SyTablo> {

    @Serial
    private static final long serialVersionUID = 4022294296719935271L;

    @Inject
    private SyTabloService service;

    public SyTabloController() {
        super(SyTablo.class);
    }

    public void getAllTableNames() {
        try {
            List<String> tableNames = service.findAllTableName();
            if (!tableNames.isEmpty()) {
                List<SyTablo> tablos = service.findAll();
                for (String tableName : tableNames) {
                    SyTablo tablo = tablos.stream()
                            .filter(x -> x.getTabloAdi().equals(tableName))
                            .findFirst()
                            .orElse(null);
                    if (tablo == null) {
                        tablo = SyTablo.builder()
                                .tabloAdi(tableName)
                                .build();
                        tablo.setEkleyen(Util.getSyKullanici());
                        tablo.setEklemeTarihi(LocalDateTime.now());
                        service.edit(tablo);
                    }
                }
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            }
        } catch (Exception ex) {
           log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}
