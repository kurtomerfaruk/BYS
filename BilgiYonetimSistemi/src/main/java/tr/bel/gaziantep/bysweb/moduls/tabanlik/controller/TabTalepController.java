package tr.bel.gaziantep.bysweb.moduls.tabanlik.controller;

import jakarta.ejb.EJBException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumEvetHayir;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.*;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.dto.TabTalepAsama;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.*;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 11:47
 */
@Named
@ViewScoped
@Slf4j
public class TabTalepController extends AbstractController<TabTalep> {

    @Serial
    private static final long serialVersionUID = 3410469348087299704L;

    @Inject
    private TabTalepService service;
    @Inject
    private TabTalepWorkflowService workflowService;
    @Inject
    private TabRandevuService randevuService;
    @Inject
    private TabOlcumService olcumService;
    @Inject
    private TabUretimKararService kararService;
    @Inject
    private TabOdemeService odemeService;
    @Inject
    private TabUretimService uretimService;
    @Inject
    private TabTeslimatService teslimatService;
    @Inject
    private TabBelgeService belgeService;

    @Getter
    @Setter
    private List<String> durumOptions = Collections.emptyList();
    @Getter
    private List<TabTalepAsama> processStages = Collections.emptyList();
    @Getter
    private List<TabTalepHareket> statusHistory = Collections.emptyList();
    @Getter
    private Set<EnumTabTalepIslem> availableActions = EnumSet.noneOf(EnumTabTalepIslem.class);
    @Getter
    private Set<EnumTabTalepDurumu> backOptions = EnumSet.noneOf(EnumTabTalepDurumu.class);
    @Getter
    private List<TabRandevu> randevular = Collections.emptyList();
    @Getter
    private List<TabOlcum> olcumler = Collections.emptyList();
    @Getter
    private List<TabUretimKarar> kararlar = Collections.emptyList();
    @Getter
    private List<TabOdeme> odemeler = Collections.emptyList();
    @Getter
    private List<TabUretim> uretimler = Collections.emptyList();
    @Getter
    private List<TabTeslimat> teslimatlar = Collections.emptyList();
    @Getter
    private List<TabBelge> belgeler = Collections.emptyList();
    @Getter
    @Setter
    private String transitionDescription;
    @Getter
    @Setter
    private EnumTabBelgeTuru belgeTuru;

    /* Detay ekranındaki dinamik işlem alanları */
    @Getter
    @Setter
    private LocalDateTime olcumTarihi = LocalDateTime.now();
    @Getter
    private final TabOlcumDetay sagDetay = TabOlcumDetay.builder().ayakTarafi(EnumTabAyakTarafi.SAG).build();
    @Getter
    private final TabOlcumDetay solDetay = TabOlcumDetay.builder().ayakTarafi(EnumTabAyakTarafi.SOL).build();
    @Getter
    @Setter
    private EnumEvetHayir karar;
    @Getter
    @Setter
    private EnumTabUcretTuru ucretTuru;
    @Getter
    @Setter
    private BigDecimal tutar;
    @Getter
    @Setter
    private LocalDateTime odemeTarihi = LocalDateTime.now();
    @Getter
    @Setter
    private String dekontNo;
    @Getter
    @Setter
    private String ucretsizGerekce;
    @Getter
    @Setter
    private SyKullanici sorumluKullanici;
    @Getter
    @Setter
    private StStok stStok;
    @Getter
    @Setter
    private StDepo stDepo;
    @Getter
    @Setter
    private BigDecimal miktar = BigDecimal.ONE;

    public TabTalepController() {
        super(TabTalep.class);
    }

    @Override
    public TabTalep prepareCreate(ActionEvent event) {
        TabTalep newItem = super.prepareCreate(event);
        newItem.setTalepTarihi(LocalDateTime.now());
        newItem.setDurum(EnumTabTalepDurumu.YENI);
        return newItem;
    }

