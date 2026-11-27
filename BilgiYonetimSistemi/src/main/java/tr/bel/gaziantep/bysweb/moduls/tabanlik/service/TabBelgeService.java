package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.primefaces.model.file.UploadedFile;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabBelgeTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Function;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabBelge;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 14:34
 */
@Stateless
public class TabBelgeService extends AbstractService<TabBelge> {

    @Serial
    private static final long serialVersionUID = -831951601141470158L;
    private static final long MAX_DOSYA_BOYUTU = 10L * 1024 * 1024;
    private static final Pattern IZINLI_UZANTI = Pattern.compile("(?i)\\.(pdf|jpe?g|png|doc|docx|xls|xlsx)$");

    public TabBelgeService() {
        super(TabBelge.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<TabBelge> findByTalep(TabTalep tabTalep) {
        if (tabTalep == null || tabTalep.getId() == null) {
            return List.of();
        }
        return getEntityManager()
                .createQuery("SELECT b FROM TabBelge b WHERE b.aktif=true AND b.tabTalep = :talep " +
                                "ORDER BY b.yuklenmeTarihi DESC, b.id DESC",
                        TabBelge.class)
                .setParameter("talep", tabTalep)
                .getResultList();
    }

    public List<TabBelge> findByTalepByType(TabTalep tabTalep, EnumTabBelgeTuru belgeTuru) {
        if (tabTalep == null || tabTalep.getId() == null || belgeTuru == null) {
            return List.of();
        }
        return getEntityManager()
                .createQuery("SELECT b FROM TabBelge b WHERE b.aktif=true AND b.tabTalep = :talep " +
                                "AND b.belgeTuru = :tur ORDER BY b.yuklenmeTarihi DESC, b.id DESC",
                        TabBelge.class)
                .setParameter("talep", tabTalep)
                .setParameter("tur", belgeTuru)
                .getResultList();
    }

    public boolean existsByType(TabTalep tabTalep, EnumTabBelgeTuru belgeTuru) {
        return !findByTalepByType(tabTalep, belgeTuru).isEmpty();
    }

    /**
     * Dosya türü/boyut kontrolü yapar, UUID'li fiziksel dosya adıyla diske yazar
     * ve belge kaydını oluşturur.
     */
    public TabBelge upload(TabTalep talep, EnumTabBelgeTuru belgeTuru, UploadedFile file, String aciklama) {
        if (talep == null || talep.getId() == null) {
            throw new IllegalArgumentException("Belge eklenecek talep bulunamadı.");
        }
        if (belgeTuru == null) {
            throw new IllegalArgumentException("Belge türü seçilmelidir.");
        }
        if (file == null || file.getContent() == null || file.getContent().length == 0) {
            throw new IllegalArgumentException("Yüklenecek dosya bulunamadı.");
        }
        if (file.getContent().length > MAX_DOSYA_BOYUTU) {
            throw new IllegalArgumentException("Dosya boyutu 10 MB'dan büyük olamaz.");
        }
        String originalName = StringUtil.isBlank(file.getFileName()) ? "dosya" : file.getFileName();
        if (!IZINLI_UZANTI.matcher(originalName).find()) {
            throw new IllegalArgumentException("İzin verilmeyen dosya türü. PDF, JPG, PNG, DOC, DOCX, XLS veya XLSX yükleyebilirsiniz.");
        }

        String tcKimlikNo = "bilinmiyor";
        if (talep.getTabKisi() != null && talep.getTabKisi().getGnlKisi() != null
                && !StringUtil.isBlank(talep.getTabKisi().getGnlKisi().getTcKimlikNo())) {
            tcKimlikNo = talep.getTabKisi().getGnlKisi().getTcKimlikNo();
        }
        tcKimlikNo = tcKimlikNo.replaceAll("[^0-9A-Za-z_-]", "_");
        String talepNo = StringUtil.isBlank(talep.getTalepNo())
                ? "TALEP-" + talep.getId()
                : talep.getTalepNo().replaceAll("[^0-9A-Za-z_-]", "_");

        String root = Function.isWindows()
                ? Constants.TABANLIK_TALEP_BELGE_KLASORU
                : Constants.TABANLIK_TALEP_BELGE_KLASORU_MAC;
        File talepKlasoru = new File(root + tcKimlikNo + File.separator + talepNo);
        if (!talepKlasoru.exists() && !talepKlasoru.mkdirs()) {
            throw new IllegalArgumentException("Belge klasörü oluşturulamadı: " + talepKlasoru.getAbsolutePath());
        }

        String extension = Function.getExtension(file);
        String fizikselAd = belgeTuru.name() + "_" + UUID.randomUUID().toString().replace("-", "") + extension;
        String dosyaYolu = talepKlasoru.getAbsolutePath() + File.separator + fizikselAd;

        try (InputStream in = file.getInputStream(); FileOutputStream out = new FileOutputStream(dosyaYolu)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("Dosya diske yazılamadı: " + ex.getMessage());
        }

        TabBelge belge = TabBelge.builder()
                .tabTalep(talep)
                .belgeTuru(belgeTuru)
                .dosyaAdi(originalName)
                .dosyaYolu(dosyaYolu)
                .mimeTuru(file.getContentType())
                .dosyaBoyutu((long) file.getContent().length)
                .yukleyen(Util.getSyKullanici())
                .yuklenmeTarihi(LocalDateTime.now())
                .aciklama(aciklama)
                .build();
        create(belge);
        flush();
        return belge;
    }
}

