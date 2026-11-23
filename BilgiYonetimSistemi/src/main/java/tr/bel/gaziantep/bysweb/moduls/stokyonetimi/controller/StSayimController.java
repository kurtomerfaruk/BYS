package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.ejb.EJBException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.CellEditEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStSayimDurum;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StSayim;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StSayimKalem;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBarkod;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StSayimService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokBarkodService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokService;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 10:45
 */
@Named
@ViewScoped
@Slf4j
public class StSayimController extends AbstractController<StSayim> {

    @Serial
    private static final long serialVersionUID = 1202600117621L;

    @Inject
    private StSayimService sayimService;
    @Inject
    private FilterOptionService filterOptionService;
    @Inject
    private StStokBarkodService stStokBarkodService;
    @Inject
    private StStokService stStokService;

    @Getter
    @Setter
    private StSayimKalem kalem = new StSayimKalem();

    @Getter
    @Setter
    private String barcodeInput;
    @Getter
    @Setter
    private BigDecimal countQuantity = BigDecimal.ONE;
    @Getter
    @Setter
    private boolean duplicateCount;

    private final Deque<CountSnapshot> undoStack = new ArrayDeque<>();

    public StSayimController() {
        super(StSayim.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        if (key == EnumSyFiltreAnahtari.STSAYIM_DURUMU) {
            return filterOptionService.getStSayimDurumus();
        }
        return Collections.emptyList();
    }

    @Override
    public void resetParents() {
        barcodeInput = null;
        countQuantity = BigDecimal.ONE;
        duplicateCount = false;
        undoStack.clear();
    }

    @Override
    public StSayim prepareCreate(ActionEvent event) {
        StSayim yeni = super.prepareCreate(event);
        if (yeni != null) {
            yeni.setBaslangicTarihi(LocalDateTime.now());
            yeni.setDurum(EnumStSayimDurum.TASLAK);
            yeni.setKalemler(new ArrayList<>());
        }
        kalem = new StSayimKalem();
        resetParents();
        return yeni;
    }

    public void addKalem() {
        if (getSelected() == null) {
            return;
        }
        if (kalem.getStStok() == null || kalem.getStStok().getId() == null) {
            FacesUtil.addExclamationMessage("Sayım kalemi için stok seçin.");
            return;
        }
        if (getSelected().getKalemler() == null) {
            getSelected().setKalemler(new ArrayList<>());
        }
        kalem.setStSayim(getSelected());
        if (kalem.getSistemMiktari() == null) {
            kalem.setSistemMiktari(BigDecimal.ZERO);
        }
        getSelected().getKalemler().add(kalem);
        kalem = new StSayimKalem();
        FacesUtil.addSuccessMessage(Constants.KAYIT_EKLENDI);
    }

    public void removeKalem(StSayimKalem item) {
        if (getSelected() == null || item == null) {
            return;
        }
        getSelected().getKalemler().remove(item);
        FacesUtil.addSuccessMessage(Constants.KAYIT_SILINDI);
    }


    /**
     * Kalem sayım durumunu metin olarak döndürür.
     */
    public String getItemStatus(StSayimKalem item) {
        if (item == null) {
            return "";
        }
        if (item.isSayilmadi()) {
            return FacesUtil.message("sayilmadi");
        }
        if (item.getSayilanMiktar() != null) {
            return FacesUtil.message("sayildi");
        }
        return FacesUtil.message("bekliyor");
    }

    private StStok findStock(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String code = value.trim();
        StStokBarkod barkod = stStokBarkodService.findByBarkod(code);
        if (barkod != null && barkod.getStStok() != null) {
            return barkod.getStStok();
        }
        return stStokService.findByKod(code);
    }

    private StSayimKalem findCountItem(StStok stok) {
        if (getSelected() == null || getSelected().getKalemler() == null || stok == null || stok.getId() == null) {
            return null;
        }
        return getSelected().getKalemler().stream()
                .filter(item -> item.getStStok() != null && item.getStStok().getId() != null)
                .filter(item -> item.getStStok().getId().equals(stok.getId()))
                .findFirst()
                .orElse(null);
    }

    private void pushUndo(StSayimKalem item, boolean added) {
        undoStack.push(new CountSnapshot(item, item.getSayilanMiktar(), item.isSayilmadi(), added));
    }

    /**
     * Hızlı okutma modunda barkod/stok kodu ile sayılan miktarı kaydeder.
     */
    public void addCountFromBarcode() {
        applyCount(false);
    }

    /**
     * Tekrar okutulan barkod için yeni miktarı mevcut sayılan miktara ekler.
     */
    public void addToExistingCount() {
        if (getSelected() == null) {
            return;
        }
        StStok stok = findStock(barcodeInput);
        if (stok == null) {
            FacesUtil.addExclamationMessage("Bu barkod/kod ile kayıtlı stok bulunamadı.");
            return;
        }
        StSayimKalem item = findCountItem(stok);
        if (item == null) {
            item = sayimService.addOrGetCountItem(getSelected(), stok);

            if (item != null) {
                getSelected().getKalemler().add(item);
            }
        }
        if (item == null) {
            FacesUtil.addExclamationMessage("Sayım kalemi oluşturulamadı.");
            return;
        }
        BigDecimal eklenecek = countQuantity == null ? BigDecimal.ZERO : countQuantity;
        BigDecimal mevcut = item.getSayilanMiktar() == null ? BigDecimal.ZERO : item.getSayilanMiktar();
        BigDecimal yeni = mevcut.add(eklenecek);
        pushUndo(item, false);
        try {
            StSayimKalem saved = sayimService.saveCountValue(snapshotWithQuantity(item, yeni, false));
            item.setSayilanMiktar(saved.getSayilanMiktar());
            item.setSayilmadi(saved.isSayilmadi());
            item.setFark(saved.getFark());
            barcodeInput = null;
            countQuantity = BigDecimal.ONE;
            duplicateCount = false;
            FacesUtil.addSuccessMessage(Constants.KAYIT_EKLENDI);
        } catch (EJBException ex) {
            undoStack.pop();
            FacesUtil.addExclamationMessage(ex.getCause().getMessage());
        }
    }

    /**
     * Aynı barkod tekrar okutulduğunda mevcut değeri üzerine yazar.
     */
    public void overwriteCount() {
        applyCount(true);
    }

    private void applyCount(boolean overwrite) {
        if (getSelected() == null) {
            return;
        }
        StStok stok = findStock(barcodeInput);
        if (stok == null) {
            FacesUtil.addExclamationMessage("Bu barkod/kod ile kayıtlı stok bulunamadı.");
            duplicateCount = false;
            return;
        }
        StSayimKalem item = findCountItem(stok);
        if (item == null) {
            item = sayimService.addOrGetCountItem(getSelected(), stok);

            if (item != null) {
                getSelected().getKalemler().add(item);
            }
        }
        if (item == null) {
            FacesUtil.addExclamationMessage("Sayım kalemi oluşturulamadı.");
            duplicateCount = false;
            return;
        }
        if (!overwrite && (item.getSayilanMiktar() != null || item.isSayilmadi())) {
            duplicateCount = true;
            FacesUtil.addExclamationMessage("Bu stok zaten sayıldı. Değiştirmek için 'Değiştir' düğmesine basın.");
            return;
        }
        BigDecimal qty = countQuantity == null ? BigDecimal.ZERO : countQuantity;
        if (qty.compareTo(BigDecimal.ZERO) < 0) {
            FacesUtil.addExclamationMessage("Sayılan miktar negatif olamaz.");
            return;
        }
        pushUndo(item, false);
        try {
            StSayimKalem saved = sayimService.saveCountValue(snapshotWithQuantity(item, qty, false));
            item.setSayilanMiktar(saved.getSayilanMiktar());
            item.setSayilmadi(saved.isSayilmadi());
            item.setFark(saved.getFark());
            barcodeInput = null;
            countQuantity = BigDecimal.ONE;
            duplicateCount = false;
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
        } catch (EJBException ex) {
            undoStack.pop();
            FacesUtil.addExclamationMessage(ex.getMessage());
        }
    }

    private StSayimKalem snapshotWithQuantity(StSayimKalem item, BigDecimal qty, boolean notCounted) {
        StSayimKalem snapshot = new StSayimKalem();
        snapshot.setId(item.getId());
        snapshot.setStSayim(item.getStSayim());
        snapshot.setSistemMiktari(item.getSistemMiktari());
        snapshot.setSayilanMiktar(notCounted ? null : qty);
        snapshot.setSayilmadi(notCounted);
        return snapshot;
    }

    public void undoLastCount() {
        if (getSelected() == null || undoStack.isEmpty()) {
            FacesUtil.addExclamationMessage("Geri alınacak işlem yok.");
            return;
        }
        CountSnapshot snap = undoStack.pop();
        StSayimKalem item = findItemById(snap.getItemId());
        if (item == null) {
            return;
        }
        try {
            if (snap.isAdded()) {
                sayimService.removeCountItem(item);
                getSelected().getKalemler().remove(item);
            } else {
                StSayimKalem restored = sayimService.restoreCountValue(item, snap.getQuantity(), snap.isNotCounted());
                item.setSayilanMiktar(restored.getSayilanMiktar());
                item.setSayilmadi(restored.isSayilmadi());
                item.setFark(restored.getFark());
            }
            barcodeInput = null;
            countQuantity = BigDecimal.ONE;
            duplicateCount = false;
        } catch (Exception ex) {
            log.error("Sayım geri alma hatası", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    private StSayimKalem findItemById(Integer id) {
        if (getSelected() == null || getSelected().getKalemler() == null || id == null) {
            return null;
        }
        return getSelected().getKalemler().stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst()
                .orElse(null);
    }

    public void markNotCounted(StSayimKalem item) {
        if (item == null) {
            return;
        }
        pushUndo(item, false);
        try {
            StSayimKalem saved = sayimService.markNotCounted(item);
            item.setSayilmadi(saved.isSayilmadi());
            item.setSayilanMiktar(saved.getSayilanMiktar());
            item.setFark(saved.getFark());
        } catch (IllegalArgumentException ex) {
            undoStack.pop();
            FacesUtil.addExclamationMessage(ex.getMessage());
        }
    }

    /**
     * Liste modunda hücre düzenleme sonrası çağrılır.
     */
    public void onCountCellEdit(CellEditEvent<BigDecimal> event) {
        if (event == null) {
            return;
        }
        Object rowData = event.getRowData();
        if (!(rowData instanceof StSayimKalem item)) {
            return;
        }
        try {
            StSayimKalem saved = sayimService.saveCountValue(item);
            item.setSayilmadi(saved.isSayilmadi());
            item.setFark(saved.getFark());
        } catch (EJBException ex) {
            FacesUtil.addExclamationMessage(ex.getCause().getMessage());
        }
    }


    public int getCountedCount() {
        if (getSelected() == null || getSelected().getKalemler() == null) {
            return 0;
        }
        return (int) getSelected().getKalemler().stream()
                .filter(item -> !item.isSayilmadi() && item.getSayilanMiktar() != null)
                .count();
    }

    public int getRemainingCount() {
        if (getSelected() == null || getSelected().getKalemler() == null) {
            return 0;
        }
        return getSelected().getKalemler().size() - getCountedCount();
    }

    public int getDifferenceCount() {
        if (getSelected() == null || getSelected().getKalemler() == null) {
            return 0;
        }
        return (int) getSelected().getKalemler().stream()
                .filter(item -> !item.isSayilmadi()
                        && item.getFark() != null
                        && item.getFark().compareTo(BigDecimal.ZERO) != 0)
                .count();
    }

    public int completePercentage() {
        if (getSelected() == null || getSelected().getKalemler() == null) {
            return 0;
        }
        int counted = getCountedCount();
        return (int) ((double) counted / getSelected().getKalemler().size() * 100);
    }

    public BigDecimal getTotalSystem() {
        if (getSelected() == null || getSelected().getKalemler() == null) {
            return BigDecimal.ZERO;
        }
        return getSelected().getKalemler().stream()
                .map(StSayimKalem::getSistemMiktari)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalCounted() {
        if (getSelected() == null || getSelected().getKalemler() == null) {
            return BigDecimal.ZERO;
        }
        return getSelected().getKalemler().stream()
                .filter(item -> !item.isSayilmadi())
                .map(StSayimKalem::getSayilanMiktar)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void saveNew(ActionEvent event) {
        try {
            if (getSelected() == null) {
                return;
            }
            StSayim kayit = sayimService.sayimOlustur(getSelected());
            setSelected(kayit);
            FacesUtil.addSuccessMessage(Constants.KAYIT_EKLENDI);
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.addExclamationMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Sayim olusturulamadi", ex);
            FacesUtil.addErrorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void start() {
        try {
            if (this.getSelected() == null || this.getSelected().getId() == null) {
                throw new IllegalArgumentException("İşlem için bir sayım seçin.");
            }

            if (this.getSelected().getDurum() != EnumStSayimDurum.TASLAK) {
                throw new IllegalArgumentException("Bu sayım başlatılamaz");
            }

            this.getSelected().setDurum(EnumStSayimDurum.SAYIMDA);
            if (this.getSelected().getBaslangicTarihi() == null) {
                this.getSelected().setBaslangicTarihi(LocalDateTime.now());
            }
            this.getSelected().setSayanSyKullanici(this.getSyKullanici());
            sayimService.edit(this.getSelected());
            FacesUtil.addSuccessMessage("Sayım başlatıldı.");
        } catch (IllegalArgumentException ex) {
            FacesUtil.addExclamationMessage(ex.getCause().getMessage());
        } catch (Exception ex) {
            log.error("Sayim baslatilamadi", ex);
            FacesUtil.addErrorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void finish() {
        try {
            sayimService.finish(getSelected(), this.getSyKullanici());
            FacesUtil.addSuccessMessage("Sayım tamamlandı");
            setItems(null);
            setLazyItems(null);
        } catch (EJBException ex) {
            FacesUtil.addExclamationMessage(ex.getCause().getMessage());
        } catch (Exception ex) {
            log.error("Sayim tamamlanamadi", ex);
            FacesUtil.addErrorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void confirm() {
        try {
            sayimService.confirm(getSelected(), this.getSyKullanici());
            FacesUtil.addSuccessMessage("Bu sayım onaylandı.");
            setItems(null);
            setLazyItems(null);
        } catch (EJBException ex) {
            FacesUtil.addExclamationMessage(ex.getCause().getMessage());
        } catch (Exception ex) {
            log.error("Sayim onaylanamadi", ex);
            FacesUtil.addErrorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void cancel() {
        try {
            if (this.getSelected() == null || this.getSelected().getId() == null) {
                throw new IllegalArgumentException("Bu sayım başlatılamaz");
            }
            if (this.getSelected().getDurum() == EnumStSayimDurum.TAMAMLANDI) {
                throw new IllegalArgumentException("Bu sayım iptal edilemez");
            }
            this.getSelected().setDurum(EnumStSayimDurum.IPTAL);
            sayimService.edit(this.getSelected());
            FacesUtil.addSuccessMessage("Bu sayım iptal edildi.");
        } catch (IllegalArgumentException ex) {
            FacesUtil.addExclamationMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Sayim iptal edilemedi", ex);
            FacesUtil.addErrorMessage(Constants.HATA_OLUSTU);
        }
    }

    private static class CountSnapshot {
        private final Integer itemId;
        private final BigDecimal quantity;
        private final boolean notCounted;
        private final boolean added;

        CountSnapshot(StSayimKalem item, BigDecimal quantity, boolean notCounted, boolean added) {
            this.itemId = item == null ? null : item.getId();
            this.quantity = quantity;
            this.notCounted = notCounted;
            this.added = added;
        }

        Integer getItemId() {
            return itemId;
        }

        BigDecimal getQuantity() {
            return quantity;
        }

        boolean isNotCounted() {
            return notCounted;
        }

        boolean isAdded() {
            return added;
        }
    }
}
