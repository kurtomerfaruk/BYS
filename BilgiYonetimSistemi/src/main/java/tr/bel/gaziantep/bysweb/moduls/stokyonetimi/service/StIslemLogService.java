package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStIslemLogTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StIslemLog;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 13:25
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StIslemLogService extends AbstractService<StIslemLog> {

    @Serial
    private static final long serialVersionUID = -5000961389029011898L;

    public StIslemLogService() {
        super(StIslemLog.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void log(EnumStIslemLogTuru islemTuru, SyKullanici kullanici, StDepo depo,
                    String belgeTuru, String belgeNo, String detay) {
        try {
            StIslemLog log = new StIslemLog();
            log.setIslemTuru(islemTuru);
            log.setSyKullanici(kullanici);
            log.setStDepo(depo);
            log.setBelgeTuru(belgeTuru);
            log.setBelgeNo(belgeNo);
            log.setDetay(detay);
            try {
                log.setIpAdresi(Util.getRequest().getRemoteAddr());
            } catch (Exception ignored) {
            }
            // BaseEntity audit alanlari
            if (log.getEkleyen() == null) {
                // @PrePersist ekleyen set eder; ama em.persist oncesi manuel set etmeyelim
            }
            em.persist(log);
        } catch (Exception e) {
            // log hatasi is akisini bozmasin
        }
    }
}
