package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabRandevuDurumu;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabRandevuTuru;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabTalepIslem;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyYetkiService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabRandevu;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:34
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TabTalepWorkflowService implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 751374689667839785L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Inject
    private SyYetkiService syYetkiService;
    @Inject
    private TabRandevuService randevuService;
    @Inject
    private TabTalepHareketService hareketService;

    /**
     * Randevu kaydı + talep durum geçişi tek transaction'da.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createAppointmentAndTransition(Integer talepId, EnumTabRandevuTuru tur,
                                               LocalDateTime randevuTarihi, String aciklama) {
        if (talepId == null || tur == null || randevuTarihi == null) {
            throw new IllegalArgumentException("Randevu için talep, tür ve tarih zorunludur.");
        }
        TabTalep talep = em.find(TabTalep.class, talepId);
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }

        EnumTabTalepIslem action = resolveAppointmentAction(tur, talep.getDurum());
        checkPermission(action);
        checkTransition(talep, action);
        checkGuards(talep, action, tur);

        SyKullanici kullanici = Util.getSyKullanici();
        TabRandevu randevu = TabRandevu.builder()
                .tabTalep(talep)
                .tabKisi(talep.getTabKisi())
                .randevuTuru(tur)
                .randevuTarihi(randevuTarihi)
                .durum(EnumTabRandevuDurumu.PLANLANDI)
                .olusturan(kullanici)
                .aciklama(aciklama)
                .build();
        em.persist(randevu);
        em.flush();

        doTransition(talep, action, aciklama == null || aciklama.isBlank()
                ? tur.getDisplayValue() + " randevusu oluşturuldu."
                : aciklama);
    }

    /**
     * Mevcut durumda uygulanabilir işlemler (yetki filtreli).
     */
    public boolean isActionAvailable(TabTalep talep, EnumTabTalepIslem action) {
        if (talep == null || action == null) {
            return false;
        }
        if (!action.getSourceStatuses().contains(talep.getDurum())) {
            return false;
        }
        return syYetkiService.hasPermission(Util.getSyKullanici(), action.getPermission());
    }

    private EnumTabTalepIslem resolveAppointmentAction(EnumTabRandevuTuru tur, EnumTabTalepDurumu durum) {
        if (tur == EnumTabRandevuTuru.OLCU && durum == EnumTabTalepDurumu.YENI) {
            return EnumTabTalepIslem.RANDEVU_VER;
        }
        // Teslimat randevusu üretim tamamlandıktan sonra eklenecek (Adım 8).
        throw new IllegalArgumentException("Bu aşamada " + tur.getDisplayValue() + " randevusu verilemez.");
    }

    private void checkPermission(EnumTabTalepIslem action) {
        if (!syYetkiService.hasPermission(Util.getSyKullanici(), action.getPermission())) {
            throw new IllegalArgumentException("Bu işlem için yetkiniz bulunmuyor: " + action.getDisplayValue());
        }
    }

    private void checkTransition(TabTalep talep, EnumTabTalepIslem action) {
        if (!action.getSourceStatuses().contains(talep.getDurum())) {
            throw new IllegalArgumentException(
                    "Talep mevcut durumda (" + talep.getDurum().getDisplayValue() +
                            ") bu işlem uygulanamaz: " + action.getDisplayValue());
        }
        if (talep.getDurum().isClosed()) {
            throw new IllegalArgumentException("Kapatılmış talep üzerinde işlem yapılamaz.");
        }
    }

    private void checkGuards(TabTalep talep, EnumTabTalepIslem action, EnumTabRandevuTuru tur) {
        if (action == EnumTabTalepIslem.RANDEVU_VER) {
            if (talep.getTabKisi() == null || talep.getTabKisi().getId() == null) {
                throw new IllegalArgumentException("Randevu verilmeden önce talep kişisi seçilmelidir.");
            }
            if (tur != EnumTabRandevuTuru.OLCU) {
                throw new IllegalArgumentException("Bu aşamada yalnızca ölçüm randevusu verilebilir.");
            }
        }
    }

    private void doTransition(TabTalep talep, EnumTabTalepIslem action, String description) {
        em.lock(talep, LockModeType.OPTIMISTIC);
        EnumTabTalepDurumu eskiDurum = talep.getDurum();
        EnumTabTalepDurumu yeniDurum = action.getTargetStatus();
        talep.setDurum(yeniDurum);
        em.merge(talep);
        em.flush();

        hareketService.add(talep, eskiDurum, yeniDurum, action.name(),
                StringUtil.isBlank(description) ? action.getDisplayValue() : description);
        em.flush();
    }
}

