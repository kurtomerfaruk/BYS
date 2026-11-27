package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabTalepDurumu;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalepHareket;

import java.io.Serial;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 11:46
 */
@Stateless
public class TabTalepService extends AbstractService<TabTalep> {

    @Serial
    private static final long serialVersionUID = -3719097928964784407L;

    @Inject
    private TabTalepHareketService hareketService;

    public TabTalepService() {
        super(TabTalep.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createTalep(TabTalep talep) {
        if (talep == null) {
            throw new IllegalArgumentException("Oluşturulacak talep bulunamadı.");
        }
        if (talep.getTabKisi() == null || talep.getTabKisi().getId() == null) {
            throw new IllegalArgumentException("Talep için kişi seçilmelidir.");
        }
        if (talep.getTalepTarihi() == null) {
            talep.setTalepTarihi(LocalDateTime.now());
        }
        if (talep.getDurum() == null) {
            talep.setDurum(EnumTabTalepDurumu.YENI);
        }

        create(talep);
        flush();
        talep.setTalepNo(generateTalepNo(talep.getId()));
        edit(talep);
        flush();

        hareketService.add(talep, null, EnumTabTalepDurumu.YENI, "CREATE", "Talep oluşturuldu.");
        flush();
    }

    /**
     * Genel bilgi güncellemesi yalnızca kilitlememiş durumlarda yapılabilir.
     * Durum değişikliği bu metotla yapılamaz; workflow servisi üzerinden yapılır.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateTalep(TabTalep talep) {
        if (talep == null || talep.getId() == null) {
            throw new IllegalArgumentException("Güncellenecek talep bulunamadı.");
        }
        TabTalep mevcut = em.find(TabTalep.class, talep.getId());
        if (mevcut == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }
        if (mevcut.getDurum() != null && mevcut.getDurum().isUpdateLocked()) {
            throw new IllegalArgumentException("Bu aşamada talep bilgileri güncellenemez. " +
                    "Değişiklikler iş akışı üzerinden yapılmalıdır.");
        }
        mevcut.setAciklama(talep.getAciklama());
        edit(mevcut);
        flush();
    }

    public List<TabTalepHareket> findHareketByTalep(TabTalep talep) {
        return hareketService.findByTalep(talep);
    }

    private String generateTalepNo(Integer id) {
        return "TAB-" + Year.now().getValue() + "-" + String.format("%06d", id == null ? 0 : id);
    }

    /**
     * Talep numarası benzersizliği için yardımcı kontrol.
     */
    public boolean existByTalepNo(String talepNo) {
        if (StringUtil.isBlank(talepNo)) {
            return false;
        }
        return !getEntityManager()
                .createQuery("SELECT t.id FROM TabTalep t WHERE t.talepNo = :talepNo", Integer.class)
                .setParameter("talepNo", talepNo)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }
}
