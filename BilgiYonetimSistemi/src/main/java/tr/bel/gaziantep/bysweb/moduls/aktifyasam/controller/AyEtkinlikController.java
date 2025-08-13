package tr.bel.gaziantep.bysweb.moduls.aktifyasam.controller;

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
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyEtkinlik;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyEtkinlikKisi;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyEtkinlikResim;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyKisi;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.service.AyEtkinlikResimService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

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
 * @since 3.07.2025 10:17
 */
@Named
@ViewScoped
@Slf4j
public class AyEtkinlikController extends AbstractController<AyEtkinlik> {

    @Serial
    private static final long serialVersionUID = -5282679943229703925L;

    @Inject
    private AyEtkinlikResimService ayEtkinlikResimService;

    @Getter
    @Setter
    private AyKisi ayKisi;
    @Getter
    @Setter
    private List<ResponsiveOption> responsiveOptions1;
    @Getter
    @Setter
    private int activeIndex = 0;

    public AyEtkinlikController() {
        super(AyEtkinlik.class);
    }

    @PostConstruct
    @Override
    public void init(){
        readColumns();
        responsiveOptions1 = new ArrayList<>();
        responsiveOptions1.add(new ResponsiveOption("1024px", 5));
        responsiveOptions1.add(new ResponsiveOption("768px", 3));
        responsiveOptions1.add(new ResponsiveOption("560px", 1));
    }

    public void secilenAyKisi(SelectEvent<AyKisi> event) {
        ayKisi = event.getObject();
    }

    public void addPerson() {
        try {
            AyEtkinlikKisi ayEtkinlikKisi = this.getSelected().getAyEtkinlikKisiList()
                    .stream()
                    .filter(ayEtkinlikKisi1 -> ayEtkinlikKisi1.getAyKisi().getId().equals(ayKisi.getId()))
                    .findFirst()
                    .orElse(null);
            if (ayEtkinlikKisi == null) {
                ayEtkinlikKisi = AyEtkinlikKisi.builder().ayEtkinlik(this.getSelected()).ayKisi(ayKisi).build();
                if(ayEtkinlikKisi.getAyKisi().getId()!=null){
                    this.getSelected().getAyEtkinlikKisiList().add(ayEtkinlikKisi);
                    FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
                    ayKisi = AyKisi.builder().gnlKisi(new GnlKisi()).build();
                }
            }
        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void removePerson(AyEtkinlikKisi ayEtkinlikKisi) {
        try {
            if (this.getSelected().getAyEtkinlikKisiList().contains(ayEtkinlikKisi)) {
                this.getSelected().getAyEtkinlikKisiList().remove(ayEtkinlikKisi);
                FacesUtil.successMessage(Constants.KAYIT_SILINDI);
            }
        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public  void  readInfo(){
        if(this.getSelected()!=null){
            ayKisi = AyKisi.builder().gnlKisi(new GnlKisi()).build();
        }
    }

    public void uploadImage(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            String dosyaYolu = "";
            dosyaYolu = createFilePath(event.getFile());
            AyEtkinlikResim etkinlikResim = new AyEtkinlikResim();
            etkinlikResim.setAyEtkinlik(this.getSelected());
            etkinlikResim.setResimAdi(file.getFileName());
            etkinlikResim.setResimYolu(dosyaYolu);
            ayEtkinlikResimService.create(etkinlikResim);
            saveImage(file, dosyaYolu);
            this.getSelected().getAyEtkinlikResimList().add(etkinlikResim);
            FacesUtil.successMessage("resimEklendi");
        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    private String createFilePath(UploadedFile file) {
        String path = Function.isWindows() ? Constants.AKTIF_YASAM_ETKINLIK_RESIMLERI : Constants.AKTIF_YASAM_ETKINLIK_RESIMLERI_MAC;
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

    public void confirmDelete(AyEtkinlikResim resim){
        try {
            resim.setAktif(false);
            ayEtkinlikResimService.edit(resim);
            Files.deleteIfExists(Path.of(resim.getResimYolu()));
            this.getSelected().getAyEtkinlikResimList().remove(resim);
            FacesUtil.successMessage("resimSilindi");
        }catch (Exception ex){
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}
