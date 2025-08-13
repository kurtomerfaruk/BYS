package tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.evdeyasamadestek.EnumEdbDurum;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbHizmetPlanlama;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbPersonel;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbVerilecekHizmet;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbVerilecekHizmetPersonel;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.07.2025 09:06
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EdbVerilecekHizmetService extends AbstractService<EdbVerilecekHizmet> {

    @Serial
    private static final long serialVersionUID = 2834174733144262275L;

    public EdbVerilecekHizmetService() {
        super(EdbVerilecekHizmet.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    protected EntityManager getEntityManager(){
        return  em;
    }

    public EdbVerilecekHizmet findByHizmetPlanlamaId(EdbHizmetPlanlama hizmetPlanlama) {
        return (EdbVerilecekHizmet) getEntityManager()
                .createNamedQuery("EdbVerilecekHizmet.findByHizmetPlanlamaId")
                .setParameter("edbHizmetPlanlama",hizmetPlanlama)
                .getResultList()
                .stream().
                findFirst()
                .orElse(null);
    }

    public List<EdbVerilecekHizmet> findByDurumMaxDate(EnumEdbDurum durum) {
        return getEntityManager().createNamedQuery("EdbVerilecekHizmet.findByDurumMaxDate").setParameter("durum", durum).getResultList();
    }

    public List<EdbVerilecekHizmet> findByDurum(EnumEdbDurum durum) {
        return getEntityManager().createNamedQuery("EdbVerilecekHizmet.findByDurum").setParameter("durum", durum).getResultList();
    }


    public void update(EdbVerilecekHizmet hizmet, List<EdbPersonel> edbPersonelList) {
        hizmet.getEdbVerilecekHizmetPersonelList().forEach(x -> x.setSecili(false));

        for (EdbPersonel personel : edbPersonelList) {
            EdbVerilecekHizmetPersonel hizmetPersonel = hizmet.getEdbVerilecekHizmetPersonelList()
                    .stream()
                    .filter(x -> x.getEdbPersonel().getId().equals(personel.getId()))
                    .findFirst()
                    .orElse(null);
            if (hizmetPersonel == null) {
                hizmetPersonel = EdbVerilecekHizmetPersonel.builder().secili(true).edbPersonel(personel).edbVerilecekHizmet(hizmet).build();
                hizmet.getEdbVerilecekHizmetPersonelList().add(hizmetPersonel);
            } else {
                int index = hizmet.getEdbVerilecekHizmetPersonelList().indexOf(hizmetPersonel);
                hizmetPersonel.setSecili(true);
                hizmet.getEdbVerilecekHizmetPersonelList().set(index, hizmetPersonel);
            }
        }

        edit(hizmet);
    }
}
