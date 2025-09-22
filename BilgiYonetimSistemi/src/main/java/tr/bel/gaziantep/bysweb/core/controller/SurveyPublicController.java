package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlAnketTuru;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlAnket;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlAnketSoru;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlAnketCevapService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlAnketService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.09.2025 09:17
 */
@Named
@ViewScoped
@Getter
@Setter
@Slf4j
public class SurveyPublicController implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 7263714441584443026L;

    @Inject
    private GnlAnketService service;
    @Inject
    private GnlAnketCevapService gnlAnketCevapService;
    @Inject
    private GnlKisiService gnlKisiService;
    @Inject
    private KpsController kps;
    @Inject
    private FacesContext faces;

    private GnlAnket gnlAnket;
    private Map<Integer, Object> answers = new HashMap<>();
    private Map<Integer, Object> otherAnswers = new HashMap<>();
//    private String tcKimlikNo;
//    private LocalDate dogumTarihi;
    private String anonToken;
    private String[] selectedManyCheckbox;
    private GnlKisi gnlKisi;

    @PostConstruct
    public void init() {
        Map<String, String> params = faces.getExternalContext().getRequestParameterMap();
        String token = params.get("token");
        if (token != null) {
            gnlAnket = service.findByToken(token);
        }

        if (gnlAnket == null) return;


        if (gnlAnket.getAnketTuru() == EnumGnlAnketTuru.GENEL) {
            Cookie cookie = getCookie("SURVEY_ANON_TOKEN");
            if (cookie != null && StringUtil.isNotBlank(cookie.getValue())) {
                anonToken = cookie.getValue();
            } else {
                anonToken = UUID.randomUUID().toString();
                faces.getExternalContext().addResponseCookie("SURVEY_ANON_TOKEN", anonToken, Map.of("path", "/", "maxAge", 31536000));
            }

        }

        gnlKisi = new GnlKisi();

        Integer loggedUserId = getLoggedUserIdOrNull();
        boolean can = service.canSubmit(gnlAnket, loggedUserId, gnlKisi.getTcKimlikNo(), anonToken);
        if (!can) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Uyarı", " Bu anketi daha önce doldurmuşsunuz.");
            PrimeFaces.current().dialog().showMessageDynamic(message);
            return;
        }

    }

    public void submitAnswers() {
        try {
            if (gnlAnket == null) {
                faces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anket bulunamadı", null));
                return;
            }

            Integer loggedUserId = getLoggedUserIdOrNull();
            boolean can = service.canSubmit(gnlAnket, loggedUserId, gnlKisi.getTcKimlikNo(), anonToken);
            if (!can) {
                faces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Bu anketi daha önce doldurmuşsunuz.", null));
                return;
            }

            if (gnlAnket.getAnketTuru() == EnumGnlAnketTuru.KISI) {
                if (gnlKisi == null) {
                    faces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Kişi bilgilerini kontrol ediniz. Bilgiler yanlış olduğundan kayıt yapılamaz", null));
                    return;
                }
            }

            for (GnlAnketSoru gnlAnketSoru : gnlAnket.getGnlAnketSoruList()) {
                if (gnlAnketSoru.isZorunlu()) {
                    Object v = answers.get(gnlAnketSoru.getId());
                    boolean empty = (v == null) || (v instanceof String && ((String) v).isBlank())
                            || (v instanceof Collection && ((Collection<?>) v).isEmpty());
                    if (empty) {
                        faces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Lütfen tüm zorunlu alanları doldurun: " + gnlAnketSoru.getTanim(), null));
                        return;
                    }
                }
            }

            gnlAnketCevapService.save(gnlAnket, loggedUserId, gnlKisi, anonToken, answers, otherAnswers);

            faces.addMessage(null, new FacesMessage("Cevaplarınız kaydedildi. Teşekkürler!"));
            gnlAnket = new GnlAnket();
            PrimeFaces.current().ajax().update("fillForm");
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    private Cookie getCookie(String name) {
        Map<String, Object> cookies = faces.getExternalContext().getRequestCookieMap();
        if (cookies.containsKey(name)) {
            return (Cookie) cookies.get(name);
        }
        return null;
    }

    private Integer getLoggedUserIdOrNull() {
        // Senin oturum yönetimine göre uyarlayacaksın.
        // Örnek: SecurityContext, SessionMap veya custom user bean.
        return null;
    }

//    public void manyCheckboxChanged(List<GnlAnketSoruSecenek> list) {
//        String message = "";
//        if (list != null) {
//            for (int i = 0; i < list.size(); i++) {
//                if (i > 0) {
//                    message += ", ";
//                }
//                message += list.get(i).getId();
//            }
//        }
//    }

//    public void validateRadio(FacesContext ctx, UIComponent comp, Object value) {
//        String qid = comp.getId().split("_")[1]; // veya başka yöntemle questionId al
//        if ("OTHER".equals(value)) {
//            String otherText = otherAnswers.get(Integer.valueOf(qid)).toString();
//            if (otherText == null || otherText.trim().isEmpty()) {
//                throw new ValidatorException(
//                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                                "Diğer seçildiğinde açıklama zorunludur", null));
//            }
//        }
//    }

//    public void deneme(Integer id) {
//        System.out.println("answer:" + answers.size());
//        System.out.println(answers.get(id));
//        System.out.println("otheranswers: " + otherAnswers.size());
//    }

    public boolean isOtherSelected(Integer questionId) {
        Object[] selected = (Object[]) answers.get(questionId); // Array alıyoruz
        if (selected == null) return false;
        for (Object o : selected) {
            if ("OTHER".equals(o)) return true;
        }
        return false;
    }

    public void getTcKimlik() {
        try {
            String tcKimlikNo = gnlKisi.getTcKimlikNo();
            LocalDate dogumTarihi = gnlKisi.getDogumTarihi();
            if (StringUtil.isBlank(tcKimlikNo)) {
                FacesUtil.addExclamationMessage("T.C. Kimlik No boş olamaz");
                return;
            }
            if (dogumTarihi == null) {
                FacesUtil.addExclamationMessage("Doğum Tarihi boş olamaz");
                return;
            }
//            gnlKisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
//            if (gnlKisi == null) {
//                gnlKisi = GnlKisi.builder().tcKimlikNo(tcKimlikNo).dogumTarihi(dogumTarihi).build();
//            }
            gnlKisi = kps.findByTcKimlikNo(gnlKisi, EnumModul.GENEL);
            if(gnlKisi == null) {
                gnlKisi = new GnlKisi();
            }else{
                gnlKisiService.edit(gnlKisi);
            }
        } catch (Exception ex) {
            log.error(null, ex);
        }
    }

}