    @Override
    public void saveNew(ActionEvent event) {
        if (this.getSelected() == null) {
            return;
        }
        if (this.getSelected().getTabKisi() == null) {
            FacesUtil.addErrorMessage("Talep için kişi seçilmelidir.");
            return;
        }
        try {
            service.createTalep(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            if (!isValidationFailed()) {
                this.setItems(null);
                this.setLazyItems(null);
            }
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    @Override
    public void save(ActionEvent event) {
        if (this.getSelected() == null) {
            return;
        }
        try {
            service.updateTalep(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Kişi seçim dialogundan dönen TabKisi kaydı.
     */
    public void secilenTabKisi(SelectEvent<TabKisi> event) {
        TabKisi tabKisi = event.getObject();
        this.getSelected().setTabKisi(tabKisi);
    }


    /**
     * Talep seçim dialogunda seçilen kaydı dialogReturn ile döndürür.
     */
    public void closeSelection(TabTalep tabTalep) {
        PrimeFaces.current().dialog().closeDynamic(tabTalep);
    }

    /**
     * Talep seçim tablosunda satıra çift tıklanınca seçim dialogunu kapatır.
     */
    public void onRowDoubleSelect(SelectEvent<TabTalep> event) {
        if (event != null && event.getObject() != null) {
            closeSelection(event.getObject());
        }
    }


    /**
     * Detay dialogu açılmadan önce tüm aşama verilerini tazeler.
     */
    public void readInfo() {
        processStages = Collections.emptyList();
        statusHistory = Collections.emptyList();
        availableActions = EnumSet.noneOf(EnumTabTalepIslem.class);
        backOptions = EnumSet.noneOf(EnumTabTalepDurumu.class);
        randevular = Collections.emptyList();
        olcumler = Collections.emptyList();
        kararlar = Collections.emptyList();
        odemeler = Collections.emptyList();
        uretimler = Collections.emptyList();
        teslimatlar = Collections.emptyList();
        belgeler = Collections.emptyList();
        transitionDescription = null;

        if (this.getSelected() == null || this.getSelected().getId() == null) {
            return;
        }
        TabTalep taze = service.find(this.getSelected().getId());
        if (taze == null) {
            return;
        }
        this.setSelected(taze);
        statusHistory = service.findHareketByTalep(taze);
        randevular = randevuService.findByTalep(taze);
        olcumler = olcumService.findByTalep(taze);
        kararlar = kararService.findByTalep(taze);
        odemeler = odemeService.findByTalep(taze);
        uretimler = uretimService.findByTalep(taze);
        teslimatlar = teslimatService.findByTalep(taze);
        belgeler = belgeService.findByTalep(taze);

        for (EnumTabTalepIslem action : EnumTabTalepIslem.values()) {
            if (workflowService.isActionAvailable(taze, action)) {
                availableActions.add(action);
            }
        }
        backOptions = getBackOptions();
        processStages = buildProcessStages(taze);
    }

    /**
     * Merkezi workflow üzerinden durum geçişi uygular.
     */
    public void applyTransition(EnumTabTalepIslem action) {
        if (action == null || this.getSelected() == null || this.getSelected().getId() == null) {
            FacesUtil.addErrorMessage("Geçersiz talep veya işlem.");
            return;
        }
        try {
            executeAction(action, this.getSelected().getId(), gerekce());
            FacesUtil.addSuccessMessage(action.getDisplayValue() + " " + FacesUtil.message(Constants.KAYIT_GUNCELLENDI));
            resetActionFields();
            readInfo();
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Yetkili düzeltme işlemi: önceki aşamaya gerekçeli geri dönüş.
     */
    public void goBack(EnumTabTalepDurumu hedefDurum) {
        if (hedefDurum == null || this.getSelected() == null || this.getSelected().getId() == null) {
            FacesUtil.addErrorMessage("Geçersiz geri dönüş işlemi.");
            return;
        }
        try {
            workflowService.goBack(this.getSelected().getId(), hedefDurum, transitionDescription);
            FacesUtil.addSuccessMessage(FacesUtil.message(Constants.KAYIT_GUNCELLENDI));
            readInfo();
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        }
    }

    /**
     * Detay ekranındaki tek ana işlem (mevcut durumda izinli ilk işlem).
     */
    public EnumTabTalepIslem getPrimaryAction() {
        if (isProcessComplete()) {
            return null;
        }
        EnumTabTalepDurumu durum = this.getSelected() == null ? null : this.getSelected().getDurum();
        if (availableActions == null || availableActions.isEmpty()) {
            // Ortez-Protez kalıbı: detay yenilenmeden önceki bir ajax çağrısında
            // set edilmemişse işlemi mevcut durumdan yeniden hesapla.
            EnumSet<EnumTabTalepIslem> filtrelenen = EnumSet.noneOf(EnumTabTalepIslem.class);
            for (EnumTabTalepIslem aday : EnumTabTalepIslem.getActions(durum)) {
                if (workflowService.isActionAvailable(this.getSelected(), aday)) {
                    filtrelenen.add(aday);
                }
            }
            availableActions = filtrelenen;
        }
        if (availableActions.isEmpty()) {
            return null;
        }
        if (durum == EnumTabTalepDurumu.UCRET_BEKLIYOR) {
            boolean ucretsiz = this.getSelected().getUcretTuru() == EnumTabUcretTuru.UCRETSIZ;
            EnumTabTalepIslem beklenen = ucretsiz
                    ? EnumTabTalepIslem.UCRETSIZ_ONAYLA
                    : EnumTabTalepIslem.UCRETLI_ODEME_ONAYLA;
            if (availableActions.contains(beklenen)) {
                return beklenen;
            }
        }
        return availableActions.stream().findFirst().orElse(null);
    }

    /**
     * Ana işlem dışında kalan, daha az kullanılan işlemler.
     */
    public Set<EnumTabTalepIslem> getSecondaryActions() {
        EnumTabTalepIslem birincil = getPrimaryAction();
        if (birincil == null || availableActions == null) {
            return Set.of();
        }
        EnumSet<EnumTabTalepIslem> sonuc = EnumSet.noneOf(EnumTabTalepIslem.class);
        for (EnumTabTalepIslem action : availableActions) {
            if (action != birincil) {
                sonuc.add(action);
            }
        }
        return sonuc;
    }

    public boolean isProcessComplete() {
        EnumTabTalepDurumu durum = this.getSelected() == null ? null : this.getSelected().getDurum();
        return durum != null && durum.isClosed();
    }

    public int getAppointmentCount() {
        return this.getSelected() == null ? 0 : randevuService.findByTalep(this.getSelected()).size();
    }

    /**
     * Geri dönüş seçenekleri (workflow üzerinden).
     */
    public Set<EnumTabTalepDurumu> getBackOptions() {
        return workflowService.getBackOptions(this.getSelected());
    }

    public String getPendingAction() {
        EnumTabTalepDurumu durum = this.getSelected() == null ? null : this.getSelected().getDurum();
        if (durum == null) {
            return "-";
        }
        return switch (durum) {
            case YENI -> "Ölçüm randevusu verilecek";
            case RANDEVU_BEKLIYOR -> "Ölçüm randevusu gerçekleştirilecek";
            case OLCUM_BEKLIYOR -> "Ayak ölçüleri girilecek";
            case URETIM_KARARI_BEKLIYOR -> "Üretim kararı verilecek";
            case UCRET_BEKLIYOR -> "Ücret/ödeme onayı yapılacak";
            case URETIM_BEKLIYOR -> "Üretime başlanacak";
            case URETIMDE -> "Üretim tamamlanacak";
            case TESLIMAT_RANDEVUSU_BEKLIYOR -> "Teslimat randevusu verilecek";
            case TESLIMAT_BEKLIYOR -> "Teslimat yapılacak";
            case TAMAMLANDI -> "Talep tamamlandı";
            case IPTAL_EDILDI, UYGUN_BULUNMADI -> "Talep kapatıldı";
        };
    }

    private void resetActionFields() {
        transitionDescription = null;
        karar = null;
        ucretTuru = null;
        tutar = null;
        dekontNo = null;
        ucretsizGerekce = null;
        sorumluKullanici = null;
        stStok = null;
        stDepo = null;
        miktar = BigDecimal.ONE;
    }

    /**
     * Mockup'taki "Sıradaki İşlem" kartındaki tek butondan ilgili işlemi uygular.
     */
    public void applyPrimaryAction() {
        EnumTabTalepIslem action = getPrimaryAction();
        if (action == null || this.getSelected() == null || this.getSelected().getId() == null) {
            FacesUtil.addErrorMessage("Bu aşamada uygulanabilir işlem bulunmuyor.");
            return;
        }
        Integer talepId = this.getSelected().getId();
        String note = gerekce();
        try {
            executeAction(action, talepId, note);
            FacesUtil.addSuccessMessage(action.getDisplayValue() + " " + FacesUtil.message(Constants.KAYIT_GUNCELLENDI));
            resetActionFields();
            readInfo();
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Talep detay ekranından belge yükler (tür ve boyut kontrolü serviste yapılır).
     */
    public void uploadBelge(FileUploadEvent event) {
        if (this.getSelected() == null || this.getSelected().getId() == null) {
            FacesUtil.addErrorMessage("Belge yüklemek için talep seçilmelidir.");
            return;
        }
        if (belgeTuru == null) {
            FacesUtil.addErrorMessage("Belge türü seçilmelidir.");
            return;
        }
        try {
            belgeService.upload(this.getSelected(), belgeTuru, event.getFile(), transitionDescription);
            FacesUtil.addSuccessMessage(FacesUtil.message(Constants.KAYIT_EKLENDI));
            readInfo();
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Tüm işlem giriş noktaları (ana buton ve "daha fazla işlem" linkleri) aynı
     * uzmanlaşmış servis metotlarına yönlendirilir. Böylece karar, ödeme onayı,
     * üretim ve teslimat yan etkileri hiçbir yoldan atlanamaz.
     */
    private void executeAction(EnumTabTalepIslem action, Integer talepId, String note) {
        switch (action) {
            case RANDEVU_VER -> throw new IllegalArgumentException("Ölçüm randevusu randevu ekranından verilmelidir.");
            case OLCUM_GIR -> {
                TabOlcum olcum = TabOlcum.builder()
                        .tabTalep(this.getSelected())
                        .olcumTarihi(olcumTarihi == null ? LocalDateTime.now() : olcumTarihi)
                        .aciklama(note)
                        .detayList(new ArrayList<>(List.of(sagDetay, solDetay)))
                        .build();
                workflowService.createOlcumAndTransition(olcum);
            }
            case OLCUM_TAMAMLA -> workflowService.applyTransition(talepId, action, note);
            case KARAR_EVET, KARAR_HAYIR -> workflowService.applyDecision(talepId, karar, note);
            case UCRETLI_ODEME_ONAYLA -> workflowService.applyChargeAndApprove(
                    talepId, EnumTabUcretTuru.UCRETLI, tutar, odemeTarihi, null, dekontNo, null, note);
            case UCRETSIZ_ONAYLA -> workflowService.applyChargeAndApprove(
                    talepId, EnumTabUcretTuru.UCRETSIZ, null, null, null, null, ucretsizGerekce, note);
            case URETIM_BASLAT -> workflowService.startProduction(
                    talepId,
                    sorumluKullanici == null ? null : sorumluKullanici.getId(),
                    stStok == null ? null : stStok.getId(),
                    stDepo == null ? null : stDepo.getId(),
                    miktar, note);
            case URETIM_TAMAMLA -> workflowService.completeProduction(talepId, note);
            case TESLIMAT_RANDEVUSU_VER -> throw new IllegalArgumentException("Teslimat randevusu randevu ekranından verilmelidir.");
            case TESLIM_ET -> workflowService.applyDelivery(talepId, note);
            default -> workflowService.applyTransition(talepId, action, note);
        }
    }

    private String gerekce() {
        if (!StringUtil.isBlank(transitionDescription)) {
            return transitionDescription;
        }
        return StringUtil.isBlank(ucretsizGerekce) ? null : ucretsizGerekce;
    }

    private List<TabTalepAsama> buildProcessStages(TabTalep talep) {
        String[] adlar = {"Talep", "Randevu", "Ölçüm", "Karar", "Ücret", "Üretim", "Teslimat", "Tamamlandı"};
        List<Set<EnumTabTalepDurumu>> durumKumeleri = List.of(
                EnumSet.of(EnumTabTalepDurumu.YENI),
                EnumSet.of(EnumTabTalepDurumu.RANDEVU_BEKLIYOR),
                EnumSet.of(EnumTabTalepDurumu.OLCUM_BEKLIYOR),
                EnumSet.of(EnumTabTalepDurumu.URETIM_KARARI_BEKLIYOR),
                EnumSet.of(EnumTabTalepDurumu.UCRET_BEKLIYOR),
                EnumSet.of(EnumTabTalepDurumu.URETIM_BEKLIYOR, EnumTabTalepDurumu.URETIMDE),
                EnumSet.of(EnumTabTalepDurumu.TESLIMAT_RANDEVUSU_BEKLIYOR, EnumTabTalepDurumu.TESLIMAT_BEKLIYOR),
                EnumSet.of(EnumTabTalepDurumu.TAMAMLANDI)
        );
        EnumTabTalepDurumu durum = talep.getDurum();
        int aktifIndex = -1;
        for (int i = 0; i < durumKumeleri.size(); i++) {
            if (durumKumeleri.get(i).contains(durum)) {
                aktifIndex = i;
                break;
            }
        }
        boolean rejected = durum == EnumTabTalepDurumu.UYGUN_BULUNMADI
                || durum == EnumTabTalepDurumu.IPTAL_EDILDI;
        List<TabTalepAsama> stages = new ArrayList<>();
        for (int i = 0; i < adlar.length; i++) {
            String ad = adlar[i];
            String css;
            String detay;
            if (rejected && i == 3) {
                ad = durum.getDisplayValue();
                css = "rejected";
                detay = formatPhaseDate(durumKumeleri.get(i));
            } else if (rejected && i > 3) {
                css = "pending";
                detay = "";
            } else if (i == 0) {
                css = "done";
                detay = formatDate(talep.getEklemeTarihi());
            } else if (i == 1) {
                css = "done";
                detay = formatDate(talep.getTalepTarihi());
            } else if (i < aktifIndex) {
                css = "done";
                detay = formatPhaseDate(durumKumeleri.get(i));
            } else if (i == aktifIndex) {
                css = "current";
                detay = "şu an";
            } else {
                css = "pending";
                detay = "";
            }
            stages.add(new TabTalepAsama(ad, css, detay, i + 1));
        }
        return stages;
    }

    private String formatPhaseDate(Set<EnumTabTalepDurumu> durumlar) {
        if (statusHistory == null) {
            return "";
        }
        return statusHistory.stream()
                .filter(h -> durumlar.contains(h.getYeniDurum()))
                .map(TabTalepHareket::getIslemTarihi)
                .filter(java.util.Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .map(this::formatDate)
                .orElse("");
    }

    private String formatDate(LocalDateTime tarih) {
        if (tarih == null) {
            return "";
        }
        return DateUtil.localdateTimeToString(tarih, FacesUtil.message("tarihFormat"));
    }
}
