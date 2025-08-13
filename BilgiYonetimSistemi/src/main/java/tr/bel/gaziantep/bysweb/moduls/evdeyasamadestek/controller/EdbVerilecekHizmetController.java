package tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.ResponsiveOption;
import org.primefaces.model.file.UploadedFile;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.evdeyasamadestek.EnumEdbDurum;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Function;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbPersonel;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbVerilecekHizmet;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbVerilecekHizmetPersonel;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbVerilecekHizmetResim;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.service.EdbVerilecekHizmetResimService;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.service.EdbVerilecekHizmetService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.07.2025 10:31
 */
@Named
@ViewScoped
@Slf4j
public class EdbVerilecekHizmetController extends AbstractController<EdbVerilecekHizmet> {
    @Serial
    private static final long serialVersionUID = 460229038573830168L;

    @Inject
    private EdbVerilecekHizmetService service;
    @Inject
    private EdbVerilecekHizmetResimService edbVerilecekHizmetResimService;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private int activeIndex = 0;
    @Getter
    @Setter
    private List<ResponsiveOption> responsiveOptions;
    @Getter
    @Setter
    private List<EdbPersonel> edbPersonelList = new ArrayList<>();

    public EdbVerilecekHizmetController() {
        super(EdbVerilecekHizmet.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case EDBBASVURU_DURUMU -> {
                return filterOptionService.getEdbBasvuruDurumus();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @PostConstruct
    @Override
    public void init() {
        readColumns();
        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("1024px", 5));
        responsiveOptions.add(new ResponsiveOption("768px", 3));
        responsiveOptions.add(new ResponsiveOption("560px", 1));
    }

    public void createService() {
        try {
            List<EdbVerilecekHizmet> hizmets = service.findByDurumMaxDate(EnumEdbDurum.TAMAMLANDI);
            List<EdbVerilecekHizmet> bekleyenHizmets = service.findByDurum(EnumEdbDurum.BEKLIYOR);
            for (EdbVerilecekHizmet hizmet : hizmets) {
                if (!bekleyenHizmets.contains(hizmet)) {
                    LocalDate today = LocalDate.now();
                    if (today.equals(hizmet.getHizmetTarihi()) || today.isAfter(hizmet.getHizmetTarihi())) {
                        EdbVerilecekHizmet newHizmet = new EdbVerilecekHizmet();
                        newHizmet.setDurum(EnumEdbDurum.BEKLIYOR);
                        newHizmet.setEdbHizmetPlanlama(hizmet.getEdbHizmetPlanlama());
                        newHizmet.setTarih(hizmet.getTarih());
                        newHizmet.setHizmetTarihi(DateUtil.addDay(hizmet.getEdbHizmetPlanlama().getPeriyot()).toLocalDate());
                        service.create(newHizmet);
                    }
                }
            }
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void readInfo() {
        if (this.getSelected() != null) {
            try {
                this.getSelected().getEdbVerilecekHizmetPersonelList().stream()
                        .map(EdbVerilecekHizmetPersonel::getEdbPersonel)
                        .forEach(edbPersonelList::add);
            }catch (Exception ex){
                log.error(null,ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void changeActiveIndex() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        this.activeIndex = Integer.parseInt(params.get("index"));
    }

    public void confirmDelete(EdbVerilecekHizmetResim resim) {
        try {
            resim.setAktif(false);
            edbVerilecekHizmetResimService.edit(resim);
            Files.deleteIfExists(Path.of(resim.getResimYolu()));
            FacesUtil.successMessage("resimSilindi");
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void uploadImage(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            String dosyaYolu = "";
            dosyaYolu = createFilePath(event.getFile());
            EdbVerilecekHizmetResim hizmetResim = new EdbVerilecekHizmetResim();
            hizmetResim.setEdbVerilecekHizmet(this.getSelected());
            hizmetResim.setResimAdi(file.getFileName());
            hizmetResim.setResimYolu(dosyaYolu);
            edbVerilecekHizmetResimService.create(hizmetResim);
            saveImage(file, dosyaYolu);
            this.getSelected().getEdbVerilecekHizmetResimList().add(hizmetResim);
            FacesUtil.successMessage("resimEklendi");
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    private String createFilePath(UploadedFile file) {
        String path = Function.isWindows() ? Constants.EVDE_YASAM_VERILECEK_HIZMETLER_KLASORU : Constants.EVDE_YASAM_VERILECEK_HIZMETLER_KLASORU_MAC;
        File etkinlikFolder = new File(path + this.getSelected().getId());

        if (!etkinlikFolder.exists()) {
            etkinlikFolder.mkdir();
        }

        String dosyaAdi = file.getFileName();

        return etkinlikFolder + File.separator + dosyaAdi;

    }

    private void saveImage(UploadedFile file, String imagePath) {
        try {
            File result = new File(imagePath);

            InputStream inputStream;
            try (FileOutputStream fileOutputStream = new FileOutputStream(result)) {
                byte[] buffer = new byte[8192];
                int bulk;
                inputStream = file.getInputStream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }
            }
            inputStream.close();

        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    @Override
    public void saveNew(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                service.update(this.getSelected(), edbPersonelList);
                FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
                edbPersonelList = new ArrayList<>();
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    @Override
    public void save(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                service.update(this.getSelected(), edbPersonelList);
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
                edbPersonelList = new ArrayList<>();
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void valueChangeEdbPersonel(ValueChangeEvent event) {
        List<EdbPersonel> edbPersonelList = (List<EdbPersonel>) event.getNewValue();
        this.getSelected().getEdbVerilecekHizmetPersonelList().forEach(x -> x.setSecili(false));

        for (EdbPersonel personel : edbPersonelList) {
            EdbVerilecekHizmetPersonel hizmetPersonel = this.getSelected().getEdbVerilecekHizmetPersonelList()
                    .stream()
                    .filter(x -> x.getEdbPersonel().getId().equals(personel.getId()))
                    .findFirst()
                    .orElse(null);
            if (hizmetPersonel == null) {
                hizmetPersonel = EdbVerilecekHizmetPersonel.builder().secili(true).edbPersonel(personel).edbVerilecekHizmet(this.getSelected()).build();
                this.getSelected().getEdbVerilecekHizmetPersonelList().add(hizmetPersonel);
            } else {
                int index = this.getSelected().getEdbVerilecekHizmetPersonelList().indexOf(hizmetPersonel);
                hizmetPersonel.setSecili(true);
                this.getSelected().getEdbVerilecekHizmetPersonelList().set(index, hizmetPersonel);
            }
        }
    }


}
