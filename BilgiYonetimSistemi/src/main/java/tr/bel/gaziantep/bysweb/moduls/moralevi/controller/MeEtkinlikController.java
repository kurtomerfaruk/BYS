package tr.bel.gaziantep.bysweb.moduls.moralevi.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ResponsiveOption;
import org.primefaces.model.file.UploadedFile;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Function;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeEtkinlik;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeEtkinlikKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeEtkinlikResim;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.service.MeEtkinlikKisiService;
import tr.bel.gaziantep.bysweb.moduls.moralevi.service.MeEtkinlikResimService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.07.2025 14:07
 */
@Named
@ViewScoped
@Slf4j
public class MeEtkinlikController extends AbstractController<MeEtkinlik> {
    @Serial
    private static final long serialVersionUID = 8092294416265962709L;

    @Inject
    private MeEtkinlikKisiService meEtkinlikKisiService;
    @Inject
    private MeEtkinlikResimService meEtkinlikResimService;

    @Getter
    @Setter
    private MeKisi meKisi;
    @Getter
    @Setter
    private List<ResponsiveOption> responsiveOptions;
    @Getter
    @Setter
    private int activeIndex = 0;

    public MeEtkinlikController() {
        super(MeEtkinlik.class);
    }

    @PostConstruct
    @Override
    public void init(){
        readColumns();
        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("1024px", 5));
        responsiveOptions.add(new ResponsiveOption("768px", 3));
        responsiveOptions.add(new ResponsiveOption("560px", 1));
    }

    public void secilenMeKisi(SelectEvent<MeKisi> event) {
        meKisi = event.getObject();
    }

    public void addPerson() {
        try {
            MeEtkinlikKisi meEtkinlikKisi = this.getSelected().getMeEtkinlikKisiList()
                    .stream()
                    .filter(x -> x.getMeKisi().getId().equals(meKisi.getId()))
                    .findFirst()
                    .orElse(null);
            if (meEtkinlikKisi == null) {
                meEtkinlikKisi = MeEtkinlikKisi.builder().meEtkinlik(this.getSelected()).meKisi(meKisi).build();
                if(meEtkinlikKisi.getMeKisi().getId()!=null){
                    this.getSelected().getMeEtkinlikKisiList().add(meEtkinlikKisi);
                    FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
                    meKisi = MeKisi.builder().eyKisi(EyKisi.builder().gnlKisi(new GnlKisi()).build()).build();
                }
            }
        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void removePerson(MeEtkinlikKisi meEtkinlikKisi) {
        try {
            if (this.getSelected().getMeEtkinlikKisiList().contains(meEtkinlikKisi)) {
                meEtkinlikKisi.setAktif(false);
                meEtkinlikKisiService.edit(meEtkinlikKisi);
                this.getSelected().getMeEtkinlikKisiList().remove(meEtkinlikKisi);
                FacesUtil.successMessage(Constants.KAYIT_SILINDI);
            }
        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public  void  readInfo(){
        if(this.getSelected()!=null){
            meKisi = MeKisi.builder().eyKisi(EyKisi.builder().gnlKisi(new GnlKisi()).build()).build();
        }
    }

    public void uploadImage(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            String dosyaYolu = "";
            dosyaYolu = createFilePath(event.getFile());
            MeEtkinlikResim etkinlikResim = new MeEtkinlikResim();
            etkinlikResim.setMeEtkinlik(this.getSelected());
            etkinlikResim.setResimAdi(file.getFileName());
            etkinlikResim.setResimYolu(dosyaYolu);
            meEtkinlikResimService.create(etkinlikResim);
            saveImage(file, dosyaYolu);
            this.getSelected().getMeEtkinlikResimList().add(etkinlikResim);
            FacesUtil.successMessage("resimEklendi");
        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    private String createFilePath(UploadedFile file) {
        String path = Function.isWindows() ? Constants.MORAL_EVI_ETKINLIK_RESIMLERI : Constants.MORAL_EVI_ETKINLIK_RESIMLERI_MAC;
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
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }


    public void changeActiveIndex() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        this.activeIndex = Integer.valueOf(params.get("index"));
    }

    public void confirmDelete(MeEtkinlikResim resim){
        try {
            resim.setAktif(false);
            meEtkinlikResimService.edit(resim);
            Files.deleteIfExists(Path.of(resim.getResimYolu()));
            this.getSelected().getMeEtkinlikResimList().remove(resim);
            FacesUtil.successMessage("resimSilindi");
        }catch (Exception ex){
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}
