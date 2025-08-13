package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIl;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlIlceService;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 30.06.2025 08:59
 */
@Named
@ViewScoped
@Slf4j
public class GnlIlceController extends AbstractController<GnlIlce> {
    @Serial
    private static final long serialVersionUID = -6270981271938354045L;

    @Inject
    private GnlIlceService service;

    public GnlIlceController() {
        super(GnlIlce.class);
    }

    public List<GnlIlce> getIlceByIl() {
        return service.findByIlId(27);
    }

    public List<GnlIlce> getIlceByIlId(GnlIl gnlIl) {
        if(gnlIl==null || gnlIl.getId()==null){
            return Collections.emptyList();
        }
        return service.findByIlId(gnlIl.getId());
    }

}
