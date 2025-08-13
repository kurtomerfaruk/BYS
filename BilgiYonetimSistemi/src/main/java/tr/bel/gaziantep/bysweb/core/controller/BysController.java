package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DialogFrameworkOptions;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Function;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 13:52
 */
@Setter
@Getter
@Named
@ViewScoped
@Slf4j
public class BysController implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 5495046820308144447L;

    private boolean error;

    @PostConstruct
    public void init() {
        createNotificationList();
    }

    private void createNotificationList()  {
        try {
            SyKullanici kullanici = Util.getSyKullanici();
            if (kullanici == null) {
                throw new Exception("Kullanıcı bilgileri okunamadı....");
            }
            if (kullanici.getParola().equals(Function.encrypt("Bys_12345"))) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Şifre Bilgisi Güncelleme", "Şifrenizi Değiştirin.\n "
                        + "Aksi takdirde 10 gün içerisinde parolanız sistem tarafından değişecektir");
                PrimeFaces.current().dialog().showMessageDynamic(message);
                error = true;
            }

            if (kullanici.getGnlPersonel().getGnlKisi().getTcKimlikNo().isBlank() || kullanici.getGnlPersonel().getGnlKisi().getDogumTarihi() == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Kişi Bilgileri Güncelleme", "T.C. Kimlik Numaranızı / Doğum Tarihinizi " +
                        "güncellemeniz gerekmektedir.");
                PrimeFaces.current().dialog().showMessageDynamic(message);
                error = true;
            }
        }catch (Exception ex){
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void openCommonDialog(String fileName) {
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .modal(true)
                .fitViewport(true)
                .responsive(true)
                .width("100%")
                .contentWidth("100%")
                .resizeObserver(true)
                .resizeObserverCenter(true)
                .resizable(false)
                .styleClass("max-w-screen")
                .iframeStyleClass("max-w-screen")
                .appendTo("@(body)")
                .position("top")
                .build();
        PrimeFaces.current().dialog().openDynamic("/moduls/common/" + fileName, options, null);
    }
}
