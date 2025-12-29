package tr.bel.gaziantep.bysweb.moduls.engelsizler.schedule;

import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.InitApp;
import tr.bel.gaziantep.bysweb.core.converter.ModelConverter;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.Function;
import tr.bel.gaziantep.bysweb.core.utils.ListUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisiEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyEngelGrubuService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.webservice.gazikart.controller.GaziKartService;
import tr.bel.gaziantep.bysweb.webservice.gazikart.model.ServisModel;
import tr.bel.gaziantep.bysweb.webservice.gazikart.model.ServisSonucu;
import tr.bel.gaziantep.bysweb.webservice.kps.controller.KpsService;
import tr.bel.gaziantep.bysweb.webservice.kps.model.KpsModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameters;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.08.2025 15:48
 */
@Stateless
@Slf4j
public class GaziKartSchedule implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 2265107901295922872L;

    @Inject
    private EyKisiService service;
    @Inject
    private EyEngelGrubuService engelGrubuService;
    @Inject
    private InitApp initApp;
    @Inject
    private ModelConverter converter;

    @Schedule(minute = "0",second = "0",dayOfMonth = "*",month = "*",year = "*",hour = "1",dayOfWeek = "Mon-Fri",persistent = false)
    public void updateGnlKisi() throws Exception {
        log.info("Gazikart baslama tarihi :"+ LocalDateTime.now());
        listUpdate();
        log.info("Gazikart Guncelle bitis tarihi:" + LocalDateTime.now());
    }

    private void listUpdate() throws Exception {
        GaziKartService gaziKartService = new GaziKartService();
        String format = "yyyyMMdd";
        ServisSonucu sonuc = gaziKartService.getList(initApp.getProperty("gazikart.webServisLink"), DateUtil.localdateToString(LocalDate.now().minusDays(1),
                format), DateUtil.localdateToString(LocalDate.now(), format));
        if (sonuc != null && sonuc.getData() != null) {
            List<KisiParameter> kisiParameterList = converter.servisModelToKisiParameters(sonuc.getData(), EnumModul.GAZIKART);
            if (kisiParameterList.isEmpty()) {
                return;
            }
            List<List<KisiParameter>> hasNotTcKimlikSplitList = ListUtil.partition(kisiParameterList, 100);
            List<String> hasNotTcKimlikList = new ArrayList<>();

            for (List<KisiParameter> kisiParameters : hasNotTcKimlikSplitList) {
                List<String> tcNewList = kisiParameters.stream().map(parameter -> parameter.getTcKimlikNo() + "").toList();
                hasNotTcKimlikList.addAll(service.findByTcKimlikNoList(tcNewList));
            }

            for (String str : hasNotTcKimlikList) {
                kisiParameterList.removeIf(x -> x.getTcKimlikNo() == Long.parseLong(str));
            }

            List<List<KisiParameter>> splitList = ListUtil.partition(kisiParameterList, 70);
            KpsService kpsService = new KpsService();
            for (List<KisiParameter> kisiParameters : splitList) {
                KisiParameters parameters = new KisiParameters();
                parameters.setKisiler(kisiParameters);
                List<KpsModel> kpsModels = kpsService.getKpsFull(initApp.getProperty("webServisLink"), initApp.getProperty("webServisToken"), parameters);
                addPerson(sonuc.getData(), kpsModels, EnumModul.GAZIKART);

            }
        }
    }

    private void addPerson(List<ServisModel> servisModels, List<KpsModel> kpsModels, EnumModul modul) throws Exception {

        for (KpsModel kpsModel : kpsModels) {

            List<EyKisiEngelGrubu> eyKisiEngelgrubus = new ArrayList<>();
            EyKisi eyKisi = converter.convertKpsModelToEyKisi(kpsModel, modul);
            ServisModel servisModel = isExistServisModel(eyKisi.getGnlKisi().getTcKimlikNo(), servisModels);

            if (servisModel != null) {
                String phoneNumber = Function.phoneValidate(servisModel.getTelMobile());
                eyKisi.getGnlKisi().setTelefon(phoneNumber);
                String[] disableGroups = servisModel.getDisabledDegree().split("-");
                String disableGroupStr = "";
                if (disableGroups.length == 2) {
                    Integer engelOrani = Integer.parseInt(disableGroups[1].replace("+", ""));
                    eyKisi.setToplamVucutKayipOrani(engelOrani);
                }
                if (disableGroups.length > 0) {
                    disableGroupStr = disableGroups[0].split(" ")[0];
                    if (disableGroupStr.equals("Süregen")) {
                        disableGroupStr = disableGroupStr.replace("g", "ğ");
                    }
                }

                EyEngelGrubu disableGroup = engelGrubuService.findByName(disableGroupStr);
                if (disableGroup != null) {
                    EyKisiEngelGrubu eyKisiEngelgrubu = new EyKisiEngelGrubu();
                    eyKisiEngelgrubu.setEyEngelGrubu(disableGroup);
                    eyKisiEngelgrubu.setEyKisi(eyKisi);
                    eyKisiEngelgrubu.setSecili(Boolean.TRUE);
                    eyKisiEngelgrubus.add(eyKisiEngelgrubu);
                }
                eyKisi.setEyKisiEngelGrubuList(eyKisiEngelgrubus);
                service.edit(eyKisi);
            }
        }
    }

    private ServisModel isExistServisModel(String tcKimlikNo, List<ServisModel> servisModels) {
        return servisModels.stream()
                .filter(servisModel -> StringUtil.isNotBlank(servisModel.getIdentityNo()) && servisModel.getIdentityNo().equals(tcKimlikNo))
                .findFirst()
                .orElse(null);
    }
}
