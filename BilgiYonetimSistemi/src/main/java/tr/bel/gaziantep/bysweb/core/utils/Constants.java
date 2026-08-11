package tr.bel.gaziantep.bysweb.core.utils;

import java.io.Serial;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:35
 */
public class Constants implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -7261898663529813594L;

    public static final Locale LOCALE = new Locale("tr", "TR");
    public static final String PERSISTENCE_NAME = "BYS";
    public static final String UNIT_NAME = "BYS_PU";
    public static final String POOL_NAME = "BYSPool";
    public static final String HATA_OLUSTU = "hataOlustu";
    public static final String KAYIT_EKLENDI = "kayitEklendi";
    public static final String KAYIT_GUNCELLENDI = "kayitGuncellendi";
    public static final String KAYIT_SILINDI = "kayitSilindi";
    public static final String AKTIF = "aktif";
    public static final String SESSION_ID = "sessionId";
    public static final String USER_COUNTER = "userCounter";
    public static final String KAYIT_EKLENIRKEN_GUNCELLENIRKEN_HATA_OLUSTU = "kayitEklenirkenGuncellenirkenHataOlustu";
    public static final String PAROLA_DEGISTIRILMESI_GEREKLI = "parolaDegistirilmesiGerekli";
    public static final String CP1254="Cp1254";

    public static final String ENGELSIZLER_KISI_DOSYALARI = "C:\\BYS\\Engelsizler\\Dosyalar\\";
    public static final String ENGELSIZLER_KISI_DOSYALARI_MAC = "/Users/omerfarukkurt/BYS/Engelsizler/Dosyalar/";
    public static final String AKTIF_YASAM_ETKINLIK_RESIMLERI = "C:\\BYS\\AktifYasam\\Etkinlik\\";
    public static final String AKTIF_YASAM_ETKINLIK_RESIMLERI_MAC = "/Users/omerfarukkurt/BYS/AktifYasam/Etkinlik/";
    public static final String MORAL_EVI_ETKINLIK_RESIMLERI = "C:\\BYS\\MoralEvi\\Etkinlik\\";
    public static final String MORAL_EVI_ETKINLIK_RESIMLERI_MAC = "/Users/omerfarukkurt/BYS/MoralEvi/Etkinlik/";
    public static final String EVDE_YASAM_VERILECEK_HIZMETLER_KLASORU = "C:\\BYS\\EvdeYasam\\VerilecekHizmetler\\";
    public static final String EVDE_YASAM_VERILECEK_HIZMETLER_KLASORU_MAC = "/Users/omerfarukkurt/BYS/EvdeYasam/VerilecekHizmetler/";
    public static final String ORTEZ_PROTEZ_OLCU_SABLON_KLASORU = "C:\\BYS\\OrtezProtez\\OlcuSablon\\";
    public static final String ORTEZ_PROTEZ_OLCU_SABLON_KLASORU_MAC = "/Users/omerfarukkurt/BYS/OrtezProtez/OlcuSablon/";
    public static final String ORTEZ_PROTEZ_RAPOR_RECETE_KLASORU = "C:\\BYS\\OrtezProtez\\RaporRecete\\";
    public static final String ORTEZ_PROTEZ_RAPOR_RECETE_KLASORU_MAC = "/Users/omerfarukkurt/BYS/OrtezProtez/RaporRecete/";
    public static final String ORTEZ_PROTEZ_BASVURU_DOSYA_KLASORU = "C:\\BYS\\OrtezProtez\\BasvuruDosya\\";
    public static final String ORTEZ_PROTEZ_BASVURU_DOSYA_KLASORU_MAC = "/Users/omerfarukkurt/BYS/OrtezProtez/BasvuruDosya/";

    public static final Map<String, String> THEMES ;
    public static final Map<String, String> GRAPHIC_THEMES ;

    static {
        Map<String, String> themes = new LinkedHashMap<>();
        themes.put("arya-blue", "Arya Blue");
        themes.put("bootstrap4-blue-dark", "Bootstrap4 Blue Dark");
        themes.put("bootstrap4-blue-light", "Bootstrap4 Blue Light");
        themes.put("bootstrap4-purple-dark", "Bootstrap4 Purple Dark");
        themes.put("bootstrap4-purple-light", "Bootstrap4 Purple Light");
        themes.put("luna-amber", "Luna Amber");
        themes.put("luna-blue", "Luna Blue");
        themes.put("luna-green", "Luna Green");
        themes.put("luna-pink", "Luna Pink");
        themes.put("material-compact-deeppurple-dark", "Material Compact Deep Purple Dark");
        themes.put("material-compact-deeppurple-light", "Material Compact Deep Purple Light");
        themes.put("material-compact-indigo-dark", "Material Compact Indigo Dark");
        themes.put("material-compact-indigo-light", "Material Compact Indigo Light");
        themes.put("material-deeppurple-dark", "Material Deep Purple Dark");
        themes.put("material-deeppurple-light", "Material Deep Purple Light");
        themes.put("material-indigo-dark", "Material Indigo Dark");
        themes.put("material-indigo-light", "Material Indigo Light");
        themes.put("nova-colored", "Nova Colored");
        themes.put("nova-dark", "Nova Dark");
        themes.put("nova-light", "Nova Light");
        themes.put("saga-blue", "Saga Blue");
        themes.put("vela-blue", "Vela Blue");



        THEMES = Collections.unmodifiableMap(themes);

        Map<String, String> graphicThemes = new LinkedHashMap<>();
        graphicThemes.put("dark", "Dark");
        graphicThemes.put("dataviz", "Dataviz");
        graphicThemes.put("frozen", "Frozen");
        graphicThemes.put("kelly", "Kelly");
        graphicThemes.put("material", "Material");
        graphicThemes.put("moonrisekingdom", "Moon Rise Kingdom");
        graphicThemes.put("spiritedaway", "Spirited Away");
        GRAPHIC_THEMES = Collections.unmodifiableMap(graphicThemes);
    }
}
