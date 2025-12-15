package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.*;
import tr.bel.gaziantep.bysweb.moduls.genel.report.GnlRaporIstek;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.12.2025 14:06
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GnlRaporKullaniciRaporSablonService extends AbstractService<GnlRaporKullaniciRaporSablon> {

    @Serial
    private static final long serialVersionUID = 6506207347793096623L;

    public GnlRaporKullaniciRaporSablonService() {
        super(GnlRaporKullaniciRaporSablon.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public GnlRaporIstek sablonuRaporIstegineCevir(GnlRaporIstek raporIstek) {
        try {
            Integer sablonId = raporIstek.getSablonId();
            GnlRaporKullaniciRaporSablon sablon = em.find(GnlRaporKullaniciRaporSablon.class, sablonId);

            if (sablon == null) {
                return raporIstek;
            }

            // Kolonları yükle
            TypedQuery<GnlRaporSablonKolon> kolonQuery = em.createQuery(
                    "SELECT sk FROM GnlRaporSablonKolon sk " +
                            "JOIN FETCH sk.gnlRaporKolon rk " +
                            "WHERE sk.gnlRaporKullaniciRaporSablon.id = :sablonId " +
                            "ORDER BY sk.siralama",
                    GnlRaporSablonKolon.class
            );
            kolonQuery.setParameter("sablonId", sablonId);
            List<GnlRaporSablonKolon> sablonKolonlari = kolonQuery.getResultList();

            List<GnlRaporIstek.RaporKolonDto> kolonDtoList = new ArrayList<>();
            for (GnlRaporSablonKolon sablonKolon : sablonKolonlari) {
                GnlRaporKolon kolon = sablonKolon.getGnlRaporKolon();
                GnlRaporIstek.RaporKolonDto dto = new GnlRaporIstek.RaporKolonDto();
                dto.setId(kolon.getId());
                dto.setAlanAdi(kolon.getAlanAdi());
                dto.setGorunurAdi(kolon.getGorunurAdi());
                dto.setVeriTipi(kolon.getVeriTipi());
                dto.setGenislik(sablonKolon.getGenislik());
                kolonDtoList.add(dto);
            }
            raporIstek.setKolonlar(kolonDtoList);

            // Parametreleri yükle
            TypedQuery<GnlRaporSablonParametre> paramQuery = em.createQuery(
                    "SELECT sp FROM GnlRaporSablonParametre sp " +
                            "JOIN FETCH sp.gnlRaporParametre rp " +
                            "WHERE sp.gnlRaporKullaniciRaporSablon.id = :sablonId",
                    GnlRaporSablonParametre.class
            );
            paramQuery.setParameter("sablonId", sablonId);
            List<GnlRaporSablonParametre> sablonParametreleri = paramQuery.getResultList();

            List<GnlRaporIstek.ParametreDegeriDto> paramDtoList = new ArrayList<>();
            for (GnlRaporSablonParametre sablonParam : sablonParametreleri) {
                GnlRaporParametre parametre = sablonParam.getGnlRaporParametre();
                GnlRaporIstek.ParametreDegeriDto dto = new GnlRaporIstek.ParametreDegeriDto();
                dto.setParametreId(parametre.getId());
                dto.setParametreAdi(parametre.getParametreAdi());
                dto.setGorunurAdi(parametre.getGorunurAdi());
                dto.setDeger(sablonParam.getDeger());
                dto.setIkinciDeger(sablonParam.getIkinciDeger());
                dto.setOperator(sablonParam.getOperator());
                dto.setVeriTipi(parametre.getVeriTipi());
                paramDtoList.add(dto);
            }
            raporIstek.setParametreler(paramDtoList);

            return raporIstek;

        } catch (Exception e) {
            throw new RuntimeException("Şablon yüklenirken hata: " + e.getMessage(), e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public GnlRaporKullaniciRaporSablon saveSablon(GnlRaporKullaniciRaporSablon sablon,
                                           List<GnlRaporIstek.RaporKolonDto> kolonlar,
                                           List<GnlRaporIstek.ParametreDegeriDto> parametreler) {
        try {
            // Ana şablonu kaydet
            em.persist(sablon);

            // Kolonları kaydet
            for (int i = 0; i < kolonlar.size(); i++) {
                GnlRaporIstek.RaporKolonDto kolonDto = kolonlar.get(i);
                GnlRaporKolon kolon = em.find(GnlRaporKolon.class, kolonDto.getId());

                if (kolon != null) {
                    GnlRaporSablonKolon sablonKolon = new GnlRaporSablonKolon();
                    sablonKolon.setGnlRaporKullaniciRaporSablon(sablon);
                    sablonKolon.setGnlRaporKolon(kolon);
                    sablonKolon.setSiralama(i);
                    sablonKolon.setGenislik(kolonDto.getGenislik());
                    em.persist(sablonKolon);
                }
            }

            // Parametreleri kaydet
            for (GnlRaporIstek.ParametreDegeriDto paramDto : parametreler) {
                if (paramDto.getDeger() != null && !paramDto.getDeger().isEmpty()) {
                    GnlRaporParametre parametre = em.find(GnlRaporParametre.class, paramDto.getParametreId());

                    if (parametre != null) {
                        GnlRaporSablonParametre sablonParam = new GnlRaporSablonParametre();
                        sablonParam.setGnlRaporKullaniciRaporSablon(sablon);
                        sablonParam.setGnlRaporParametre(parametre);
                        sablonParam.setDeger(paramDto.getDeger());
                        sablonParam.setIkinciDeger(paramDto.getIkinciDeger());
                        sablonParam.setOperator(paramDto.getOperator());
                        em.persist(sablonParam);
                    }
                }
            }

            em.flush();
            return sablon;

        } catch (Exception e) {
            throw new RuntimeException("Şablon kaydedilirken hata: " + e.getMessage(), e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<GnlRaporKullaniciRaporSablon> getKullaniciSablonlari(Integer kullaniciId) {
        try {
            TypedQuery<GnlRaporKullaniciRaporSablon> query = em.createQuery(
                    "SELECT s FROM GnlRaporKullaniciRaporSablon s " +
                            "WHERE s.syKullanici.id = :kullaniciId OR s.genel = true " +
                            "ORDER BY s.sablonAdi",
                    GnlRaporKullaniciRaporSablon.class
            );
            query.setParameter("kullaniciId", kullaniciId);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Şablonlar getirilirken hata: " + e.getMessage(), e);
        }
    }


}
