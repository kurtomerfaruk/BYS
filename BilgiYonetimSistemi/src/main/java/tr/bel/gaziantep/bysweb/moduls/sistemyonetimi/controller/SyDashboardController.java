package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import tr.bel.gaziantep.bysweb.core.controller.InitApp;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.dto.SyDashboardModulDTO;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyGenelAyar;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullaniciGiris;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullaniciGirisSayfa;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyDashboardService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyGenelAyarService;

import java.io.File;
import java.io.Serial;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.25.0
 * @since 27.10.2026 11:20
 */
@Named
@ViewScoped
@Slf4j
public class SyDashboardController implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -9030926094097110647L;

    private static final int TREND_GUN_SAYISI = 14;
    private static final int MODUL_GUN_SAYISI = 30;
    private static final int TABLO_KAYIT_SAYISI = 10;
    private static final long HATALI_GIRIS_KRITIK_ESIGI = 10L;
    private static final DateTimeFormatter ETIKET_FORMAT = DateTimeFormatter.ofPattern("dd.MM");

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    /**
     * Modül manifesti: EnumModul -> {link ön eki, ikon, renk}.
     * Yeni modül eklendiğinde buraya yapılacak tek satırlık ekleme panelin
     * modül grid'ini ve grafiklerini otomatik ölçeklendirir.
     */
    private static final Map<EnumModul, String[]> MODUL_MANIFEST = new LinkedHashMap<>();

    static {
        MODUL_MANIFEST.put(EnumModul.GENEL, new String[]{"genel", "ti ti-users", "primary"});
        MODUL_MANIFEST.put(EnumModul.ENGELSIZLER, new String[]{"engelsizler", "ti ti-accessible", "info"});
        MODUL_MANIFEST.put(EnumModul.AKTIF_YASAM, new String[]{"aktifyasam", "ti ti-heartbeat", "success"});
        MODUL_MANIFEST.put(EnumModul.SAGLIK_HIZMETLERI, new String[]{"saglikhizmetleri", "ti ti-stethoscope", "danger"});
        MODUL_MANIFEST.put(EnumModul.MORAL_EVI, new String[]{"moralevi", "ti ti-mood-smile", "purple"});
        MODUL_MANIFEST.put(EnumModul.EDB, new String[]{"evimdunyalarabedel", "ti ti-home-heart", "orange"});
        MODUL_MANIFEST.put(EnumModul.ILERI_YAS, new String[]{"ileriyas", "ti ti-old", "indigo"});
        MODUL_MANIFEST.put(EnumModul.EKM, new String[]{"engelsizkariyermerkezi", "ti ti-briefcase", "teal"});
        MODUL_MANIFEST.put(EnumModul.ORTEZ_PROTEZ, new String[]{"ortezprotez", "ti ti-crutches", "pink"});
        MODUL_MANIFEST.put(EnumModul.PSIKIYATRI_KLINIGI, new String[]{"psikiyatriklinigi", "ti ti-brain", "teal"});
        MODUL_MANIFEST.put(EnumModul.HAFRIYAT, new String[]{"hafriyat", "ti ti-truck", "secondary"});
        MODUL_MANIFEST.put(EnumModul.SISTEM_YONETIMI, new String[]{"sistemyonetimi", "ti ti-settings-cog", "warning"});
    }

    private static final List<String> GRAFIK_RENKLERI = List.of(
            "rgba(58,122,224,0.85)",
            "rgba(89,194,148,0.85)",
            "rgba(246,153,63,0.85)",
            "rgba(228,77,99,0.85)",
            "rgba(149,97,226,0.85)",
            "rgba(69,170,242,0.85)",
            "rgba(247,103,173,0.85)",
            "rgba(13,148,136,0.85)",
            "rgba(245,201,15,0.85)",
            "rgba(108,117,125,0.85)",
            "rgba(94,168,45,0.85)",
            "rgba(0,150,199,0.85)"
    );

    @Inject
    private SyDashboardService syDashboardService;
    @Inject
    private InitApp initApp;
    @Inject
    private SyGenelAyarService syGenelAyarService;

    @Getter
    @Setter
    private long toplamKullanici;
    @Getter
    @Setter
    private long bugunGirisYapan;
    @Getter
    @Setter
    private long dunGirisYapan;
    @Getter
    @Setter
    private long bugunSayfaHareketi;
    @Getter
    @Setter
    private long hataliGiris24Saat;
    @Getter
    @Setter
    private long kilitliKullanici;
    @Getter
    @Setter
    private long parolaBekleyenKullanici;

    @Getter
    @Setter
    private int sistemCpuYuzdesi;
    @Getter
    @Setter
    private int jvmCpuYuzdesi;
    @Getter
    @Setter
    private int ramYuzdesi;
    @Getter
    @Setter
    private String ramKullanimMetni;
    @Getter
    @Setter
    private String ramBosMetni;
    @Getter
    @Setter
    private int heapYuzdesi;
    @Getter
    @Setter
    private String heapKullanimMetni;
    @Getter
    @Setter
    private int diskYuzdesi;
    @Getter
    @Setter
    private String diskKullanimMetni;
    @Getter
    @Setter
    private String diskBosMetni;
    @Getter
    @Setter
    private int threadSayisi;
    @Getter
    @Setter
    private int cekirdekSayisi;
    @Getter
    @Setter
    private String calismaSuresi;

    @Getter
    @Setter
    private String trendChartJson;
    @Getter
    @Setter
    private String rolChartJson;
    @Getter
    @Setter
    private String modulChartJson;

    @Getter
    @Setter
    private String sistemSaglikChartJson;

    @Getter
    @Setter
    private List<SyDashboardModulDTO> modulOzetleri;
    @Getter
    @Setter
    private List<SyKullaniciGiris> sonGirisler;
    @Getter
    @Setter
    private List<SyKullaniciGirisSayfa> sonSayfaHareketleri;
    @Getter
    @Setter
    private int maxUser=0;
    @Getter
    @Setter
    private String maxUserDate;

    private final java.util.Deque<Integer> sistemCpuGecmisi = new java.util.ArrayDeque<>();
    private final java.util.Deque<Integer> sistemRamGecmisi = new java.util.ArrayDeque<>();
    private final java.util.Deque<Integer> sistemHeapGecmisi = new java.util.ArrayDeque<>();
    private final java.util.Deque<Integer> sistemDiskGecmisi = new java.util.ArrayDeque<>();
    private final java.util.Deque<String> sistemZamanGecmisi = new java.util.ArrayDeque<>();
    private static final int SISTEM_GECMIS_SAYISI = 30;

    @PostConstruct
    public void init() {
        yukle();
    }

    public void yukle() {
        try {
            sistemDurumuYukle();

            LocalDate bugun = LocalDate.now();
            LocalDateTime trendBaslangic = bugun.minusDays(TREND_GUN_SAYISI - 1L).atStartOfDay();
            LocalDateTime modulBaslangic = bugun.minusDays(MODUL_GUN_SAYISI - 1L).atStartOfDay();

            toplamKullanici = syDashboardService.toplamKullaniciSayisi();
            kilitliKullanici = syDashboardService.kilitliKullaniciSayisi();
            parolaBekleyenKullanici = syDashboardService.parolaDegistirilecekKullaniciSayisi();
            hataliGiris24Saat = syDashboardService.hataliGirisSayisi(LocalDateTime.now().minusHours(24));

            List<Object[]> girisKayitlari = syDashboardService.basariliGirisKayitlari(trendBaslangic);
            List<LocalDateTime> sayfaHareketZamanlari = syDashboardService.sayfaHareketZamanlari(trendBaslangic);

            Map<LocalDate, Set<Integer>> gunlukKullanicilar = new HashMap<>();
            for (Object[] kayit : girisKayitlari) {
                LocalDateTime zaman = (LocalDateTime) kayit[0];
                Integer kullaniciId = (Integer) kayit[1];
                if (zaman != null && kullaniciId != null) {
                    gunlukKullanicilar.computeIfAbsent(zaman.toLocalDate(), k -> new HashSet<>()).add(kullaniciId);
                }
            }
            Map<LocalDate, Long> gunlukSayfaHareketleri = new HashMap<>();
            for (LocalDateTime zaman : sayfaHareketZamanlari) {
                if (zaman != null) {
                    gunlukSayfaHareketleri.merge(zaman.toLocalDate(), 1L, Long::sum);
                }
            }

            bugunGirisYapan = gunlukKullanicilar.getOrDefault(bugun, Set.of()).size();
            dunGirisYapan = gunlukKullanicilar.getOrDefault(bugun.minusDays(1), Set.of()).size();
            bugunSayfaHareketi = gunlukSayfaHareketleri.getOrDefault(bugun, 0L);

            trendChartJson = trendGrafikOlustur(gunlukKullanicilar, gunlukSayfaHareketleri, bugun);
            rolChartJson = rolGrafikOlustur();
            modulOzetleri = modulOzetleriOlustur(modulBaslangic);
            modulChartJson = modulGrafikOlustur(modulOzetleri);

            sonGirisler = syDashboardService.sonGirisler(TABLO_KAYIT_SAYISI);
            sonSayfaHareketleri = syDashboardService.sonSayfaHareketleri(TABLO_KAYIT_SAYISI);
            SyGenelAyar ayar =syGenelAyarService.findByTanim("MaxUser");
            if (ayar != null) {
                maxUser =Integer.parseInt( ayar.getDeger());
                SyGenelAyar maxUserDateAyar =syGenelAyarService.findByTanim("MaxUserDate");
                maxUserDate = maxUserDateAyar.getDeger();
            }
            PrimeFaces.current().executeScript("rcDashboard()");
        } catch (Exception e) {
            log.error("Sistem paneli verileri yüklenirken hata oluştu.", e);
        }
    }

    public void yenile() {
        yukle();
    }

    /**
     * Sistem sağlığı panelinin hızlı yenileme dinleyicisi (10 sn p:poll).
     * Yalnızca işletim sistemi / JVM ölçümlerini tazeler, veritabanına dokunmaz.
     */
    public void yenileSistemDurumu() {
        sistemDurumuYukle();
    }

    /**
     * İşletim sistemi, JVM ve disk kaynaklarını anlık okur.
     * com.sun.management.OperatingSystemMXBean JDK 14+ API'lerini kullanır
     * (getCpuLoad, getTotalMemorySize, getFreeMemorySize).
     */
    private void sistemDurumuYukle() {
        try {
            com.sun.management.OperatingSystemMXBean isBean =
                    (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            sistemCpuYuzdesi = yuzdeyeCevir(isBean.getCpuLoad());
            jvmCpuYuzdesi = yuzdeyeCevir(isBean.getProcessCpuLoad());
            cekirdekSayisi = isBean.getAvailableProcessors();

            long ramToplam = isBean.getTotalMemorySize();
            long ramBos = isBean.getFreeMemorySize();
            long ramKullanilan = Math.max(0, ramToplam - ramBos);
            ramYuzdesi = oranYuzdesi(ramKullanilan, ramToplam);
            ramKullanimMetni = boyutMetni(ramKullanilan) + " / " + boyutMetni(ramToplam);
            ramBosMetni = boyutMetni(ramBos) + " boş";

            MemoryUsage heap = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
            heapYuzdesi = heap.getMax() > 0 ? oranYuzdesi(heap.getUsed(), heap.getMax()) : 0;
            heapKullanimMetni = heap.getMax() > 0
                    ? boyutMetni(heap.getUsed()) + " / " + boyutMetni(heap.getMax())
                    : boyutMetni(heap.getUsed()) + " / sınır yok";

            File kokDizin = new File("/");
            long diskToplam = kokDizin.getTotalSpace();
            long diskBos = kokDizin.getUsableSpace();
            long diskKullanilan = Math.max(0, diskToplam - diskBos);
            diskYuzdesi = oranYuzdesi(diskKullanilan, diskToplam);
            diskKullanimMetni = boyutMetni(diskKullanilan) + " / " + boyutMetni(diskToplam);
            diskBosMetni = boyutMetni(diskBos) + " boş";

            threadSayisi = ManagementFactory.getThreadMXBean().getThreadCount();
            calismaSuresi = calismaSuresiBicimlendir(ManagementFactory.getRuntimeMXBean().getUptime());

            sistemCpuGecmisi.addLast(sistemCpuYuzdesi);
            sistemRamGecmisi.addLast(ramYuzdesi);
            sistemHeapGecmisi.addLast(heapYuzdesi);
            sistemDiskGecmisi.addLast(diskYuzdesi);
            sistemZamanGecmisi.addLast(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));

            while (sistemCpuGecmisi.size() > SISTEM_GECMIS_SAYISI) {
                sistemCpuGecmisi.pollFirst();
                sistemRamGecmisi.pollFirst();
                sistemHeapGecmisi.pollFirst();
                sistemDiskGecmisi.pollFirst();
                sistemZamanGecmisi.pollFirst();
            }
            sistemSaglikChartJson = sistemSaglikGrafikOlustur();
        } catch (Exception e) {
            log.error("Sistem sağlığı ölçümleri okunurken hata oluştu.", e);
        }
    }

    private int yuzdeyeCevir(double deger) {
        if (deger < 0) {
            return 0;
        }
        return (int) Math.round(Math.min(deger, 1.0) * 100);
    }

    private int oranYuzdesi(long kullanilan, long toplam) {
        if (toplam <= 0) {
            return 0;
        }
        return (int) Math.round(kullanilan * 100.0 / toplam);
    }

    private String boyutMetni(long bayt) {
        double gb = bayt / 1073741824.0;
        if (gb >= 1.0) {
            return String.format(Locale.US, "%.1f GB", gb);
        }
        return String.format(Locale.US, "%d MB", bayt / 1048576L);
    }

    private String calismaSuresiBicimlendir(long milisaniye) {
        long gun = milisaniye / 86400000L;
        long saat = (milisaniye % 86400000L) / 3600000L;
        long dakika = (milisaniye % 3600000L) / 60000L;
        if (gun > 0) {
            return gun + " gün " + saat + " sa " + dakika + " dk";
        }
        if (saat > 0) {
            return saat + " sa " + dakika + " dk";
        }
        return dakika + " dk";
    }

    public String getCpuRenk() {
        return durumRengi(sistemCpuYuzdesi);
    }

    public String getRamRenk() {
        return durumRengi(ramYuzdesi);
    }

    public String getHeapRenk() {
        return durumRengi(heapYuzdesi);
    }

    public String getDiskRenk() {
        return durumRengi(diskYuzdesi);
    }

    private String durumRengi(int yuzde) {
        if (yuzde >= 90) {
            return "danger";
        }
        if (yuzde >= 70) {
            return "warning";
        }
        return "success";
    }

    /**
     * Sistem kaynaklarından herhangi biri kritik eşikte mi?
     * Kritik uyarı bandına da dahil edilir.
     */
    public boolean isSistemKaynaklariKritik() {
        return sistemCpuYuzdesi >= 90 || ramYuzdesi >= 90 || diskYuzdesi >= 90 || heapYuzdesi >= 95;
    }


    public long getAktifOturum() {
        return initApp.getSyKullanicis() != null ? initApp.getSyKullanicis().size() : 0;
    }

    public boolean isKritikUyariVar() {
        return kilitliKullanici > 0 || hataliGiris24Saat >= HATALI_GIRIS_KRITIK_ESIGI || isSistemKaynaklariKritik();
    }

    public String getKritikUyariMetni() {
        List<String> parcalar = new ArrayList<>();
        if (kilitliKullanici > 0) {
            parcalar.add(kilitliKullanici + " kilitli hesap");
        }
        if (hataliGiris24Saat >= HATALI_GIRIS_KRITIK_ESIGI) {
            parcalar.add("son 24 saatte " + hataliGiris24Saat + " hatalı giriş denemesi");
        }
        if (sistemCpuYuzdesi >= 90) {
            parcalar.add("sistem CPU %" + sistemCpuYuzdesi);
        }
        if (ramYuzdesi >= 90) {
            parcalar.add("RAM kullanımı %" + ramYuzdesi);
        }
        if (heapYuzdesi >= 95) {
            parcalar.add("JVM heap kullanımı %" + heapYuzdesi);
        }
        if (diskYuzdesi >= 90) {
            parcalar.add("disk kullanımı %" + diskYuzdesi);
        }
        return String.join(", ", parcalar) + " — derhal incelenmelidir.";
    }

    public String getGirisTrendYonu() {
        if (dunGirisYapan == 0) {
            return "";
        }
        return bugunGirisYapan >= dunGirisYapan ? "up" : "down";
    }

    private String trendGrafikOlustur(Map<LocalDate, Set<Integer>> gunlukKullanicilar,
                                      Map<LocalDate, Long> gunlukSayfaHareketleri, LocalDate bugun) {
        List<String> etiketler = new ArrayList<>();
        List<Number> girisSayilari = new ArrayList<>();
        List<Number> sayfaHareketSayilari = new ArrayList<>();
        for (int i = TREND_GUN_SAYISI - 1; i >= 0; i--) {
            LocalDate gun = bugun.minusDays(i);
            etiketler.add(gun.format(ETIKET_FORMAT));
            girisSayilari.add(gunlukKullanicilar.getOrDefault(gun, Set.of()).size());
            sayfaHareketSayilari.add(gunlukSayfaHareketleri.getOrDefault(gun, 0L));
        }

        Map<String, Object> girisDataset = new LinkedHashMap<>();
        girisDataset.put("label", "Giriş Yapan Kullanıcı");
        girisDataset.put("data", girisSayilari);
        girisDataset.put("borderColor", "rgba(58,122,224,1)");
        girisDataset.put("backgroundColor", "rgba(58,122,224,0.15)");
        girisDataset.put("fill", true);
        girisDataset.put("tension", 0.3);

        Map<String, Object> hareketDataset = new LinkedHashMap<>();
        hareketDataset.put("label", "Sayfa Hareketi");
        hareketDataset.put("data", sayfaHareketSayilari);
        hareketDataset.put("borderColor", "rgba(89,194,148,1)");
        hareketDataset.put("backgroundColor", "rgba(89,194,148,0.15)");
        hareketDataset.put("fill", true);
        hareketDataset.put("tension", 0.3);

        return grafikConfigOlustur("line", etiketler, List.of(girisDataset, hareketDataset), temelSecenekler(null));
    }

    private String rolGrafikOlustur() {
        List<Object[]> rolDagilimi = syDashboardService.rolDagilimi();
        List<String> etiketler = new ArrayList<>();
        List<Number> sayilar = new ArrayList<>();
        List<String> renkler = new ArrayList<>();
        int renkIndex = 0;
        for (Object[] kayit : rolDagilimi) {
            String rolAdi = (String) kayit[0];
            Long adet = (Long) kayit[1];
            if (rolAdi != null && adet != null) {
                etiketler.add(rolAdi);
                sayilar.add(adet);
                renkler.add(GRAFIK_RENKLERI.get(renkIndex % GRAFIK_RENKLERI.size()));
                renkIndex++;
            }
        }
        if (etiketler.isEmpty()) {
            return "";
        }

        Map<String, Object> dataset = new LinkedHashMap<>();
        dataset.put("label", "Kullanıcı");
        dataset.put("data", sayilar);
        dataset.put("backgroundColor", renkler);
        dataset.put("borderColor", "rgba(255,255,255,0.7)");
        dataset.put("borderWidth", 2);

        return grafikConfigOlustur("doughnut", etiketler, List.of(dataset), temelSecenekler(null));
    }

    private List<SyDashboardModulDTO> modulOzetleriOlustur(LocalDateTime baslangic) {
        Map<String, EnumModul> onEkModulEslesmesi = new HashMap<>();
        MODUL_MANIFEST.forEach((modul, bilgi) -> onEkModulEslesmesi.put(bilgi[0], modul));

        List<Object[]> kayitlar = syDashboardService.sayfaHareketKayitlari(baslangic);
        Map<EnumModul, long[]> istatistikler = new HashMap<>();
        Map<EnumModul, Set<Integer>> modulKullanicilari = new HashMap<>();
        Map<EnumModul, LocalDateTime> sonErisimler = new HashMap<>();
        for (Object[] kayit : kayitlar) {
            String link = (String) kayit[0];
            Integer kullaniciId = (Integer) kayit[1];
            LocalDateTime zaman = (LocalDateTime) kayit[2];
            EnumModul modul = modulBul(onEkModulEslesmesi, link);
            if (modul == null) {
                continue;
            }
            long[] sayilar = istatistikler.computeIfAbsent(modul, k -> new long[1]);
            sayilar[0]++;
            if (kullaniciId != null) {
                modulKullanicilari.computeIfAbsent(modul, k -> new HashSet<>()).add(kullaniciId);
            }
            if (zaman != null) {
                sonErisimler.merge(modul, zaman, (eski, yeni) -> yeni.isAfter(eski) ? yeni : eski);
            }
        }

        List<SyDashboardModulDTO> ozetler = new ArrayList<>();
        MODUL_MANIFEST.forEach((modul, bilgi) -> {
            long[] sayilar = istatistikler.getOrDefault(modul, new long[1]);
            Set<Integer> kullanicilar = modulKullanicilari.getOrDefault(modul, Set.of());
            ozetler.add(SyDashboardModulDTO.builder()
                    .modul(modul)
                    .ikon(bilgi[1])
                    .renk(bilgi[2])
                    .ziyaretSayisi(sayilar[0])
                    .kullaniciSayisi(kullanicilar.size())
                    .sonErisim(sonErisimler.get(modul))
                    .build());
        });
        return ozetler;
    }

    private EnumModul modulBul(Map<String, EnumModul> onEkModulEslesmesi, String link) {
        if (link == null || !link.startsWith("/moduls/")) {
            return null;
        }
        String[] parcalar = link.split("/");
        if (parcalar.length < 3) {
            return null;
        }
        return onEkModulEslesmesi.get(parcalar[2]);
    }

    private String modulGrafikOlustur(List<SyDashboardModulDTO> ozetler) {
        List<SyDashboardModulDTO> kullanilanlar = ozetler.stream()
                .filter(SyDashboardModulDTO::isKullanilmis)
                .sorted(Comparator.comparingLong(SyDashboardModulDTO::getZiyaretSayisi).reversed())
                .toList();
        if (kullanilanlar.isEmpty()) {
            return "";
        }

        List<String> etiketler = new ArrayList<>();
        List<Number> sayilar = new ArrayList<>();
        List<String> renkler = new ArrayList<>();
        int renkIndex = 0;
        for (SyDashboardModulDTO ozet : kullanilanlar) {
            etiketler.add(ozet.getModulAdi());
            sayilar.add(ozet.getZiyaretSayisi());
            renkler.add(GRAFIK_RENKLERI.get(renkIndex % GRAFIK_RENKLERI.size()));
            renkIndex++;
        }

        Map<String, Object> dataset = new LinkedHashMap<>();
        dataset.put("label", "Sayfa Hareketi");
        dataset.put("data", sayilar);
        dataset.put("backgroundColor", renkler);

        Map<String, Object> secenekler = temelSecenekler("y");
        ((Map<String, Object>) secenekler.get("plugins")).put("legend", Map.of("display", false));
        return grafikConfigOlustur("bar", etiketler, List.of(dataset), secenekler);
    }

    private String sistemSaglikGrafikOlustur() {
        if (sistemZamanGecmisi.isEmpty()) {
            return "";
        }
        List<String> etiketler = new ArrayList<>(sistemZamanGecmisi);
        List<Number> cpu = new ArrayList<>(sistemCpuGecmisi);
        List<Number> ram = new ArrayList<>(sistemRamGecmisi);
        List<Number> heap = new ArrayList<>(sistemHeapGecmisi);
        List<Number> disk = new ArrayList<>(sistemDiskGecmisi);

        Map<String, Object> cpuDataset = new LinkedHashMap<>();
        cpuDataset.put("label", "CPU (%)");
        cpuDataset.put("data", cpu);
        cpuDataset.put("borderColor", "rgba(228,77,99,1)");
        cpuDataset.put("backgroundColor", "rgba(228,77,99,0.12)");
        cpuDataset.put("fill", true);
        cpuDataset.put("tension", 0.35);
        cpuDataset.put("pointRadius", 0);
        cpuDataset.put("borderWidth", 2);

        Map<String, Object> ramDataset = new LinkedHashMap<>();
        ramDataset.put("label", "RAM (%)");
        ramDataset.put("data", ram);
        ramDataset.put("borderColor", "rgba(58,122,224,1)");
        ramDataset.put("backgroundColor", "rgba(58,122,224,0.10)");
        ramDataset.put("fill", true);
        ramDataset.put("tension", 0.35);
        ramDataset.put("pointRadius", 0);
        ramDataset.put("borderWidth", 2);

        Map<String, Object> heapDataset = new LinkedHashMap<>();
        heapDataset.put("label", "Heap (%)");
        heapDataset.put("data", heap);
        heapDataset.put("borderColor", "rgba(246,153,63,1)");
        heapDataset.put("backgroundColor", "rgba(246,153,63,0.10)");
        heapDataset.put("fill", true);
        heapDataset.put("tension", 0.35);
        heapDataset.put("pointRadius", 0);
        heapDataset.put("borderWidth", 2);

        Map<String, Object> diskDataset = new LinkedHashMap<>();
        diskDataset.put("label", "Disk (%)");
        diskDataset.put("data", disk);
        diskDataset.put("borderColor", "rgba(89,194,148,1)");
        diskDataset.put("backgroundColor", "rgba(89,194,148,0.10)");
        diskDataset.put("fill", true);
        diskDataset.put("tension", 0.35);
        diskDataset.put("pointRadius", 0);
        diskDataset.put("borderWidth", 2);

        Map<String, Object> secenekler = temelSecenekler(null);
        Map<String, Object> yEksen = new LinkedHashMap<>();
        yEksen.put("min", 0);
        yEksen.put("max", 100);
        yEksen.put("ticks", Map.of("stepSize", 20));
        Map<String, Object> scales = (Map<String, Object>) secenekler.get("scales");
        scales.put("y", yEksen);
        return grafikConfigOlustur("line", etiketler, List.of(cpuDataset, ramDataset, heapDataset, diskDataset), secenekler);
    }

    /**
     * Chart.js temel seçenekleri: responsive, en-boy oranını korumama,
     * alt konumda lejant ve opsiyonel indexAxis (y = yatay çubuk).
     */
    private Map<String, Object> temelSecenekler(String indexAxis) {
        Map<String, Object> secenekler = new LinkedHashMap<>();
        if (indexAxis != null) {
            secenekler.put("indexAxis", indexAxis);
        }
        secenekler.put("responsive", true);
        secenekler.put("maintainAspectRatio", false);
        Map<String, Object> plugins = new LinkedHashMap<>();
        plugins.put("legend", Map.of("position", "bottom"));
        secenekler.put("plugins", plugins);
        secenekler.put("scales", new LinkedHashMap<String, Object>());
        return secenekler;
    }

    private String grafikConfigOlustur(String tip, List<String> etiketler,
                                       List<Map<String, Object>> datasetler, Map<String, Object> secenekler) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("labels", etiketler);
        data.put("datasets", datasetler);

        Map<String, Object> config = new LinkedHashMap<>();
        config.put("type", tip);
        config.put("data", data);
        config.put("options", secenekler);

        try {
            return JSON_MAPPER.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            log.error("Grafik JSON'u üretilirken hata oluştu.", e);
            return "";
        }
    }

    public boolean getHataliGiris(SyKullaniciGiris giris) {
        return giris != null && "Hatalı giriş".equals(giris.getAciklama());
    }
}
