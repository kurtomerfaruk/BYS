package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlAnketSoruTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlAnket;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlAnketCevap;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlAnketCevapDetay;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlAnketSoru;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.09.2025 09:46
 */
@Stateless
public class GnlAnketCevapService extends AbstractService<GnlAnketCevap> {

    @Serial
    private static final long serialVersionUID = -5368596187639327752L;

    public GnlAnketCevapService() {
        super(GnlAnketCevap.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public boolean existsBySurveyIdAndUserId(Integer gnlAnketId, Integer syKullaniciId) {
        List<GnlAnketCevap> result = getEntityManager()
                .createNamedQuery("GnlAnketCevap.findByGnlAnketIdBySyKullaniciId")
                .setParameter("gnlAnketId", gnlAnketId)
                .setParameter("syKullaniciId", syKullaniciId)
                .getResultList();
        return !result.isEmpty();
    }

    public boolean existsBySurveyIdAndTcNo(Integer gnlAnketId, String tcKimlikNo) {
        List<GnlAnketCevap> result = getEntityManager()
                .createNamedQuery("GnlAnketCevap.findByGnlAnketIdByTcKimlikNo")
                .setParameter("gnlAnketId", gnlAnketId)
                .setParameter("tcKimlikNo", tcKimlikNo)
                .getResultList();
        return !result.isEmpty();
    }

    public boolean existsBySurveyIdAndAnonToken(Integer gnlAnketId, String token) {
        List<GnlAnketCevap> result = getEntityManager()
                .createNamedQuery("GnlAnketCevap.findByGnlAnketIdByToken")
                .setParameter("gnlAnketId", gnlAnketId)
                .setParameter("token", token)
                .getResultList();
        return !result.isEmpty();
    }

    public void save(GnlAnket gnlAnket, Integer syKullaniciId, String tcKimlikNo, LocalDate dogumTarihi, String anonToken, Map<Integer, Object> answers, Map<Integer, Object> otherAnswers) {
        GnlAnketCevap cevap = new GnlAnketCevap();
        cevap.setGnlAnket(gnlAnket);
        if (syKullaniciId != null) {
            cevap.setSyKullanici(new SyKullanici(syKullaniciId));
        }
        cevap.setTcKimlikNo(tcKimlikNo);
        cevap.setDogumTarihi(dogumTarihi);
        cevap.setToken(anonToken);
        cevap.setTarih(LocalDateTime.now());

        List<GnlAnketCevapDetay> detays = new ArrayList<>();
        for (GnlAnketSoru gnlAnketSoru : gnlAnket.getGnlAnketSoruList()) {
            Object val = answers.get(gnlAnketSoru.getId());
            if (val == null) continue;

            GnlAnketCevapDetay detay = new GnlAnketCevapDetay();
            detay.setGnlAnketCevap(cevap);
            detay.setGnlAnketSoru(gnlAnketSoru);

            if (gnlAnketSoru.getSoruTuru() == EnumGnlAnketSoruTuru.SELECT_MANY && val instanceof Collection<?>) {
                String joined = ((Collection<?>) val)
                        .stream().map(Object::toString)
                        .collect(Collectors.joining(","));
                detay.setCevap(joined);
            } else if (val instanceof String[]) {
                String joined = String.join(",", (String[]) val);
                detay.setCevap(joined);
            } else {
                detay.setCevap(val.toString());
            }

            String otherText = (String) otherAnswers.get(gnlAnketSoru.getId());
            if (gnlAnketSoru.isDiger() && otherText != null && !otherText.isEmpty()) {
                detay = new GnlAnketCevapDetay();
                detay.setGnlAnketCevap(cevap);
                detay.setGnlAnketSoru(gnlAnketSoru);
                detay.setCevap(otherText);
                detays.add(detay);
            }
            detays.add(detay);

        }

        cevap.setGnlAnketCevapDetayList(detays);
        edit(cevap);
    }
}
