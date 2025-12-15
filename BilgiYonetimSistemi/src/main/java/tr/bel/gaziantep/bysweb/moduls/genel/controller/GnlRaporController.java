package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.StreamedContent;
import tr.bel.gaziantep.bysweb.core.service.EnumService;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporKolon;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporKullaniciRaporSablon;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporModul;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporParametre;
import tr.bel.gaziantep.bysweb.moduls.genel.report.GnlRaporIstek;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlRaporKullaniciRaporSablonService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlRaporModulService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlRaporService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serial;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.12.2025 13:58
 */
@Named
@ViewScoped
@Slf4j
public class GnlRaporController implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -2889176134024945015L;

    @Inject
    private GnlRaporModulService gnlRaporModulService;
    @Inject
    private GnlRaporService gnlRaporService;
    @Inject
    private GnlRaporKullaniciRaporSablonService gnlRaporKullaniciRaporSablonService;
    @Inject
    private EnumService enumService;

    @Getter
    @Setter
    private GnlRaporIstek raporIstek = new GnlRaporIstek();
    @Getter
    @Setter
    private List<GnlRaporModul> modulListesi;
    @Getter
    @Setter
    private List<GnlRaporKolon> secilebilirKolonlar;
    @Getter
    @Setter
    private List<GnlRaporParametre> modulParametreleri;
    @Getter
    @Setter
    private List<GnlRaporKullaniciRaporSablon> kullaniciSablonlari;
    @Getter
    @Setter
    private GnlRaporModul seciliModul;
    @Getter
    @Setter
    private String ciktiTipi = "PDF";
    @Getter
    @Setter
    private String sablonAdi;

    @Getter
    @Setter
    private List<GnlRaporKolon> seciliKolonlar = new ArrayList<>();
    @Getter
    @Setter
    private Map<Integer, String> parametreDegerleri = new HashMap<>();
    @Getter
    @Setter
    private Map<Integer, String> parametreOperatorleri = new HashMap<>();
    @Getter
    @Setter
    private Map<Integer, String> ikinciDegerler = new HashMap<>();
    private Map<String, List<Map<String, Object>>> enumCache = new HashMap<>();
    @Getter
    @Setter
    private Map<Long, List<String>> cokluEnumDegerleri = new HashMap<>();
    @Getter
    @Setter
    private StreamedContent pdfContent;
    @Getter
    @Setter
    private boolean pdfReady;

    @PostConstruct
    public void init() {
        modulListesi = gnlRaporModulService.findAll();
        loadUserTemplates();
    }

    public void onModulChange() {
        seciliKolonlar.clear();
        parametreDegerleri.clear();
        parametreOperatorleri.clear();
        ikinciDegerler.clear();
        if (seciliModul != null) {
            secilebilirKolonlar = seciliModul.getGnlRaporKolonList();
            modulParametreleri = seciliModul.getGnlRaporParametreList();

            for (GnlRaporKolon kolon : secilebilirKolonlar) {
                if (kolon.isVarsayilan()) {
                    seciliKolonlar.add(kolon);
                }
            }

            for (GnlRaporParametre param : modulParametreleri) {
                parametreDegerleri.put(param.getId(),param.getVarsayilanDeger());
                parametreOperatorleri.put(param.getId(),param.getVarsayilanOperator());
            }


        }
    }


    public void raporOlustur() {
        try {
            System.out.println("raporOlustur");
            prepareRaporIstek();
            byte[] raporBytes = gnlRaporService.dinamikRaporOlustur(raporIstek);
            if(ciktiTipi.equals("PDF")) {
//                pdfContent = DefaultStreamedContent.builder()
//                        .stream(()->new ByteArrayInputStream(raporBytes))
//                        .contentType("application/pdf")
//                        .name("rapor.pdf")
//                        .build();
//                //PrimeFaces.current().ajax().update("pdfForm");
//                PrimeFaces.current().executeScript("PF('pdfDialogWidget').show();");
//                FacesContext.getCurrentInstance()
//                        .getExternalContext()
//                        .getSessionMap()
//                        .put("pdfData", raporBytes);

//                ServletContext sc = Util.getServletContext();
//                HttpSession session = Util.getSession();
//                session.setAttribute("pdfData",raporBytes);
//                sc.getRequestDispatcher("/gnlDinamikReportServlet");
//                PrimeFaces.current().executeScript("PF('pdfDialogWidget').show();");
//                PrimeFaces.current().ajax().update("pdfForm");

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .put("pdfData", raporBytes);
                pdfReady=true;
            }else{
                String fileName = "rapor." + (ciktiTipi.equals("EXCEL") ? "xlsx" : ciktiTipi.toUpperCase());
                downloadFile(raporBytes, fileName);
            }
            FacesUtil.addSuccessMessage("Rapor başarıyla oluşturuldu");
        } catch (Exception ex) {
           log.error(null,ex);
           FacesUtil.addErrorMessage("Rapor oluşturulurken hata oluştu.");
        }
    }

    public void sablonKaydet() {
        try {
            if (sablonAdi == null || sablonAdi.trim().isEmpty()) {
                FacesUtil.addExclamationMessage("Lütfen şablon için bir ad giriniz...");
                return;
            }

            GnlRaporKullaniciRaporSablon sablon = new GnlRaporKullaniciRaporSablon();
            sablon.setSablonAdi(sablonAdi);
            sablon.setGnlRaporModul(seciliModul);
            sablon.setSyKullanici(Util.getSyKullanici());
            sablon.setGenel(false);

            List<GnlRaporIstek.RaporKolonDto> kolonDtoList = new ArrayList<>();
            for (GnlRaporKolon kolon : seciliKolonlar) {
                GnlRaporIstek.RaporKolonDto dto = new GnlRaporIstek.RaporKolonDto();
                dto.setId(kolon.getId());
                dto.setAlanAdi(kolon.getAlanAdi());
                dto.setGorunurAdi(kolon.getGorunurAdi());
                dto.setVeriTipi(kolon.getVeriTipi());
                kolonDtoList.add(dto);
            }

            List<GnlRaporIstek.ParametreDegeriDto> paramDtoList = new ArrayList<>();
            for (GnlRaporParametre param : modulParametreleri) {
                String deger = parametreDegerleri.get(param.getId());
                if (deger != null && !deger.trim().isEmpty()) {
                    GnlRaporIstek.ParametreDegeriDto dto = new GnlRaporIstek.ParametreDegeriDto();
                    dto.setParametreId(param.getId());
                    dto.setParametreAdi(param.getParametreAdi());
                    dto.setGorunurAdi(param.getGorunurAdi());
                    dto.setDeger(deger);
                    dto.setIkinciDeger(ikinciDegerler.get(param.getId()));
                    dto.setOperator(parametreOperatorleri.get(param.getId()));
                    dto.setVeriTipi(param.getVeriTipi());
                    paramDtoList.add(dto);
                }
            }

            gnlRaporKullaniciRaporSablonService.saveSablon(sablon, kolonDtoList, paramDtoList);
            loadUserTemplates();
            FacesUtil.addSuccessMessage("Şablon başarıyla kaydedildi");

        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.addErrorMessage("Şablon kaydedilirken hata oluştu.");
        }
    }

    public void sablonYukle(Integer sablonId) {
        try {
            GnlRaporIstek tempIstek = new GnlRaporIstek();
            tempIstek.setSablonId(sablonId);
            tempIstek = gnlRaporKullaniciRaporSablonService.sablonuRaporIstegineCevir(tempIstek);

            onModulChange();

            seciliKolonlar.clear();
            for (GnlRaporIstek.RaporKolonDto kolonDto : tempIstek.getKolonlar()) {
                for (GnlRaporKolon kolon : secilebilirKolonlar) {
                    if (kolon.getId().equals(kolonDto.getId())) {
                        seciliKolonlar.add(kolon);
                        break;
                    }
                }
            }

            if (tempIstek.getParametreler() != null) {
                for (GnlRaporIstek.ParametreDegeriDto paramDto : tempIstek.getParametreler()) {
                    parametreDegerleri.put(paramDto.getParametreId(), paramDto.getDeger());
                    if (paramDto.getIkinciDeger() != null) {
                        ikinciDegerler.put(paramDto.getParametreId(), paramDto.getIkinciDeger());
                    }
                    if (paramDto.getOperator() != null) {
                        parametreOperatorleri.put(paramDto.getParametreId(), paramDto.getOperator());
                    }
                }
            }

            FacesUtil.addSuccessMessage("Rapor şablonu başarıyla yüklendi");

        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.addErrorMessage("Rapor şablonu yüklenirken hata oluştu.");
        }
    }

    public void sablonSil(Integer sablonId) {
        try {
            GnlRaporKullaniciRaporSablon sablon = gnlRaporKullaniciRaporSablonService.find(sablonId);
            sablon.setAktif(false);
            gnlRaporKullaniciRaporSablonService.remove(sablon);
            loadUserTemplates();

            FacesUtil.addSuccessMessage("Rapor şablonu başarıyla silindi.");

        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.addErrorMessage("Rapor şablonu silinirken hata oluştu.");
        }
    }

    private void prepareRaporIstek() {
        raporIstek.setModul(seciliModul);
        raporIstek.setModulAdi(seciliModul != null ? seciliModul.getModulAdi() : "");
        raporIstek.setCiktiTipi(ciktiTipi);
        raporIstek.setKullaniciId(Util.getSyKullanici().getId());
        raporIstek.setRaporTarihi(LocalDate.now());

        List<GnlRaporIstek.RaporKolonDto> kolonDtoList = new ArrayList<>();
        for (GnlRaporKolon kolon : seciliKolonlar) {
            GnlRaporIstek.RaporKolonDto dto = new GnlRaporIstek.RaporKolonDto();
            dto.setId(kolon.getId());
            dto.setAlanAdi(kolon.getAlanAdi());
            dto.setGorunurAdi(kolon.getGorunurAdi());
            dto.setVeriTipi(kolon.getVeriTipi());
            kolonDtoList.add(dto);
        }
        raporIstek.setKolonlar(kolonDtoList);

        List<GnlRaporIstek.ParametreDegeriDto> paramDtoList = new ArrayList<>();
        for (GnlRaporParametre param : modulParametreleri) {
            String deger = parametreDegerleri.get(param.getId());
            if ((deger != null && !deger.trim().isEmpty()) || StringUtil.isNotBlank(param.getSqlKosul())) {
                GnlRaporIstek.ParametreDegeriDto dto = new GnlRaporIstek.ParametreDegeriDto();
                dto.setParametreId(param.getId());
                dto.setParametreAdi(param.getParametreAdi());
                dto.setGorunurAdi(param.getGorunurAdi());
                dto.setDeger(deger);
                dto.setIkinciDeger(ikinciDegerler.get(param.getId()));
                dto.setOperator(parametreOperatorleri.get(param.getId()));
                dto.setVeriTipi(param.getVeriTipi());
                dto.setLookupEnumClass(param.getLookupEnumClass());
                dto.setSqlKosul(param.getSqlKosul());
                paramDtoList.add(dto);
            }
        }
        raporIstek.setParametreler(paramDtoList);
    }

    private void downloadFile(byte[] content, String fileName) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

            response.reset();
            response.setContentType(getContentType(ciktiTipi));
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + fileName + "\"");
            response.setContentLength(content.length);

            OutputStream output = response.getOutputStream();
            output.write(content);
            output.flush();

            facesContext.responseComplete();
        } catch (IOException e) {
            throw new RuntimeException("Dosya indirme hatası: " + e.getMessage(), e);
        }
    }


    private String getContentType(String outputType) {
        return switch (outputType.toUpperCase()) {
            case "PDF" -> "application/pdf";
            case "EXCEL" -> "application/vnd.ms-excel";
            case "CSV" -> "text/csv";
            default -> "application/octet-stream";
        };
    }

    private void loadUserTemplates() {
        try {
            kullaniciSablonlari = gnlRaporKullaniciRaporSablonService.getKullaniciSablonlari(Objects.requireNonNull(Util.getSyKullanici()).getId());
        } catch (Exception e) {
            kullaniciSablonlari = new ArrayList<>();
        }
    }

    public List<Map<String, Object>> getEnumValuesForParametre(GnlRaporParametre parametre) {
        if (parametre == null || parametre.getLookupEnumClass() == null) {
            return new ArrayList<>();
        }

        String enumClassName = parametre.getLookupEnumClass();

        if (enumCache.containsKey(enumClassName)) {
            return enumCache.get(enumClassName);
        }

        List<Map<String, Object>> enumValues = enumService.getEnumValues(enumClassName);
        enumCache.put(enumClassName, enumValues);

        return enumValues;
    }

    public List<SelectItem> getEnumSelectItems(GnlRaporParametre parametre) {
        List<SelectItem> items = new ArrayList<>();

        items.add(new SelectItem("", "Seçiniz..."));

        List<Map<String, Object>> enumValues = getEnumValuesForParametre(parametre);
        for (Map<String, Object> enumValue : enumValues) {
            String kod = (String) enumValue.get("kod");
            String aciklama = (String) enumValue.get("aciklama");
            items.add(new SelectItem(kod, aciklama));
        }

        return items;
    }

    public List<SelectItem> getMultiEnumSelectItems(GnlRaporParametre parametre) {
        List<SelectItem> items = new ArrayList<>();

        List<Map<String, Object>> enumValues = getEnumValuesForParametre(parametre);
        for (Map<String, Object> enumValue : enumValues) {
            String kod = (String) enumValue.get("kod");
            String aciklama = (String) enumValue.get("aciklama");
            items.add(new SelectItem(kod, aciklama));
        }

        return items;
    }


}
