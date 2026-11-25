package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.primefaces.model.file.UploadedFile;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBelgeTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Function;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalepBelge;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 25.11.2026 14:11
 */
@Stateless
public class OrtTalepBelgeService extends AbstractService<OrtTalepBelge> {

    @Serial
    private static final long serialVersionUID = 7200493103883500229L;

    public OrtTalepBelgeService() {
        super(OrtTalepBelge.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<OrtTalepBelge> findByTalep(OrtTalep talep) {
        if (talep == null || talep.getId() == null) {
            return List.of();
        }
        return getEntityManager().createQuery(
                        "SELECT b FROM OrtTalepBelge b WHERE b.aktif=true AND b.ortTalep.id=:talepId ORDER BY b.yuklenmeTarihi DESC",
                        OrtTalepBelge.class)
                .setParameter("talepId", talep.getId())
                .getResultList();
    }

    public List<OrtTalepBelge> findByTalepByType(OrtTalep talep, EnumOrtBelgeTuru belgeTuru) {
        if (talep == null || talep.getId() == null || belgeTuru == null) {
            return List.of();
        }
        return getEntityManager().createQuery(
                        "SELECT b FROM OrtTalepBelge b WHERE b.aktif=true AND b.ortTalep.id=:talepId " +
                                "AND b.belgeTuru=:tur ORDER BY b.yuklenmeTarihi DESC",
                        OrtTalepBelge.class)
                .setParameter("talepId", talep.getId())
                .setParameter("tur", belgeTuru)
                .getResultList();
    }

    public boolean existsByType(OrtTalep talep, EnumOrtBelgeTuru belgeTuru) {
        return !findByTalepByType(talep, belgeTuru).isEmpty();
    }

    /**
     * Dosyayı diske yazar ve belge kaydını oluşturur.
     *
     * @return kaydedilen belge
     */
    public OrtTalepBelge upload(OrtTalep talep, EnumOrtBelgeTuru belgeTuru, UploadedFile file, String aciklama) {
        if (talep == null || talep.getId() == null) {
            throw new IllegalArgumentException("Belge eklenecek talep bulunamadı.");
        }
        if (belgeTuru == null) {
            throw new IllegalArgumentException("Belge türü seçilmelidir.");
        }
        if (file == null || file.getContent() == null || file.getContent().length == 0) {
            throw new IllegalArgumentException("Yüklenecek dosya bulunamadı.");
        }

        String tcKimlikNo = talep.getOrtKisi() == null || talep.getOrtKisi().getGnlKisi() == null
                ? "bilinmiyor" : talep.getOrtKisi().getGnlKisi().getTcKimlikNo();
        if (tcKimlikNo == null || tcKimlikNo.isBlank()) {
            tcKimlikNo = "bilinmiyor";
        }
        tcKimlikNo = tcKimlikNo.replaceAll("[^0-9A-Za-z_-]", "_");

        String root = Function.isWindows()
                ? Constants.ORTEZ_PROTEZ_TALEP_BELGE_KLASORU
                : Constants.ORTEZ_PROTEZ_TALEP_BELGE_KLASORU_MAC;
        File personFolder = new File(root + tcKimlikNo);
        if (!personFolder.exists() && !personFolder.mkdirs()) {
            throw new IllegalArgumentException("Belge klasörü oluşturulamadı: " + personFolder.getAbsolutePath());
        }

        String extension = Function.getExtension(file);
        String dosyaAdi = belgeTuru.name() + "_" + UUID.randomUUID().toString().replace("-", "") + extension;
        String dosyaYolu = personFolder.getAbsolutePath() + File.separator + dosyaAdi;

        try (InputStream in = file.getInputStream(); FileOutputStream out = new FileOutputStream(dosyaYolu)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            out.flush();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Dosya diske yazılamadı: " + ex.getMessage());
        }

        OrtTalepBelge belge = OrtTalepBelge.builder()
                .ortTalep(talep)
                .belgeTuru(belgeTuru)
                .dosyaAdi(file.getFileName())
                .dosyaYolu(dosyaYolu)
                .mimeType(file.getContentType())
                .dosyaBoyutu((long) file.getContent().length)
                .yuklenmeTarihi(LocalDateTime.now())
                .aciklama(aciklama)
                .build();
        create(belge);
        flush();
        return belge;
    }

    /**
     * Belge kaydını pasife alır ve fiziksel dosyayı siler.
     */
    public void deleteDocument(OrtTalepBelge belge) {
        if (belge == null || belge.getId() == null) {
            return;
        }
        String yol = belge.getDosyaYolu();
        belge.setAktif(false);
        edit(belge);
        flush();
        if (yol != null && !yol.isBlank()) {
            File dosya = new File(yol);
            if (dosya.exists() && !dosya.delete()) {
                // Dosya silinemese de kayıt pasife alındı; kullanıcıya bilgi verilmez.
            }
        }
    }
}