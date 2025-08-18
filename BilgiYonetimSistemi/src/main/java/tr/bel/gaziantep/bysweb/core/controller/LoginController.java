package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyKullaniciTuru;
import tr.bel.gaziantep.bysweb.core.sessions.SessionManager;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullaniciGiris;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyKullaniciGirisService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyKullaniciService;

import java.io.IOException;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 14:41
 */
@Named
@SessionScoped
@Slf4j
public class LoginController implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -7645148923743873466L;

    @Inject
    private SessionManager sessionManager;
    @Inject
    @Push(channel = "syKullaniciChannel")
    private PushContext pushContext;

    @Inject
    private InitApp initApp;

    @Inject
    private SyKullaniciService syKullaniciService;
    @Inject
    private SyKullaniciGirisService girisService;


    @Getter
    @Setter
    private SyKullanici syKullanici;

    @Getter
    @Setter
    private String kullaniciAdi;
    @Getter
    @Setter
    private String parola;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String dogrulamaKodu;
    @Getter
    @Setter
    private String sifre;
    @Getter
    @Setter
    private String yeniSifre;
    @Getter
    @Setter
    private String theme="saga-blue";
    @Getter
    @Setter
    private String captchaInput;

    public LoginController() {
    }

    public void login() {
        try {
            HttpSession session = Util.getSession();
            String captcha = (String) session.getAttribute("captcha");

            if (captcha == null || !captcha.equals(captchaInput)) {
                Util.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doğrulama Kodunu hatalı girdiniz", null));
                return;
            }

            if (StringUtil.isNotBlank(kullaniciAdi) && StringUtil.isNotBlank(parola)) {
                syKullanici = syKullaniciService.findByKullaniciAdiByParola(kullaniciAdi, parola);
            }



            HttpServletRequest request = Util.getRequest();

            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (StringUtil.isBlank(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }

            if (syKullanici != null) {

                if (!syKullanici.isAktif()) {
                    FacesUtil.warningMessage("girisYapanKullaniciPasif");
                    return;
                }

                session.setAttribute("syKullanici", syKullanici);

                SyKullaniciGiris giris = new SyKullaniciGiris();
                giris.setSyKullanici(syKullanici);
                giris.setGirisIp(ipAddress);
                giris.setSessionId((String) session.getAttribute(Constants.SESSION_ID));
                giris.setGirisZamani(LocalDateTime.now());
                giris.setAciklama("Başarılı giriş yapıldı.");
                if (syKullanici.getSyKullaniciGirisList() == null) {
                    syKullanici.setSyKullaniciGirisList(new ArrayList<>());
                }
                if (StringUtil.isBlank(syKullanici.getTema())) {
                    syKullanici.setTema("saga-blue");
                }
                syKullanici.setSonGirilenIp(ipAddress);
                syKullanici.setSonGirisZamani(LocalDateTime.now());
                syKullanici.getSyKullaniciGirisList().add(giris);
                syKullaniciService.edit(syKullanici);

                if (!initApp.getSyKullanicis().contains(syKullanici)) {
                    String sessionId = (String) session.getAttribute(Constants.SESSION_ID);
                    syKullanici.setSessionId(sessionId);
                    initApp.getSyKullanicis().add(syKullanici);
                    session.setAttribute("syKullanicilar", initApp.getSyKullanicis());
                }

                if (syKullanici.getSyKullaniciGirisList() != null) {
                    List<SyKullaniciGiris> girisler = syKullanici.getSyKullaniciGirisList();
                    Collections.reverse(girisler);
                    girisler = girisler.stream().limit(10).collect(Collectors.toList());
                    syKullanici.setSyKullaniciGirisList(girisler);
                }
                theme = syKullanici.getTema();

                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
                List<SyKullanici> kullanicis = new ArrayList<>(initApp.getSyKullanicis());

                Collection<Integer> kullaniciIds = kullanicis.stream()
                        .filter(x -> x.getKullaniciTuru().equals(EnumSyKullaniciTuru.SISTEM_YONETICISI))
                        .map(SyKullanici::getId)
                        .collect(Collectors.toList());
                String message = syKullanici.getGnlPersonel().getGnlKisi().getAdSoyad() + " giriş yaptı.";
                pushContext.send(new FacesMessage(message), kullaniciIds);
            } else {
                syKullanici = syKullaniciService.findByKullaniciAdi(kullaniciAdi);
                if (syKullanici != null) {
                    SyKullaniciGiris giris = new SyKullaniciGiris();
                    giris.setSyKullanici(syKullanici);
                    giris.setGirisIp(ipAddress);
                    giris.setSessionId((String) session.getAttribute("sessionId"));
                    giris.setGirisZamani(LocalDateTime.now());
                    giris.setAciklama("Hatalı giriş");
                    girisService.create(giris);
                }
                FacesUtil.warningMessage("kullaniciAdiParolaHatali");
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }

    }

    public void logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        try {
            initApp.getSyKullanicis().remove(syKullanici);
            List<SyKullanici> kullanicis = new ArrayList<>(initApp.getSyKullanicis());

            Collection<Integer> kullaniciIds = kullanicis.stream()
                    .filter(x -> x.getKullaniciTuru().equals(EnumSyKullaniciTuru.SISTEM_YONETICISI))
                    .map(SyKullanici::getId)
                    .collect(Collectors.toList());
            String message = syKullanici.getGnlPersonel().getGnlKisi().getAdSoyad() + " çıkış yaptı.";
            pushContext.send(new FacesMessage(message), kullaniciIds);
            syKullanici = null;
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void profilKaydet(ActionEvent event) {
        if (syKullanici != null) {
            try {
                syKullaniciService.edit(syKullanici);
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void destroySessionById(String id) {
        if (StringUtil.isNotBlank(id)) {
            SyKullanici user = initApp.getSyKullanicis().stream().filter(x -> x.getSessionId().equals(id)).findFirst().orElse(null);
            if (user != null) {
                sessionManager.invalidateSession(id);
                pushContext.send("oturum kapatildi",user.getId().longValue());
                FacesUtil.successMessage("kullaniciOturumuKapatildi");
            }

        }
    }

    public void destroySession() {
        List<SyKullanici> userList = new ArrayList<>(initApp.getSyKullanicis());
        for (SyKullanici user : userList) {
            if (!user.getKullaniciTuru().equals(EnumSyKullaniciTuru.SISTEM_YONETICISI)) {
                sessionManager.invalidateSession(user.getSessionId());
                initApp.getSyKullanicis().remove(user);
            }
        }
        FacesUtil.successMessage("kullaniciOturumuKapatildi");
    }

    public void saveTheme(ActionEvent event) {
        try {
            syKullaniciService.edit(syKullanici);
            HttpSession session = Util.getSession();
            session.setAttribute("syKullanici", syKullanici);
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
        }
    }
}
