package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.ejb.EJBException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepIslem;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepTipi;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.dto.OrtTalepAsama;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtKisi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalepDurumGecmisi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTeslimat;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtRandevuService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtTalepService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtTalepWorkflowService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtTeslimatService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 16:01
 */
@Named
@ViewScoped
@Slf4j
public class OrtTalepController extends AbstractController<OrtTalep> {

    @Serial
    private static final long serialVersionUID = -6479112138153571653L;

    @Inject
    private OrtTalepService service;
    @Inject
    private OrtTalepWorkflowService workflowService;
    @Inject
    private OrtRandevuService ortRandevuService;
    @Inject
    private OrtTeslimatService ortTeslimatService;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private List<OrtTalepDurumGecmisi> statusHistory = Collections.emptyList();
    @Getter
    @Setter
    private Set<EnumOrtTalepIslem> availableActions = Collections.emptySet();
    @Getter
    @Setter
    private String transitionDescription;
    @Getter
    @Setter
    private String pendingAction;
    @Getter
    private List<OrtTeslimat> teslimatlar = Collections.emptyList();

    public OrtTalepController() {
        super(OrtTalep.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ORTTALEP_DURUMU -> {
                return filterOptionService.getOrtTalepDurumus();
            }
            case ORTTALEP_TIPI -> {
                return filterOptionService.getOrtTalepTipis();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public OrtTalep prepareCreate(ActionEvent event) {
        OrtTalep newItem = super.prepareCreate(event);
        newItem.setTalepTarihi(LocalDateTime.now());
        newItem.setDurum(EnumOrtTalepDurumu.YENI_TALEP);
        newItem.setTalepTipi(EnumOrtTalepTipi.ON_GORUSME);
        statusHistory = Collections.emptyList();
        return newItem;
    }

    public void saveNew(ActionEvent event) {
        if (this.getSelected() == null) {
            return;
        }
        try {
            if (this.getSelected().getOrtKisi() == null) {
                FacesUtil.addErrorMessage("Lütfen talep için kişi seçiniz.");
                return;
            }
            service.createTalep(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            if (!isValidationFailed()) {
                super.setItems(null);
                super.setLazyItems(null);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void save(ActionEvent event) {
        if (this.getSelected() == null) {
            return;
        }
        try {
            if (this.getSelected().getOrtKisi() == null) {
                FacesUtil.addErrorMessage("Lütfen talep için kişi seçiniz.");
                return;
            }
            service.updateTalep(this.getSelected());
            readInfo();
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void readInfo() {
        // Her çağrıda türetilmiş alanlar sıfırlanır; aksi hâlde önceki talebe ait
        // aksiyonlar/yetkiler sonraki detay ekranında yanlışlıkla görünür kalabilir.
        statusHistory = Collections.emptyList();
        availableActions = Collections.emptySet();
        pendingAction = "-";
        teslimatlar = Collections.emptyList();
        transitionDescription = null;

        if (this.getSelected() != null && this.getSelected().getId() != null) {
            // Detay her açıldığında talebi veritabanından yenile; randevu gibi
            // başka bir akış durum değiştirmiş olsa bile güncel durum görünsün.
            this.setSelected(service.find(this.getSelected().getId()));
        }
        if (this.getSelected() != null) {
            statusHistory = workflowService.getStatusHistory(this.getSelected());
            availableActions = workflowService.getAvailableActions(this.getSelected());
            pendingAction = workflowService.getPendingAction(this.getSelected());
            teslimatlar = ortTeslimatService.findByTalep(this.getSelected());
        }
    }

    /**
     * Ücret/proje aşamasından sonra genel güncelleme ekranı kapalıdır.
     */
    public boolean isUpdateLocked() {
        return this.getSelected() != null
                && this.getSelected().getDurum() != null
                && this.getSelected().getDurum().isUpdateLocked();
    }

    /**
     * Yalnızca gerçekten bitmiş taleplerde "Süreç tamamlandı" kartı gösterilir.
     * Bekleyen/orta aşamadaki taleplerde bu kart görünürse akış kilitli sanılır.
     */
    public boolean isProcessComplete() {
        // TAMAMLANDI ve TALEP_REDDEDILDI hâlâ "Kapat" adımına sahiptir;
        // yalnızca KAPATILDI gerçek son durumdur.
        EnumOrtTalepDurumu durum = this.getSelected() == null ? null : this.getSelected().getDurum();
        return durum == EnumOrtTalepDurumu.KAPATILDI;
    }

    /**
     * Detay ekranında "Süreç tamamlandı" yerine bekleme mesajı gösterilir.
     */
    public boolean isWaitingForWarehouseApproval() {
        return workflowService.isWaitingForWarehouseApproval(this.getSelected());
    }

    /**
     * Merkezi workflow üzerinden durum geçişi uygular. XHTML bu metodu çağırır;
     * durum doğrudan değiştirilmez.
     */
    public void applyTransition(EnumOrtTalepIslem action) {
        if (this.getSelected() == null || this.getSelected().getId() == null || action == null) {
            FacesUtil.addErrorMessage("Geçersiz talep veya işlem.");
            return;
        }
        try {
            boolean attendanceAction = action == EnumOrtTalepIslem.ON_MUAYENEYE_GEL
                    || action == EnumOrtTalepIslem.OLCU_AL
                    || action == EnumOrtTalepIslem.PROVA_YAP;
            if (attendanceAction) {
                workflowService.applyAttendanceTransition(this.getSelected().getId(), action, transitionDescription);
            } else if (action == EnumOrtTalepIslem.PERSONEL_ATA
                    || action == EnumOrtTalepIslem.FORMLARI_TAMAMLA
                    || action == EnumOrtTalepIslem.PROJE_BELIRLE) {
                // Personel / SUT / proje seçimleri workflow alanlarıdır; güvenli güncelleme.
                service.updateWorkflowFields(this.getSelected());
                workflowService.applyTransition(this.getSelected().getId(), action, transitionDescription);
            } else if (action == EnumOrtTalepIslem.TESLIM_ET) {
                // Teslimat kaydı + stok çıkışı + durum geçişi tek transaction'da.
                workflowService.applyDelivery(this.getSelected().getId(), transitionDescription);
            } else {
                workflowService.applyTransition(this.getSelected().getId(), action, transitionDescription);
            }
            OrtTalep updated = service.find(this.getSelected().getId());
            this.setSelected(updated);
            FacesUtil.addSuccessMessage(action.getDisplayValue() + " " + FacesUtil.message(Constants.KAYIT_GUNCELLENDI));
            transitionDescription = null;
            readInfo();
        } catch (EJBException ex) {
            FacesUtil.addErrorMessage(ex.getCause().getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * 10 ana aşamalı yatay süreç göstergesi verisi.
     */
    public List<OrtTalepAsama> getProcessStages() {
        List<OrtTalepAsama> stages = new ArrayList<>();
        if (this.getSelected() == null || this.getSelected().getDurum() == null) {
            return stages;
        }

        EnumOrtTalepDurumu current = this.getSelected().getDurum();
        boolean rejected = current == EnumOrtTalepDurumu.TALEP_REDDEDILDI
                || (current == EnumOrtTalepDurumu.KAPATILDI && hasStatusInHistory(EnumOrtTalepDurumu.TALEP_REDDEDILDI));
        int currentIndex = findPhaseIndex(current);

        for (int i = 0; i < PHASE_LABELS.length; i++) {
            String label = PHASE_LABELS[i];
            String css;
            String detail;

            if (rejected && i == 3) {
                label = "Talep Reddedildi";
                css = "rejected";
                detail = formatHistoryDate(EnumOrtTalepDurumu.TALEP_REDDEDILDI);
            } else if (rejected && i > 3) {
                css = "pending";
                detail = "";
            } else if (i == 0) {
                css = "done";
                detail = formatDate(this.getSelected().getEklemeTarihi());
            } else if (i == 1) {
                css = "done";
                detail = formatDate(this.getSelected().getTalepTarihi());
            } else if (i < currentIndex) {
                css = "done";
                detail = formatPhaseDate(PHASE_STATUSES.get(i));
            } else if (i == currentIndex) {
                css = "current";
                detail = current == EnumOrtTalepDurumu.KAPATILDI ? "kapatıldı" : "şu an";
            } else {
                css = "pending";
                detail = "";
            }
            stages.add(new OrtTalepAsama(label, css, detail, i + 1));
        }
        return stages;
    }

    /**
     * Tek ana işlem (mockup'taki "Sıradaki İşlem" kartı).
     *
     * Yetki filtresi uygulanmış {@code availableActions} boşsa (ör. detay yenilenmeden
     * önceki bir ajax çağrısında set edilmemişse) mevcut durumdan hareketle işlem
     * yeniden hesaplanır. Böylece randevu oluşturulduktan sonra akış ekranda kilitli
     * görünmez; sunucu tarafındaki yetki/guard kontrolleri yine geçerlidir.
     */
    public EnumOrtTalepIslem getPrimaryAction() {
        if (isProcessComplete()) {
            return null;
        }
        EnumOrtTalepTipi tipi = this.getSelected() == null ? null : this.getSelected().getTalepTipi();
        EnumOrtTalepDurumu durum = this.getSelected() == null ? null : this.getSelected().getDurum();

        Set<EnumOrtTalepIslem> actions = availableActions;
        if (actions == null || actions.isEmpty()) {
            // Yetki ve depo kilidi kontrolleriyle yeniden hesapla.
            Set<EnumOrtTalepIslem> filtered = EnumOrtTalepIslem.getActions(durum).stream()
                    .filter(a -> workflowService.isActionAvailable(this.getSelected(), a))
                    .collect(java.util.stream.Collectors.toCollection(
                            () -> EnumSet.noneOf(EnumOrtTalepIslem.class)));
            actions = filtered;
        }
        if (actions.isEmpty()) {
            return null;
        }
        if (tipi == EnumOrtTalepTipi.UCRETLI && actions.contains(EnumOrtTalepIslem.UCRET_BELIRLE)) {
            return EnumOrtTalepIslem.UCRET_BELIRLE;
        }
        if (tipi != EnumOrtTalepTipi.UCRETLI && actions.contains(EnumOrtTalepIslem.PROJE_BELIRLE)) {
            return EnumOrtTalepIslem.PROJE_BELIRLE;
        }
        return actions.iterator().next();
    }

    /**
     * Ana işlem dışında kalan, daha az kullanılan işlemler.
     */
    public Set<EnumOrtTalepIslem> getSecondaryActions() {
        EnumOrtTalepIslem primary = getPrimaryAction();
        if (primary == null) {
            return Set.of();
        }
        return availableActions.stream()
                .filter(a -> a != primary)
                .collect(java.util.stream.Collectors.toCollection(() -> EnumSet.noneOf(EnumOrtTalepIslem.class)));
    }

    /**
     * Proje seçim alanı hem proje kapsamlı taleplerde (ana işlem) hem de
     * ücretli taleplerde (isteğe bağlı ikincil işlem) gösterilir.
     */
    public boolean isProjeSecimiVisible() {
        return getPrimaryAction() == EnumOrtTalepIslem.PROJE_BELIRLE
                || getSecondaryActions().contains(EnumOrtTalepIslem.PROJE_BELIRLE);
    }

    public int getAppointmentCount() {
        if (this.getSelected() == null) {
            return 0;
        }
        return ortRandevuService.findByTalep(this.getSelected()).size();
    }

    private static final String[] PHASE_LABELS = {
            "Kişi Kaydı", "Talep Oluşturma", "Ön Muayene", "Değerlendirme", "Ücret / Proje",
            "Ölçü", "Belgeler", "Malzeme", "Prova", "Teslimat"
    };

    private static final List<Set<EnumOrtTalepDurumu>> PHASE_STATUSES = List.of(
            EnumSet.noneOf(EnumOrtTalepDurumu.class),
            EnumSet.of(EnumOrtTalepDurumu.YENI_TALEP),
            EnumSet.of(EnumOrtTalepDurumu.ON_MUAYENE_RANDEVUSU_BEKLIYOR, EnumOrtTalepDurumu.ON_MUAYENEYE_GELDI),
            EnumSet.of(EnumOrtTalepDurumu.PERSONEL_ATAMASI_BEKLIYOR, EnumOrtTalepDurumu.DEGERLENDIRME_BEKLIYOR, EnumOrtTalepDurumu.TALEP_REDDEDILDI),
            EnumSet.of(EnumOrtTalepDurumu.UCRET_BELIRLEME_BEKLIYOR, EnumOrtTalepDurumu.SOZLESME_BEKLIYOR,
                    EnumOrtTalepDurumu.ODEME_BEKLIYOR, EnumOrtTalepDurumu.PROJE_BELIRLENDI),
            EnumSet.of(EnumOrtTalepDurumu.OLCU_RANDEVUSU_BEKLIYOR, EnumOrtTalepDurumu.OLCU_ALINDI),
            EnumSet.of(EnumOrtTalepDurumu.FORMLAR_BEKLENIYOR, EnumOrtTalepDurumu.DOKTOR_RAPORU_BEKLENIYOR,
                    EnumOrtTalepDurumu.RECETE_BEKLENIYOR),
            EnumSet.of(EnumOrtTalepDurumu.MALZEME_TALEBI_BEKLIYOR, EnumOrtTalepDurumu.DEPO_ONAYI_BEKLIYOR),
            EnumSet.of(EnumOrtTalepDurumu.PROVA_RANDEVUSU_BEKLIYOR, EnumOrtTalepDurumu.PROVA_YAPILDI,
                    EnumOrtTalepDurumu.YENIDEN_PROVA_BEKLIYOR),
            EnumSet.of(EnumOrtTalepDurumu.TESLIMAT_BEKLIYOR, EnumOrtTalepDurumu.TESLIM_EDILDI,
                    EnumOrtTalepDurumu.TAMAMLANDI, EnumOrtTalepDurumu.KAPATILDI)
    );

    private int findPhaseIndex(EnumOrtTalepDurumu durum) {
        int index = PHASE_STATUSES.size() - 1;
        for (int i = 0; i < PHASE_STATUSES.size(); i++) {
            if (PHASE_STATUSES.get(i).contains(durum)) {
                index = i;
                break;
            }
        }
        // PROJE_BELIRLENDI ve ODEME_BEKLIYOR hâlâ "Ücret / Proje" aşamasının
        // içindedir. Burada sonraki aşamaya (Ölçü) geçiş yapılırsa stepper bu
        // aşamayı atlamış gibi görünür; mevcut adım doğru gösterilmelidir.
        return index;
    }

    private boolean hasStatusInHistory(EnumOrtTalepDurumu durum) {
        return statusHistory != null && statusHistory.stream()
                .anyMatch(g -> g.getYeniDurum() == durum);
    }

    private String formatPhaseDate(Set<EnumOrtTalepDurumu> statuses) {
        if (statusHistory == null) {
            return "";
        }
        return statusHistory.stream()
                .filter(g -> statuses.contains(g.getYeniDurum()))
                .map(OrtTalepDurumGecmisi::getIslemTarihi)
                .filter(java.util.Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .map(this::formatDate)
                .orElse("");
    }

    private String formatHistoryDate(EnumOrtTalepDurumu durum) {
        return formatPhaseDate(EnumSet.of(durum));
    }

    private String formatDate(LocalDateTime tarih) {
        if (tarih == null) {
            return "";
        }
        return DateUtil.localdateTimeToString(tarih, FacesUtil.message("tarihFormat"));
    }

    /**
     * Randevu gerektiren adımlarda: seçilen tarihle randevu oluşturur．
     * Randevu kaydedildiğinde OrtRandevuController z workflow geçişini yapar;
     * burada sadece dialogu hazırlarız.
     */
    public void prepareAppointmentForAction(EnumOrtTalepIslem action) {
        if (this.getSelected() == null || action == null) {
            FacesUtil.addErrorMessage("Geçersiz talep veya işlem.");
            return;
        }
        // İlgili randevu tipini ortRandevuController üzerinden hazırla.
        PrimeFaces.current().executeScript("PF('OrtRandevuCreateDialog').show()");
    }

    /**
     * Geri dönüş seçenekleri.
     */
    public Set<EnumOrtTalepDurumu> getBackOptions() {
        if (this.getSelected() == null) {
            return Set.of();
        }
        return workflowService.getBackOptions(this.getSelected());
    }

    /**
     * Seçilen adıma geri döner.
     */
    public void goBack(EnumOrtTalepDurumu hedefDurum) {
        if (this.getSelected() == null || this.getSelected().getId() == null || hedefDurum == null) {
            FacesUtil.addErrorMessage("Geçersiz talep veya hedef durum.");
            return;
        }
        try {
            workflowService.goBack(this.getSelected().getId(), hedefDurum, transitionDescription);
            this.setSelected(service.find(this.getSelected().getId()));
            FacesUtil.successMessage("Talep " + hedefDurum.getDisplayValue() + " adımına geri alındı.");
            transitionDescription = null;
            readInfo();
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void selectOrtKisi(SelectEvent<OrtKisi> event) {
        OrtKisi ortKisi = event.getObject();
        if (this.getSelected() != null) {
            this.getSelected().setOrtKisi(ortKisi);
        }
    }

    public void closeTalepSelection(OrtTalep ortTalep) {
        PrimeFaces.current().dialog().closeDynamic(ortTalep);
    }

    public void onRowDblSelect(SelectEvent<OrtTalep> event) {
        closeTalepSelection(event.getObject());
    }
}
