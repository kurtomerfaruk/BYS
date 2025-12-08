package tr.bel.gaziantep.bysweb.core.service;

import jakarta.ejb.Stateless;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumSoruTuru;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.*;
import tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbBasvuruDurumu;
import tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbHizmetTuru;
import tr.bel.gaziantep.bysweb.core.enums.genel.*;
import tr.bel.gaziantep.bysweb.core.enums.hafriyat.EnumHfFirmaTuru;
import tr.bel.gaziantep.bysweb.core.enums.hafriyat.EnumHfMalCinsi;
import tr.bel.gaziantep.bysweb.core.enums.hafriyat.EnumHfTahsilatTuru;
import tr.bel.gaziantep.bysweb.core.enums.moralevi.EnumMeTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.*;
import tr.bel.gaziantep.bysweb.core.enums.saglikhizmetleri.EnumShDanismanlikHizmeti;
import tr.bel.gaziantep.bysweb.core.enums.saglikhizmetleri.EnumShObeziteHizmet;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.*;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyBirim;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.service.AyBirimService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalepKonu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyTalepKonuService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIl;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlIlService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlIlceService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcuSablon;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtOlcuSablonService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 15:49
 */
@Stateless
public class FilterOptionService implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -8687696828652719888L;

    @Inject
    private GnlIlService gnlIlService;
    @Inject
    private GnlIlceService gnlIlceService;
    @Inject
    private EyTalepKonuService eyTalepKonuService;
    @Inject
    private AyBirimService ayBirimService;
    @Inject
    private OrtOlcuSablonService ortOlcuSablonService;

    public List<SelectItem> getSyFilterTurs() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumSyFiltreTuru value : EnumSyFiltreTuru.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getSyFilterAnahtars() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumSyFiltreAnahtari value : EnumSyFiltreAnahtari.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getSyVeriTips() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumSyVeriTipi value : EnumSyVeriTipi.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }
    public List<SelectItem> getSyFiltreEslestirmeMods() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumSyFiltreEslesmeModu value : EnumSyFiltreEslesmeModu.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getSyKullaniciTurus() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumSyKullaniciTuru value : EnumSyKullaniciTuru.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getGnlUyruks() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumGnlUyruk value : EnumGnlUyruk.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getGnlIlces() {
        List<SelectItem> result = new ArrayList<>();
        List<GnlIlce> ilceList = gnlIlceService.findByIlId(27);
        for (GnlIlce value : ilceList) {
            result.add(new SelectItem(value.getTanim(), value.getTanim()));
        }
        return result;
    }

    public List<SelectItem> getGnlIls() {
        List<SelectItem> result = new ArrayList<>();
        List<GnlIl> ilList = gnlIlService.findAll();
        for (GnlIl value : ilList) {
            result.add(new SelectItem(value.getTanim(), value.getTanim()));
        }
        return result;
    }

    public List<SelectItem> getGnlCinsiyets() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumGnlCinsiyet value : EnumGnlCinsiyet.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getGnlMedeniDurums() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumGnlMedeniDurum value : EnumGnlMedeniDurum.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getGnlKisiDurums() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumGnlDurum value : EnumGnlDurum.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getEklemeYeris() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumModul value : EnumModul.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getEyAnketDurumus() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumEyAnketDurumu value : EnumEyAnketDurumu.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getGnlTalepTurus() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumGnlTalepTuru value : EnumGnlTalepTuru.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getGnlTalepTipis() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumGnlTalepTipi value : EnumGnlTalepTipi.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getTalepDurumus() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumGnlTalepDurumu value : EnumGnlTalepDurumu.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getEyTalepKonus() {
        List<SelectItem> result = new ArrayList<>();
        List<EyTalepKonu> list = eyTalepKonuService.findAll();
        for (EyTalepKonu value : list) {
            result.add(new SelectItem(value.getTanim(), value.getTanim()));
        }
        return result;
    }

    public List<SelectItem> getEyAracBilgisis() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumEyAracBilgisi value : EnumEyAracBilgisi.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }
    public List<SelectItem> getEyAracArizaTurus() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumEyArizaTuru value : EnumEyArizaTuru.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getEvetHayirs() {
        List<SelectItem> result = new ArrayList<>();
        result.add(new SelectItem(true, "Evet"));
        result.add(new SelectItem(false, "Hayır"));
        return result;
    }

    public List<SelectItem> getEyCihazDurums() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumEyCihazDurum value : EnumEyCihazDurum.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getEyDegerlendirmeDurumus() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumEyDegerlendirmeDurumu value : EnumEyDegerlendirmeDurumu.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getGnlDevamDurumus() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumGnlDevamDurumu value : EnumGnlDevamDurumu.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getAyBirims() {
        List<SelectItem> result = new ArrayList<>();
        List<AyBirim> list = ayBirimService.findAll();
        for (AyBirim value : list) {
            result.add(new SelectItem(value.getTanim(), value.getTanim()));
        }
        return result;
    }

    public List<SelectItem> getEdbHizmetTurus() {
        List<SelectItem> result = new ArrayList<>();
        for (EnumEdbHizmetTuru value : EnumEdbHizmetTuru.values()) {
            result.add(new SelectItem(value, value.getDisplayValue()));
        }
        return result;
    }

    public List<SelectItem> getEdbBasvuruDurumus() {
        return Arrays.stream(EnumEdbBasvuruDurumu.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getShDanismanlikHizmetis() {
        return Arrays.stream(EnumShDanismanlikHizmeti.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getShObeziteHizmets() {
        return Arrays.stream(EnumShObeziteHizmet.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getSoruTurus() {
        return Arrays.stream(EnumSoruTuru.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getHfFirmaTurus() {
        return Arrays.stream(EnumHfFirmaTuru.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getHfMalCinsis() {
        return Arrays.stream(EnumHfMalCinsi.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getGnlBelediyes() {
        return Arrays.stream(EnumGnlBelediye.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getHfTahsilatTurs() {
        return Arrays.stream(EnumHfTahsilatTuru.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getSyDuyuruTurs() {
        return Arrays.stream(EnumSyDuyuruTur.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getGnlAnketTurus() {
        return Arrays.stream(EnumGnlAnketTuru.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getOrtEngelOlusums() {
        return Arrays.stream(EnumOrtEngelOlusum.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getOrtOlcuSablons() {
        List<SelectItem> result = new ArrayList<>();
        List<OrtOlcuSablon> list = ortOlcuSablonService.findAll();
        for (OrtOlcuSablon value : list) {
            result.add(new SelectItem(value.getTanim(), value.getTanim()));
        }
        return result;
    }

    public List<SelectItem> getOrtOlcuSAblonAlanTurus() {
        return Arrays.stream(EnumOrtSablonAlanTuru.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getOrtBasvuruDurums() {
        return Arrays.stream(EnumOrtBasvuruDurumu.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getOrtBasvuruHareketDurums() {
        return Arrays.stream(EnumOrtBasvuruHareketDurumu.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getOrtOlcuDurums() {
        return Arrays.stream(EnumOrtOlcuDurum.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }
    public List<SelectItem> getOrtStokBirims() {
        return Arrays.stream(EnumOrtStokBirim.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getOrtMalzemeTalepOnayDurums() {
        return Arrays.stream(EnumOrtMalzemeOnayDurumu.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getOrtFizikTedaviDurums() {
        return Arrays.stream(EnumOrtFizikTedaviDurum.values())
                .map(value -> new SelectItem(value, value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getMeTalepDurums() {
        return Arrays.stream(EnumMeTalepDurumu.values())
                .map(value -> new SelectItem(value,value.getDisplayValue()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getGnlGuns() {
        return Arrays.stream(EnumGnlGun.values())
                .map(value -> new SelectItem(value,value.getDisplayValue()))
                .collect(Collectors.toList());
    }
}
