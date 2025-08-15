package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyKullandigiCihaz;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyMaddeKullanimi;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlFaydalandigiHak;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGelirKaynagi;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYardimAlinanYerler;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYardimTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.*;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.*;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:35
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EyKisiService extends AbstractService<EyKisi> {

    @Serial
    private static final long serialVersionUID = -18582594192132522L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EyKisiService() {
        super(EyKisi.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<String> findByTcKimlikNoList(List<String> tcList) {
        return getEntityManager().createNamedQuery("EyKisi.findByKisiTcKimlikNoList").setParameter("tcList", tcList).getResultList();
    }

    public List<EyKisi> findByAddress(int recordCount) {
        Random random = new Random();
        int first = random.nextInt(recordCount);
        return getEntityManager().createNamedQuery("EyKisi.findByAdress").setFirstResult(first).setMaxResults(recordCount).getResultList();
    }

    public List<GnlKisi> getTcList(int recordCount) {
        return getEntityManager().createNamedQuery("EyKisi.getTcList").setMaxResults(recordCount).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(EyKisi eyKisi,
                       List<EyEngelGrubu> engelGrubus,
                       List<EnumGnlFaydalandigiHak> faydalandigiHakList,
                       List<EnumEyMaddeKullanimi> maddeKullanimis,
                       List<EnumGnlGelirKaynagi> aileninGelirKaynagis,
                       List<EnumGnlYardimAlinanYerler> yardimAlinanYerlers,
                       List<EnumGnlYardimTuru> yardimTurus,
                       List<EnumEyKullandigiCihaz> kullandigiCihazs) {

        eyKisi = processAllUpdates(eyKisi, engelGrubus, faydalandigiHakList,maddeKullanimis,aileninGelirKaynagis,yardimAlinanYerlers,yardimTurus,kullandigiCihazs);
        edit(eyKisi);
    }

    public EyKisi processAllUpdates(EyKisi eyKisi,
                                     List<EyEngelGrubu> engelGrubus,
                                     List<EnumGnlFaydalandigiHak> faydalandigiHakList,
                                     List<EnumEyMaddeKullanimi> maddeKullanimis,
                                     List<EnumGnlGelirKaynagi> aileninGelirKaynagis,
                                     List<EnumGnlYardimAlinanYerler> yardimAlinanYerlers,
                                     List<EnumGnlYardimTuru> yardimTurus,
                                     List<EnumEyKullandigiCihaz> kullandigiCihazs) {
        eyKisi = checkKisiEngelGrubu(eyKisi, engelGrubus);
        eyKisi = checkGnlKisiFaydalandigiHak(eyKisi, faydalandigiHakList);
        eyKisi = checkEyKisiMaddeKullanimi(eyKisi, maddeKullanimis);
        eyKisi = checkGnlKisiGelirKaynagi(eyKisi, aileninGelirKaynagis);
        eyKisi = checkGnlKisiYardimAlinanYerler(eyKisi, yardimAlinanYerlers);
        eyKisi = checkGnlKisiYardimTuru(eyKisi, yardimTurus);
        eyKisi = checkEyKisiKullandigiCihaz(eyKisi, kullandigiCihazs);
        return eyKisi;
    }

    private EyKisi checkGnlKisiFaydalandigiHak(EyKisi eyKisi, List<EnumGnlFaydalandigiHak> faydalandigiHakList) {
        if (faydalandigiHakList == null || faydalandigiHakList.isEmpty()) {
            return eyKisi;
        }

        GnlKisi gnlKisi = eyKisi.getGnlKisi();
        List<GnlKisiFaydalandigiHaklar> existingHaklar = gnlKisi.getGnlKisiFaydalandigiHaklarList();

        Set<EnumGnlFaydalandigiHak> newHakSet = new HashSet<>(faydalandigiHakList);

        for (GnlKisiFaydalandigiHaklar hak : existingHaklar) {
            hak.setSecili(newHakSet.contains(hak.getTanim()));
        }

        Set<EnumGnlFaydalandigiHak> existingTanimSet = existingHaklar.stream()
                .map(GnlKisiFaydalandigiHaklar::getTanim)
                .collect(Collectors.toSet());

        for (EnumGnlFaydalandigiHak newHak : faydalandigiHakList) {
            if (!existingTanimSet.contains(newHak)) {
                GnlKisiFaydalandigiHaklar newEntity = new GnlKisiFaydalandigiHaklar();
                newEntity.setGnlKisi(gnlKisi);
                newEntity.setTanim(newHak);
                newEntity.setSecili(true);
                existingHaklar.add(newEntity);
            }
        }

        return eyKisi;
    }

    private EyKisi checkEyKisiMaddeKullanimi(EyKisi eyKisi, List<EnumEyMaddeKullanimi> maddeKullanimis) {
        if (maddeKullanimis == null || maddeKullanimis.isEmpty()) {
            return eyKisi;
        }

        List<EyKisiMaddeKullanimi> existingMaddeKullanimi =eyKisi.getEyKisiMaddeKullanimiList();

        Set<EnumEyMaddeKullanimi> newMaddeKullanimiSet = new HashSet<>(maddeKullanimis);

        for (EyKisiMaddeKullanimi hak : existingMaddeKullanimi) {
            hak.setSecili(newMaddeKullanimiSet.contains(hak.getTanim()));
        }

        Set<EnumEyMaddeKullanimi> existingTanimSet = existingMaddeKullanimi.stream()
                .map(EyKisiMaddeKullanimi::getTanim)
                .collect(Collectors.toSet());

        for (EnumEyMaddeKullanimi newHak : maddeKullanimis) {
            if (!existingTanimSet.contains(newHak)) {
                EyKisiMaddeKullanimi newEntity = new EyKisiMaddeKullanimi();
                newEntity.setEyKisi(eyKisi);
                newEntity.setTanim(newHak);
                newEntity.setSecili(true);
                existingMaddeKullanimi.add(newEntity);
            }
        }

        return eyKisi;
    }

    private EyKisi checkGnlKisiGelirKaynagi(EyKisi eyKisi, List<EnumGnlGelirKaynagi> aileninGelirKaynagis) {
        if (aileninGelirKaynagis == null || aileninGelirKaynagis.isEmpty()) {
            return eyKisi;
        }

        GnlKisi gnlKisi = eyKisi.getGnlKisi();
        List<GnlKisiGelirKaynagi> existingGelirKaynagi = gnlKisi.getGnlKisiGelirKaynagiList();

        Set<EnumGnlGelirKaynagi> newHakSet = new HashSet<>(aileninGelirKaynagis);

        for (GnlKisiGelirKaynagi hak : existingGelirKaynagi) {
            hak.setSecili(newHakSet.contains(hak.getTanim()));
        }

        Set<EnumGnlGelirKaynagi> existingTanimSet = existingGelirKaynagi.stream()
                .map(GnlKisiGelirKaynagi::getTanim)
                .collect(Collectors.toSet());

        for (EnumGnlGelirKaynagi newHak : aileninGelirKaynagis) {
            if (!existingTanimSet.contains(newHak)) {
                GnlKisiGelirKaynagi newEntity = new GnlKisiGelirKaynagi();
                newEntity.setGnlKisi(gnlKisi);
                newEntity.setTanim(newHak);
                newEntity.setSecili(true);
                existingGelirKaynagi.add(newEntity);
            }
        }

        return eyKisi;
    }

    private EyKisi checkGnlKisiYardimAlinanYerler(EyKisi eyKisi, List<EnumGnlYardimAlinanYerler> yardimAlinanYerlers) {
        if (yardimAlinanYerlers == null || yardimAlinanYerlers.isEmpty()) {
            return eyKisi;
        }

        GnlKisi gnlKisi = eyKisi.getGnlKisi();
        List<GnlKisiYardimAlinanYerler> existingGelirKaynagi = gnlKisi.getGnlKisiYardimAlinanYerlerList();

        Set<EnumGnlYardimAlinanYerler> newHakSet = new HashSet<>(yardimAlinanYerlers);

        for (GnlKisiYardimAlinanYerler hak : existingGelirKaynagi) {
            hak.setSecili(newHakSet.contains(hak.getTanim()));
        }

        Set<EnumGnlYardimAlinanYerler> existingTanimSet = existingGelirKaynagi.stream()
                .map(GnlKisiYardimAlinanYerler::getTanim)
                .collect(Collectors.toSet());

        for (EnumGnlYardimAlinanYerler newHak : yardimAlinanYerlers) {
            if (!existingTanimSet.contains(newHak)) {
                GnlKisiYardimAlinanYerler newEntity = new GnlKisiYardimAlinanYerler();
                newEntity.setGnlKisi(gnlKisi);
                newEntity.setTanim(newHak);
                newEntity.setSecili(true);
                existingGelirKaynagi.add(newEntity);
            }
        }

        return eyKisi;
    }

    private EyKisi checkGnlKisiYardimTuru(EyKisi eyKisi,List<EnumGnlYardimTuru> yardimTurus) {
        if (yardimTurus == null || yardimTurus.isEmpty()) {
            return eyKisi;
        }

        GnlKisi gnlKisi = eyKisi.getGnlKisi();
        List<GnlKisiAldigiYardimlar> existingGelirKaynagi = gnlKisi.getGnlKisiAldigiYardimlarList();

        Set<EnumGnlYardimTuru> newHakSet = new HashSet<>(yardimTurus);

        for (GnlKisiAldigiYardimlar hak : existingGelirKaynagi) {
            hak.setSecili(newHakSet.contains(hak.getTanim()));
        }

        Set<EnumGnlYardimTuru> existingTanimSet = existingGelirKaynagi.stream()
                .map(GnlKisiAldigiYardimlar::getTanim)
                .collect(Collectors.toSet());

        for (EnumGnlYardimTuru newHak : yardimTurus) {
            if (!existingTanimSet.contains(newHak)) {
                GnlKisiAldigiYardimlar newEntity = new GnlKisiAldigiYardimlar();
                newEntity.setGnlKisi(gnlKisi);
                newEntity.setTanim(newHak);
                newEntity.setSecili(true);
                existingGelirKaynagi.add(newEntity);
            }
        }

        return eyKisi;
    }

    private EyKisi checkEyKisiKullandigiCihaz(EyKisi eyKisi,List<EnumEyKullandigiCihaz> kullandigiCihazs) {
        if (kullandigiCihazs == null || kullandigiCihazs.isEmpty()) {
            return eyKisi;
        }

        List<EyKisiKullandigiCihaz> existingMaddeKullanimi =eyKisi.getEyKisiKullandigiCihazList();

        Set<EnumEyKullandigiCihaz> newMaddeKullanimiSet = new HashSet<>(kullandigiCihazs);

        for (EyKisiKullandigiCihaz hak : existingMaddeKullanimi) {
            hak.setSecili(newMaddeKullanimiSet.contains(hak.getTanim()));
        }

        Set<EnumEyKullandigiCihaz> existingTanimSet = existingMaddeKullanimi.stream()
                .map(EyKisiKullandigiCihaz::getTanim)
                .collect(Collectors.toSet());

        for (EnumEyKullandigiCihaz newHak : kullandigiCihazs) {
            if (!existingTanimSet.contains(newHak)) {
                EyKisiKullandigiCihaz newEntity = new EyKisiKullandigiCihaz();
                newEntity.setEyKisi(eyKisi);
                newEntity.setTanim(newHak);
                newEntity.setSecili(true);
                existingMaddeKullanimi.add(newEntity);
            }
        }

        return eyKisi;
    }

    public EyKisi checkKisiEngelGrubu(EyKisi eyKisi, List<EyEngelGrubu> engelGrubus) {
        if (engelGrubus == null || engelGrubus.isEmpty()) {
            return eyKisi;
        }

        List<EyKisiEngelGrubu> kisiEngelGrubuList = eyKisi.getEyKisiEngelGrubuList();
        Set<Integer> newGrubuIds = engelGrubus.stream()
                .map(EyEngelGrubu::getId)
                .collect(Collectors.toSet());

        Map<Integer, EyKisiEngelGrubu> existingMap = new HashMap<>();
        for (EyKisiEngelGrubu keg : kisiEngelGrubuList) {
            existingMap.put(keg.getEyEngelGrubu().getId(), keg);
            keg.setSecili(newGrubuIds.contains(keg.getEyEngelGrubu().getId()));
        }

        for (EyEngelGrubu newGrubu : engelGrubus) {
            if (!existingMap.containsKey(newGrubu.getId())) {
                EyKisiEngelGrubu newKeg = new EyKisiEngelGrubu();
                newKeg.setEyKisi(eyKisi);
                newKeg.setEyEngelGrubu(newGrubu);
                newKeg.setSecili(true);
                kisiEngelGrubuList.add(newKeg);
            }
        }

        return eyKisi;
    }

    public EyKisi findByTcKimlikNo(String tcKimlikNo) {
        return (EyKisi) getEntityManager()
                .createNamedQuery("EyKisi.findByKisiTcKimlikNo")
                .setParameter("tcKimlikNo", tcKimlikNo)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public EyKisi findByKisi(GnlKisi gnlKisi) {
        return (EyKisi) getEntityManager()
                .createNamedQuery("EyKisi.findByKisi")
                .setParameter("gnlKisi", gnlKisi)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public List<EyKisi> findByKisiList(GnlKisi kisi) {
        return getEntityManager()
                .createNamedQuery("EyKisi.findByKisi")
                .setParameter("gnlKisi", kisi)
                .getResultList();
    }

//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public void update(EyKisi eyKisi,
//                       List<EyEngelGrubu> engelGrubus,
//                       List<EnumGnlFaydalandigiHak> faydalandigiHakList,
//                       List<EnumEyMaddeKullanimi> maddeKullanimis,
//                       List<EnumGnlGelirKaynagi> aileninGelirKaynagis,
//                       List<EnumGnlYardimAlinanYerler> yardimAlinanYerlers,
//                       List<EnumGnlYardimTuru> yardimTurus,
//                       List<EnumEyKullandigiCihaz> kullandigiCihazs) {
////       eyKisi =  checkKisiEngelGrubu(eyKisi,engelGrubus);
////       eyKisi = checkGnlKisiFaydalandigiHak(eyKisi,faydalandigiHakList);
//        updateKisiEngelGrubu(eyKisi, engelGrubus);
//        updateFaydalandigiHaklar(eyKisi, faydalandigiHakList);
//        edit(eyKisi);
//    }
//
//    private void updateKisiEngelGrubu(EyKisi eyKisi, List<EyEngelGrubu> engelGrubus) {
//        List<EyKisiEngelGrubu> eyKisiEngelgrubus = eyKisi.getEyKisiEngelGrubuList();
//        Map<Integer, EyKisiEngelGrubu> engelGrubuMap = eyKisiEngelgrubus.stream()
//                .collect(Collectors.toMap(
//                        x -> x.getEyEngelGrubu().getId(),
//                        x -> x
//                ));
//
//        engelGrubuMap.values().forEach(keg -> keg.setSecili(false));
//
//        engelGrubus.forEach(grubus -> {
//            EyKisiEngelGrubu keg = engelGrubuMap.computeIfAbsent(grubus.getId(), id -> {
//                EyKisiEngelGrubu newKeg = new EyKisiEngelGrubu();
//                newKeg.setEyKisi(eyKisi);
//                newKeg.setEyEngelGrubu(grubus);
//                eyKisiEngelgrubus.add(newKeg);
//                return newKeg;
//            });
//            keg.setSecili(true);
//        });
//
//        eyKisi.setEyKisiEngelGrubuList(eyKisiEngelgrubus);
//    }
//
//    private void updateFaydalandigiHaklar(EyKisi eyKisi, List<EnumGnlFaydalandigiHak> faydalandigiHakList) {
//        GnlKisi gnlKisi = eyKisi.getGnlKisi();
//        List<GnlKisiFaydalandigiHaklar> gnlKisiFaydalandigiHaklarList = gnlKisi.getGnlKisiFaydalandigiHaklarList();
//
//        Map<EnumGnlFaydalandigiHak, GnlKisiFaydalandigiHaklar> faydalandigiHaklarMap = gnlKisiFaydalandigiHaklarList.stream()
//                .collect(Collectors.toMap(
//                        GnlKisiFaydalandigiHaklar::getTanim,
//                        x -> x
//                ));
//
//        faydalandigiHaklarMap.values().forEach(keg -> keg.setSecili(false));
//
//        faydalandigiHakList.forEach(enumGnlFaydalandigiHak -> {
//            GnlKisiFaydalandigiHaklar haklar = faydalandigiHaklarMap.computeIfAbsent(enumGnlFaydalandigiHak, tanim -> {
//                GnlKisiFaydalandigiHaklar newHak = new GnlKisiFaydalandigiHaklar();
//                newHak.setGnlKisi(gnlKisi);
//                newHak.setTanim(tanim);
//                gnlKisiFaydalandigiHaklarList.add(newHak);
//                return newHak;
//            });
//            haklar.setSecili(true);
//        });
//
//        gnlKisi.setGnlKisiFaydalandigiHaklarList(gnlKisiFaydalandigiHaklarList);
//        eyKisi.setGnlKisi(gnlKisi);
//    }

//    private EyKisi checkGnlKisiFaydalandigiHak(EyKisi eyKisi, List<EnumGnlFaydalandigiHak> faydalandigiHakList) {
//        GnlKisi gnlKisi = eyKisi.getGnlKisi();
//        List<GnlKisiFaydalandigiHaklar> gnlKisiFaydalandigiHaklarList = gnlKisi.getGnlKisiFaydalandigiHaklarList();
//        Map<EnumGnlFaydalandigiHak, GnlKisiFaydalandigiHaklar> faydalandigiHaklarMap = gnlKisiFaydalandigiHaklarList.stream()
//                .collect(Collectors.toMap(
//                        GnlKisiFaydalandigiHaklar::getTanim,
//                        x -> x
//                ));
//
//        faydalandigiHaklarMap.values().forEach(keg -> keg.setSecili(false));
//
//        for (EnumGnlFaydalandigiHak enumGnlFaydalandigiHak : faydalandigiHakList) {
//            GnlKisiFaydalandigiHaklar gnlKisiFaydalandigiHaklar = faydalandigiHaklarMap.get(enumGnlFaydalandigiHak);
//
//            if (gnlKisiFaydalandigiHaklar == null) {
//                gnlKisiFaydalandigiHaklar = new GnlKisiFaydalandigiHaklar();
//                gnlKisiFaydalandigiHaklar.setGnlKisi(gnlKisi);
//                gnlKisiFaydalandigiHaklar.setTanim(enumGnlFaydalandigiHak);
//                faydalandigiHaklarMap.put(enumGnlFaydalandigiHak, gnlKisiFaydalandigiHaklar);
//                gnlKisiFaydalandigiHaklarList.add(gnlKisiFaydalandigiHaklar);
//            }
//            gnlKisiFaydalandigiHaklar.setSecili(true);
//        }
//
//        gnlKisi.setGnlKisiFaydalandigiHaklarList(gnlKisiFaydalandigiHaklarList);
//        eyKisi.setGnlKisi(gnlKisi);
//        return eyKisi;
//    }
//
//    public EyKisi checkKisiEngelGrubu(EyKisi eyKisi,List<EyEngelGrubu> engelGrubus) {
//        List<EyKisiEngelGrubu> eyKisiEngelgrubus = eyKisi.getEyKisiEngelGrubuList();
//        Map<Integer, EyKisiEngelGrubu> engelGrubuMap = eyKisiEngelgrubus.stream()
//                .collect(Collectors.toMap(
//                        x -> x.getEyEngelGrubu().getId(),
//                        x -> x
//                ));
//
//        engelGrubuMap.values().forEach(keg -> keg.setSecili(false));
//
//        for (EyEngelGrubu grubus : engelGrubus) {
//            EyKisiEngelGrubu keg = engelGrubuMap.get(grubus.getId());
//
//            if (keg == null) {
//                keg = new EyKisiEngelGrubu();
//                keg.setEyKisi(eyKisi);
//                keg.setEyEngelGrubu(grubus);
//                engelGrubuMap.put(grubus.getId(), keg);
//                eyKisiEngelgrubus.add(keg);
//            }
//            keg.setSecili(true);
//        }
//
//        eyKisi.setEyKisiEngelGrubuList(eyKisiEngelgrubus);
//        return eyKisi;
//    }
}
