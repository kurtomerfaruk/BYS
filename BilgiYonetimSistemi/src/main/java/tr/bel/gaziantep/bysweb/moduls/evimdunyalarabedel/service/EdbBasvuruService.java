package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.service;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbBasvuru;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbTalep;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.webservice.api.dto.PageResponse;
import tr.bel.gaziantep.bysweb.webservice.api.dto.evimdunyalarabedel.EdbBasvuruDTO;
import tr.bel.gaziantep.bysweb.webservice.api.dto.evimdunyalarabedel.EdbTalepDTO;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:54
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EdbBasvuruService extends AbstractService<EdbBasvuru> {

    @Serial
    private static final long serialVersionUID = 6778280155570015784L;

    public EdbBasvuruService() {
        super(EdbBasvuru.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }



    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void merge(EdbBasvuru edbBasvuru) {
        GnlKisi gnlKisi = getEntityManager().merge(edbBasvuru.getGnlKisi());
        EyKisi eyKisi = null;
        if(edbBasvuru.getEyKisi() != null) {
            EyKisi oldEyKisi = edbBasvuru.getEyKisi();
            oldEyKisi.setGnlKisi(gnlKisi);
            eyKisi = getEntityManager().merge(edbBasvuru.getEyKisi());
        }

        edbBasvuru.setGnlKisi(gnlKisi);
        edbBasvuru.setEyKisi(eyKisi);

        getEntityManager().merge(edbBasvuru);
    }

    public List<EdbBasvuru> findByGnlKisi(GnlKisi kisi) {
        return getEntityManager().createNamedQuery("EdbBasvuru.findByGnlKisi").setParameter("gnlKisi", kisi).getResultList();
    }

    public PageResponse<EdbBasvuruDTO> findAll(int page, int size) {
        TypedQuery<EdbBasvuru> query = em.createQuery(
                "SELECT p FROM EdbBasvuru p " +
                        "WHERE p.aktif=true " +
                        "AND p.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG " +
                        "AND p.gnlKisi.aktif=true " +
                        "ORDER BY p.id",
                EdbBasvuru.class);

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        List<EdbBasvuru> edbBasvuruList = query.getResultList();

        List<EdbBasvuruDTO> content = new ArrayList<>();
        for (EdbBasvuru edbBasvuru : edbBasvuruList) {
            EdbBasvuruDTO dto = new EdbBasvuruDTO();
            GnlKisi gnlKisi = edbBasvuru.getGnlKisi();
            dto.setTcKimlikNo(gnlKisi.getTcKimlikNo());
            dto.setDogumTarihi(gnlKisi.getDogumTarihi());
            dto.setAd(gnlKisi.getAd());
            dto.setSoyad(gnlKisi.getSoyad());
            dto.setDogumYeri(gnlKisi.getDogumYeri());
            dto.setIlce(gnlKisi.getGnlIlce() == null ? "-" : gnlKisi.getGnlIlce().getTanim());
            dto.setMahalle(gnlKisi.getGnlMahalle() == null ? "-" : gnlKisi.getGnlMahalle().getTanim());
            dto.setAdres(gnlKisi.getAdres());
            dto.setCinsiyet(gnlKisi.getCinsiyet().getDisplayValue());
            dto.setMedeniDurum(gnlKisi.getMedeniDurum().getDisplayValue());
            dto.setTelefon(gnlKisi.getTelefon());
            dto.setTelefon2(gnlKisi.getTelefon2());
            dto.setKoordinat(gnlKisi.getLatLng());
            dto.setEklemeTarihi(edbBasvuru.getEklemeTarihi());
            dto.setGuncellemeTarihi(edbBasvuru.getGuncellemeTarihi());
            dto.setBasvuruTarihi(edbBasvuru.getBasvuruTarihi());
            dto.setKisiTuru(edbBasvuru.getKisiTuru().getDisplayValue());
            dto.setHizmetDurumu(edbBasvuru.getHizmetDurumu().getDisplayValue());
            dto.setBasvuruDurumu(edbBasvuru.getBasvuruDurumu().getDisplayValue());

            List<EdbTalepDTO> talepList = new ArrayList<>();
            for (EdbTalep edbTalep : edbBasvuru.getEdbTalepList()) {
                if(!edbTalep.isAktif()) continue;
                EdbTalepDTO eyTalepDto = EdbTalepDTO.builder()
                        .tarih(edbTalep.getTarih())
                        .talepTuru(edbTalep.getTalepTuru().getDisplayValue())
                        .talepTipi(edbTalep.getTalepTipi().getDisplayValue())
                        .durum(edbTalep.getTalepDurumu().getDisplayValue())
                        .durumAciklama(edbTalep.getDurumAciklama())
                        .aciklama(edbTalep.getAciklama())
                        .talepEdilenHizmetler(edbTalep.getTalepEdilenHizmetler())
                        .build();
                talepList.add(eyTalepDto);
            }
            dto.setTalep(talepList);

            content.add(dto);
        }

        Long total = em.createQuery(
                "SELECT COUNT(p) FROM EdbBasvuru p " +
                        "WHERE p.aktif=true " +
                        "AND p.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG " +
                        "AND p.gnlKisi.aktif=true ",
                Long.class).getSingleResult();

        int totalPages = (int) Math.ceil((double) total / size);

        return new PageResponse<>(
                content,
                total,
                totalPages,
                page,
                size
        );
    }
}
