package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabRandevuDurumu;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabRandevuTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabRandevu;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:33
 */
@Stateless
public class TabRandevuService extends AbstractService<TabRandevu> {

    @Serial
    private static final long serialVersionUID = -4357894371397217157L;

    @Inject
    private TabTalepHareketService hareketService;

    public TabRandevuService() {
        super(TabRandevu.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<TabRandevu> findByTalep(TabTalep tabTalep) {
        if (tabTalep == null || tabTalep.getId() == null) {
            return List.of();
        }
        return getEntityManager()
                .createQuery("SELECT r FROM TabRandevu r WHERE r.aktif=true AND r.tabTalep = :talep " +
                                "ORDER BY r.randevuTarihi DESC, r.id DESC",
                        TabRandevu.class)
                .setParameter("talep", tabTalep)
                .getResultList();
    }

    public List<TabRandevu> findByTalepAndTur(TabTalep tabTalep, EnumTabRandevuTuru tur) {
        if (tabTalep == null || tabTalep.getId() == null || tur == null) {
            return List.of();
        }
        return getEntityManager()
                .createQuery("SELECT r FROM TabRandevu r WHERE r.aktif=true AND r.tabTalep = :talep " +
                                "AND r.randevuTuru = :tur ORDER BY r.randevuTarihi DESC, r.id DESC",
                        TabRandevu.class)
                .setParameter("talep", tabTalep)
                .setParameter("tur", tur)
                .getResultList();
    }

    public List<TabRandevu> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return getEntityManager()
                .createQuery("SELECT r FROM TabRandevu r WHERE r.aktif=true " +
                                "AND r.randevuTarihi >= :start AND r.randevuTarihi <= :end " +
                                "ORDER BY r.randevuTarihi ASC",
                        TabRandevu.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    public boolean hasAppointment(TabTalep tabTalep, EnumTabRandevuTuru tur) {
        return !findByTalepAndTur(tabTalep, tur).isEmpty();
    }

    public boolean existsPlanned(TabTalep tabTalep, EnumTabRandevuTuru tur) {
        return findByTalepAndTur(tabTalep, tur).stream()
                .anyMatch(r -> r.getDurum() == EnumTabRandevuDurumu.PLANLANDI);
    }

    public boolean existsAttended(TabTalep tabTalep, EnumTabRandevuTuru tur) {
        return findByTalepAndTur(tabTalep, tur).stream()
                .anyMatch(r -> r.getDurum() == EnumTabRandevuDurumu.GERCEKLESTI);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void cancel(Integer randevuId, String neden) {
        TabRandevu randevu = em.find(TabRandevu.class, randevuId);
        if (randevu == null) {
            throw new IllegalArgumentException("Randevu kaydı bulunamadı.");
        }
        if (StringUtil.isBlank(neden)) {
            throw new IllegalArgumentException("Randevu iptal nedeni zorunludur.");
        }
        randevu.setDurum(EnumTabRandevuDurumu.IPTAL);
        randevu.setIptalNedeni(neden);
        edit(randevu);
        flush();
        addHistory(randevu, "APPOINTMENT_CANCEL", "Randevu iptal edildi: " + neden);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void postpone(Integer randevuId, String neden) {
        TabRandevu randevu = em.find(TabRandevu.class, randevuId);
        if (randevu == null) {
            throw new IllegalArgumentException("Randevu kaydı bulunamadı.");
        }
        if (StringUtil.isBlank(neden)) {
            throw new IllegalArgumentException("Randevu erteleme nedeni zorunludur.");
        }
        randevu.setDurum(EnumTabRandevuDurumu.ERTELENDI);
        randevu.setErtelemeNedeni(neden);
        edit(randevu);
        flush();
        addHistory(randevu, "APPOINTMENT_POSTPONE", "Randevu ertelendi: " + neden);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void complete(Integer randevuId) {
        TabRandevu randevu = em.find(TabRandevu.class, randevuId);
        if (randevu == null) {
            throw new IllegalArgumentException("Randevu kaydı bulunamadı.");
        }
        randevu.setDurum(EnumTabRandevuDurumu.GERCEKLESTI);
        randevu.setGerceklestiren(Util.getSyKullanici());
        edit(randevu);
        flush();
        addHistory(randevu, "APPOINTMENT_COMPLETE", "Randevu gerçekleşti.");
    }

    private void addHistory(TabRandevu randevu, String islem, String aciklama) {
        if (randevu != null && randevu.getTabTalep() != null) {
            hareketService.add(randevu.getTabTalep(), null, randevu.getTabTalep().getDurum(), islem, aciklama);
        }
    }
}
