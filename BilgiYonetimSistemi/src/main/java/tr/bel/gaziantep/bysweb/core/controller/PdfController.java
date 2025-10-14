package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuruDosya;

import java.io.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.10.2025 10:29
 */
@Named
@ApplicationScoped
public class PdfController implements Serializable {
    @Serial
    private static final long serialVersionUID = -4356587689209062549L;

    private StreamedContent content;

    public StreamedContent getContent() {
        return content;
    }

    public void setContent(StreamedContent content) {
        this.content = content;
    }

    public StreamedContent getOrtBasvuruDosyaPdf(OrtBasvuruDosya dosya) throws FileNotFoundException {
        if (dosya != null) {
            File file = new File(dosya.getDosyaYolu());
            InputStream stream = new FileInputStream(file);
            content = DefaultStreamedContent.builder().contentType("application/pdf").name(dosya.getDosyaAdi()).stream(() -> stream).build();
        }
        return content;
    }
}
