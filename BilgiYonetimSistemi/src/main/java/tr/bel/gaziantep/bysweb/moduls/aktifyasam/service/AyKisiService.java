package tr.bel.gaziantep.bysweb.moduls.aktifyasam.service;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.aktifyasam.EnumAyGrup;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGun;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.*;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 10:55
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AyKisiService extends AbstractService<AyKisi> {

    @Serial
    private static final long serialVersionUID = 8871877278551526561L;

    public AyKisiService() {
        super(AyKisi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<AyKisi> findByLatLngIsNull(int recordCount) {
        return getEntityManager().createNamedQuery("AyKisi.findByLatLngIsNull").setMaxResults(recordCount).getResultList();
    }

//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public void persist(AyKisi ayKisi, List<AyAktivite> ayAktivites, List<AySanatsalBeceri> aySanatsalBeceris, List<AySaglikBilgi> aySaglikBilgis) {
//
//        ayAktivites.stream()
//                .map(ayAktivite -> AyKisiAktivite.builder().ayAktivite(ayAktivite).ayKisi(ayKisi).secili(true).build())
//                .forEach(ayKisiAktivite -> ayKisi.getAyKisiAktiviteList().add(ayKisiAktivite));
//        aySanatsalBeceris.stream()
//                .map(aySanatsalBeceri -> AyKisiSanatsalBeceri.builder().aySanatsalBeceri(aySanatsalBeceri).ayKisi(ayKisi).secili(true).build())
//                .forEach(aySanatsalBeceri -> ayKisi.getAyKisiSanatsalBeceriList().add(aySanatsalBeceri));
//        aySaglikBilgis.stream()
//                .map(aySaglikBilgileri -> AyKisiSaglikBilgi.builder().aySaglikBilgi(aySaglikBilgileri).ayKisi(ayKisi).secili(true).build())
//                .forEach(ayKisiSaglikBilgileri -> ayKisi.getAyKisiSaglikBilgileriList().add(ayKisiSaglikBilgileri));
//        create(ayKisi);
//    }


    public List<AyKisi> findByGun(EnumGnlGun day) {
        return Collections.emptyList();
//        return getEntityManager().createNamedQuery("AyKisi.findByGun").setParameter("gun", day).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(AyKisi ayKisi, List<AyAktivite> ayAktivites, List<AySanatsalBeceri> aySanatsalBeceris, List<AySaglikBilgi> aySaglikBilgis, List<EnumAyGrup> grups, List<EnumGnlGun> guns) {
        ayKisi = checkKisiAktivite(ayKisi, ayAktivites);
        ayKisi = checkKisiSanatsalBeceri(ayKisi, aySanatsalBeceris);
        ayKisi = checkKisiSaglikBilgi(ayKisi, aySaglikBilgis);
        ayKisi = checkKisiGrup(ayKisi, grups);
        ayKisi = checkKisiGun(ayKisi, guns);

        edit(ayKisi);
    }

    public AyKisi checkKisiAktivite(AyKisi ayKisi, List<AyAktivite> ayAktivites) {
        if (ayAktivites == null || ayAktivites.isEmpty()) {
            return ayKisi;
        }

        List<AyKisiAktivite> kisiAktiviteList = ayKisi.getAyKisiAktiviteList();
        Set<Integer> newGrubuIds = ayAktivites.stream()
                .map(AyAktivite::getId)
                .collect(Collectors.toSet());

        Map<Integer, AyKisiAktivite> existingMap = new HashMap<>();
        for (AyKisiAktivite keg : kisiAktiviteList) {
            existingMap.put(keg.getAyAktivite().getId(), keg);
            keg.setSecili(newGrubuIds.contains(keg.getAyAktivite().getId()));
        }

        for (AyAktivite newAktivite : ayAktivites) {
            if (!existingMap.containsKey(newAktivite.getId())) {
                AyKisiAktivite newKeg = new AyKisiAktivite();
                newKeg.setAyKisi(ayKisi);
                newKeg.setAyAktivite(newAktivite);
                newKeg.setSecili(true);
                kisiAktiviteList.add(newKeg);
            }
        }

        return ayKisi;
    }

    public AyKisi checkKisiSanatsalBeceri(AyKisi ayKisi, List<AySanatsalBeceri> aySanatsalBeceris) {
        if (aySanatsalBeceris == null || aySanatsalBeceris.isEmpty()) {
            return ayKisi;
        }

        List<AyKisiSanatsalBeceri> kisiSanatsalBeceriList = ayKisi.getAyKisiSanatsalBeceriList();
        Set<Integer> newGrubuIds = aySanatsalBeceris.stream()
                .map(AySanatsalBeceri::getId)
                .collect(Collectors.toSet());

        Map<Integer, AyKisiSanatsalBeceri> existingMap = new HashMap<>();
        for (AyKisiSanatsalBeceri keg : kisiSanatsalBeceriList) {
            existingMap.put(keg.getAySanatsalBeceri().getId(), keg);
            keg.setSecili(newGrubuIds.contains(keg.getAySanatsalBeceri().getId()));
        }

        for (AySanatsalBeceri newItem : aySanatsalBeceris) {
            if (!existingMap.containsKey(newItem.getId())) {
                AyKisiSanatsalBeceri newKeg = new AyKisiSanatsalBeceri();
                newKeg.setAyKisi(ayKisi);
                newKeg.setAySanatsalBeceri(newItem);
                newKeg.setSecili(true);
                kisiSanatsalBeceriList.add(newKeg);
            }
        }

        return ayKisi;
    }

    public AyKisi checkKisiSaglikBilgi(AyKisi ayKisi, List<AySaglikBilgi> aySaglikBilgis) {
        if (aySaglikBilgis == null || aySaglikBilgis.isEmpty()) {
            return ayKisi;
        }

        List<AyKisiSaglikBilgi> kisiSanatsalBeceriList = ayKisi.getAyKisiSaglikBilgileriList();
        Set<Integer> newGrubuIds = aySaglikBilgis.stream()
                .map(AySaglikBilgi::getId)
                .collect(Collectors.toSet());

        Map<Integer, AyKisiSaglikBilgi> existingMap = new HashMap<>();
        for (AyKisiSaglikBilgi keg : kisiSanatsalBeceriList) {
            existingMap.put(keg.getAySaglikBilgi().getId(), keg);
            keg.setSecili(newGrubuIds.contains(keg.getAySaglikBilgi().getId()));
        }

        for (AySaglikBilgi newItem : aySaglikBilgis) {
            if (!existingMap.containsKey(newItem.getId())) {
                AyKisiSaglikBilgi newKeg = new AyKisiSaglikBilgi();
                newKeg.setAyKisi(ayKisi);
                newKeg.setAySaglikBilgi(newItem);
                newKeg.setSecili(true);
                kisiSanatsalBeceriList.add(newKeg);
            }
        }

        return ayKisi;
    }

    private AyKisi checkKisiGrup(AyKisi ayKisi, List<EnumAyGrup> grups) {
        if (grups == null || grups.isEmpty()) {
            return ayKisi;
        }

        List<AyKisiGrup> existingGrup = ayKisi.getAyKisiGrupList();

        Set<EnumAyGrup> newHakSet = new HashSet<>(grups);

        for (AyKisiGrup hak : existingGrup) {
            hak.setSecili(newHakSet.contains(hak.getGrup()));
        }

        Set<EnumAyGrup> existingTanimSet = existingGrup.stream()
                .map(AyKisiGrup::getGrup)
                .collect(Collectors.toSet());

        for (EnumAyGrup newHak : grups) {
            if (!existingTanimSet.contains(newHak)) {
                AyKisiGrup newEntity = new AyKisiGrup();
                newEntity.setAyKisi(ayKisi);
                newEntity.setGrup(newHak);
                newEntity.setSecili(true);
                existingGrup.add(newEntity);
            }
        }

        return ayKisi;
    }

    private AyKisi checkKisiGun(AyKisi ayKisi, List<EnumGnlGun> guns) {
        if (guns == null || guns.isEmpty()) {
            return ayKisi;
        }

        List<AyKisiGun> existingGun = ayKisi.getAyKisiGunList();

        Set<EnumGnlGun> newHakSet = new HashSet<>(guns);

        for (AyKisiGun hak : existingGun) {
            hak.setSecili(newHakSet.contains(hak.getGun()));
        }

        Set<EnumGnlGun> existingTanimSet = existingGun.stream()
                .map(AyKisiGun::getGun)
                .collect(Collectors.toSet());

        for (EnumGnlGun newItem : guns) {
            if (!existingTanimSet.contains(newItem)) {
                AyKisiGun newEntity = new AyKisiGun();
                newEntity.setAyKisi(ayKisi);
                newEntity.setGun(newItem);
                newEntity.setSecili(true);
                existingGun.add(newEntity);
            }
        }

        return ayKisi;
    }

}
