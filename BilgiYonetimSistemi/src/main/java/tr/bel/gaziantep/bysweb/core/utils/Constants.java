package tr.bel.gaziantep.bysweb.core.utils;

import java.io.Serial;
import java.util.Locale;

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
}
