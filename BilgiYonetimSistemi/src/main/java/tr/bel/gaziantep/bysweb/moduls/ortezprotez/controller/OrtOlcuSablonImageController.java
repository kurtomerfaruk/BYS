package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseId;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import tr.bel.gaziantep.bysweb.core.utils.ImageUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcuSablon;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtOlcuSablonService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.09.2025 13:53
 */
@Named
@ApplicationScoped
public class OrtOlcuSablonImageController implements Serializable {
    @Serial
    private static final long serialVersionUID = 4056531537481483638L;

    @Inject
    private OrtOlcuSablonService olcuSablonService;

    @Setter
    private StreamedContent image;
    @Setter
    @Getter
    private UploadedFile file;

    public StreamedContent getImage() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (image == null) {
            return DefaultStreamedContent.DUMMY;
        }
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return DefaultStreamedContent.DUMMY;
        }
        return image;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            file = event.getFile();
            InputStream is = event.getFile().getInputStream();
            image = DefaultStreamedContent.builder().contentType("image/jpeg").stream(() -> is).build();
        } catch (Exception e) {
            System.out.println("Resim yuklenirken hata olustu:" + e.getMessage());
        }
    }

    public void getImageById(Integer id) {
        if (id != null) {
            OrtOlcuSablon ortOlcuSablon = olcuSablonService.find(id);
            if (!StringUtil.isBlank(ortOlcuSablon.getResimYolu())) {
                byte[] arr = ImageUtil.readBytesFromFile(ortOlcuSablon.getResimYolu());
                image = DefaultStreamedContent.builder().contentType("image/jpeg").stream(() -> new ByteArrayInputStream(arr)).build();
            } else {
                image =  DefaultStreamedContent.DUMMY;
            }
        }
    }
}
