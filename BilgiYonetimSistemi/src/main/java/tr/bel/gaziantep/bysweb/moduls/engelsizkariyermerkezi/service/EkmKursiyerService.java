package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyKullandigiCihaz;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyMaddeKullanimi;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlFaydalandigiHak;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGelirKaynagi;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYardimAlinanYerler;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYardimTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyer;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyerKurs;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKurs;
import tr.bel.gaziantep.bysweb.webservice.api.dto.PageResponse;
import tr.bel.gaziantep.bysweb.webservice.api.dto.engelsizkariyermerkezi.EkmKursiyerDto;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 8.12.2025 08:33
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EkmKursiyerService extends AbstractService<EkmKursiyer> {

    @Serial
    private static final long serialVersionUID = -1278240052111967485L;

    @Inject
    private EyKisiService eyKisiService;

    public EkmKursiyerService() {
        super(EkmKursiyer.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(EkmKursiyer ekmKursiyer,
                       List<EyEngelGrubu> engelGrubus,
                       List<EnumGnlFaydalandigiHak> faydalandigiHakList,
                       List<EnumEyMaddeKullanimi> maddeKullanimis,
                       List<EnumGnlGelirKaynagi> aileninGelirKaynagis,
                       List<EnumGnlYardimAlinanYerler> yardimAlinanYerlers,
                       List<EnumGnlYardimTuru> yardimTurus,
                       List<EnumEyKullandigiCihaz> kullandigiCihazs,
                       List<GnlKurs> gnlKursList) {
        EyKisi eyKisi = ekmKursiyer.getEyKisi();
        eyKisi = eyKisiService.processAllUpdates(eyKisi, engelGrubus, faydalandigiHakList,maddeKullanimis,aileninGelirKaynagis,yardimAlinanYerlers,yardimTurus,
                kullandigiCihazs);
        ekmKursiyer.setEyKisi(eyKisi);

        ekmKursiyer = checkKursiyerGnlKurs(ekmKursiyer, gnlKursList);
        edit(ekmKursiyer);
    }

    private EkmKursiyer checkKursiyerGnlKurs(EkmKursiyer ekmKursiyer, List<GnlKurs> gnlKursList) {
        if (gnlKursList == null || gnlKursList.isEmpty()) {
            return ekmKursiyer;
        }

        List<EkmKursiyerKurs> kursiyerKursList = ekmKursiyer.getEkmKursiyerKursList();
        Set<Integer> newGrubuIds = gnlKursList.stream()
                .map(GnlKurs::getId)
                .collect(Collectors.toSet());

        Map<Integer, EkmKursiyerKurs> existingMap = new HashMap<>();
        for (EkmKursiyerKurs kurs : kursiyerKursList) {
            existingMap.put(kurs.getGnlKurs().getId(), kurs);
            kurs.setSecili(newGrubuIds.contains(kurs.getGnlKurs().getId()));
        }

        for (GnlKurs newGrubu : gnlKursList) {
            if (!existingMap.containsKey(newGrubu.getId())) {
                EkmKursiyerKurs newKeg = new EkmKursiyerKurs();
                newKeg.setEkmKursiyer(ekmKursiyer);
                newKeg.setGnlKurs(newGrubu);
                newKeg.setSecili(true);
                kursiyerKursList.add(newKeg);
            }
        }

        return ekmKursiyer;
    }

    public EkmKursiyer findByTcKimlikNo(String tcKimlikNo) {
        return (EkmKursiyer) getEntityManager().createNamedQuery("EkmKursiyer.findByTcKimlikNo")
                .setParameter("tcKimlikNo",tcKimlikNo)
                .getResultList()
                .stream().
                findFirst()
                .orElse(null);
    }

    public PageResponse<EkmKursiyerDto> findAll(int page, int size) {
        TypedQuery<EkmKursiyer> query = em.createQuery(
                "SELECT p FROM EkmKursiyer p " +
                        "WHERE p.aktif=true " +
                        "AND p.eyKisi.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG " +
                        "AND p.eyKisi.gnlKisi.aktif=true " +
                        "AND p.eyKisi.aktif=true " +
                        "ORDER BY p.eyKisi.gnlKisi.tcKimlikNo",
                EkmKursiyer.class);

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        List<EkmKursiyer> eyKisiList = query.getResultList();

        List<EkmKursiyerDto> content = new ArrayList<>();
        for (EkmKursiyer ekmKursiyer : eyKisiList) {
            EkmKursiyerDto dto = new EkmKursiyerDto();
            GnlKisi gnlKisi = ekmKursiyer.getEyKisi().getGnlKisi();
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
            dto.setDevamDurumu(ekmKursiyer.getDevamDurumu().getDisplayValue());
            dto.setEklemeTarihi(ekmKursiyer.getEklemeTarihi());
            dto.setGuncellemeTarihi(ekmKursiyer.getGuncellemeTarihi());

            List<String> kursList = new ArrayList<>();
            for (EkmKursiyerKurs ekmKursiyerKurs : ekmKursiyer.getEkmKursiyerKursList()) {
                if(!ekmKursiyerKurs.isAktif() || !ekmKursiyerKurs.isSecili()) continue;
                kursList.add(ekmKursiyerKurs.getGnlKurs().getTanim());
            }
            dto.setKurs(kursList);

            content.add(dto);
        }

        Long total = em.createQuery(
                "SELECT COUNT(p) FROM EkmKursiyer p " +
                        "WHERE p.aktif=true " +
                        "AND p.eyKisi.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG " +
                        "AND p.eyKisi.gnlKisi.aktif=true " +
                        "AND p.eyKisi.aktif=true ",
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
