package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlAnket;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 17.09.2025 11:26
 */
@Stateless
public class GnlAnketService extends AbstractService<GnlAnket> {

    @Serial
    private static final long serialVersionUID = 1534439617363631081L;

    public GnlAnketService() {
        super(GnlAnket.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Inject
    private GnlAnketCevapService gnlAnketCevapService;

    public GnlAnket findByToken(String token) {
        return (GnlAnket) getEntityManager()
                .createNamedQuery("GnlAnket.findByToken")
                .setParameter("token",token)
                .getResultList()
                .stream().
                findFirst()
                .orElse(null);
    }

    public boolean canSubmit(GnlAnket gnlAnket, Integer syKullaniciId, String tcNo, String anonToken) {
        return switch (gnlAnket.getAnketTuru()) {
            case KULLANICI -> !gnlAnketCevapService.existsBySurveyIdAndUserId(gnlAnket.getId(), syKullaniciId);
            case KISI      -> !gnlAnketCevapService.existsBySurveyIdAndTcNo(gnlAnket.getId(), tcNo);
            case GENEL     -> !gnlAnketCevapService.existsBySurveyIdAndAnonToken(gnlAnket.getId(), anonToken);
        };
    }
}
