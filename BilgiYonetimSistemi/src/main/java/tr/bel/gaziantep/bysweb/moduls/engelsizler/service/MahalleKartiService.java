package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.dto.MahalleKartiDTO;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;

import java.io.Serial;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.17.0
 * @since 27.07.2026 13:18
 */
@Stateless
public class MahalleKartiService implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -6190926280869990809L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public MahalleKartiDTO getMahalleKarti(GnlMahalle mahalle) {
        MahalleKartiDTO dto = new MahalleKartiDTO();

        dto.setToplamEngelli(getToplamEngelli(mahalle));
        dto.setAktifEngelli(getAktifEngelli(mahalle));
        dto.setOlenEngelli(getOlenEngelli(mahalle));
        dto.setAgirOzurluSayisi(getAgirOzurluSayisi(mahalle));
        dto.setToplamTamir(getToplamTamir(mahalle));
        dto.setKursiyerSayisi(getKursiyerSayisi(mahalle));
        dto.setErkekSayisi(getCinsiyetSayisi(mahalle, "ERKEK"));
        dto.setKadinSayisi(getCinsiyetSayisi(mahalle, "KADIN"));
        dto.setBekleyenTalep(getTalepSayisiDurumunaGore(mahalle, "BEKLIYOR"));
        dto.setTamamlananTalep(getTalepSayisiDurumunaGore(mahalle, "TAMAMLANDI"));
        dto.setTamamlanmayanTalep(getTalepSayisiDurumunaGore(mahalle, "TAMAMLANMADI"));

        dto.setCinsiyetGrubuDagilimi(getCinsiyetGrubuDagilimi(mahalle));
        dto.setYasDagilimi(getYasGrubuDagilimi(mahalle));
        dto.setEngelOraniGrubuDagilimi(getEngelOraniGrubuDagilimi(mahalle));
        dto.setEngelGrubuDagilimi(getEngelGrubuDagilimi(mahalle));
        dto.setCihazTeslimGrubuDagilimi(getCihazTeslimGrubuDagilimi(mahalle));
        dto.setEgitimGrubuDagilimi(getEgitimGrubuDagilimi(mahalle));
        dto.setMedeniGrubuDagilimi(getMedeniGrubuDagilimi(mahalle));
        dto.setTamirGrubuDagilimi(getTamirGrubuDagilimi(mahalle));
        dto.setTalepGrubuDagilimi(getTalepGrubuDagilimi(mahalle));

        return dto;
    }

    private int getCinsiyetSayisi(GnlMahalle mahalle, String cinsiyet) {
        Long count = em.createQuery(
                "SELECT COUNT(e) FROM EyKisi e WHERE e.aktif=true AND e.gnlKisi.aktif=true " +
                        "AND e.gnlKisi.gnlMahalle=:mahalle " +
                        "AND e.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG " +
                        "AND e.gnlKisi.cinsiyet=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlCinsiyet." + cinsiyet,
                Long.class).setParameter("mahalle", mahalle).getSingleResult();
        return count.intValue();
    }

    private int getTalepSayisiDurumunaGore(GnlMahalle mahalle, String durum) {
        Long count = em.createQuery(
                "SELECT COUNT(t) FROM EyTalep t WHERE t.aktif=true " +
                        "AND t.eyKisi.gnlKisi.gnlMahalle=:mahalle " +
                        "AND t.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu." + durum,
                Long.class).setParameter("mahalle", mahalle).getSingleResult();
        return count.intValue();
    }

    private int getToplamEngelli(GnlMahalle mahalle) {
        Long count = em.createQuery(
                "SELECT COUNT(e) FROM EyKisi e WHERE e.aktif=true AND e.gnlKisi.aktif=true AND e.gnlKisi.gnlMahalle=:mahalle",
                Long.class).setParameter("mahalle", mahalle).getSingleResult();
        return count.intValue();
    }

    private int getAktifEngelli(GnlMahalle mahalle) {
        Long count = em.createQuery(
                "SELECT COUNT(e) FROM EyKisi e WHERE e.aktif=true AND e.gnlKisi.aktif=true " +
                        "AND e.gnlKisi.gnlMahalle=:mahalle " +
                        "AND e.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG",
                Long.class).setParameter("mahalle", mahalle).getSingleResult();
        return count.intValue();
    }

    private int getOlenEngelli(GnlMahalle mahalle) {
        Long count = em.createQuery(
                "SELECT COUNT(e) FROM EyKisi e WHERE e.aktif=true " +
                        "AND e.gnlKisi.gnlMahalle=:mahalle " +
                        "AND e.gnlKisi.durum IN (" +
                        "tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.OLU," +
                        "tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.OLUM," +
                        "tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.OLUMUN_TESPITI)",
                Long.class).setParameter("mahalle", mahalle).getSingleResult();
        return count.intValue();
    }

    private int getAgirOzurluSayisi(GnlMahalle mahalle) {
        Long count = em.createQuery(
                "SELECT COUNT(e) FROM EyKisi e WHERE e.aktif=true AND e.gnlKisi.aktif=true " +
                        "AND e.gnlKisi.gnlMahalle=:mahalle " +
                        "AND e.agirOzurlu=true AND e.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG",
                Long.class).setParameter("mahalle", mahalle).getSingleResult();
        return count.intValue();
    }

    private int getToplamTamir(GnlMahalle mahalle) {
        Long count = em.createQuery(
                "SELECT COUNT(t) FROM EyAracTamir t WHERE t.aktif=true AND t.eyKisi.gnlKisi.gnlMahalle=:mahalle",
                Long.class).setParameter("mahalle", mahalle).getSingleResult();
        return count.intValue();
    }

    private int getKursiyerSayisi(GnlMahalle mahalle) {
        Long count = em.createQuery(
                "SELECT COUNT(DISTINCT k.eyKisi) FROM EkmKursiyer k WHERE k.aktif=true " +
                        "AND k.eyKisi.gnlKisi.gnlMahalle=:mahalle",
                Long.class).setParameter("mahalle", mahalle).getSingleResult();
        return count.intValue();
    }

    private Map<String, Integer> getCinsiyetGrubuDagilimi(GnlMahalle mahalle) {
        List<Object[]> results = em.createQuery(
                "SELECT e.gnlKisi.cinsiyet, COUNT(e) FROM EyKisi e " +
                        "WHERE e.aktif=true AND e.gnlKisi.aktif=true " +
                        "AND e.gnlKisi.gnlMahalle=:mahalle " +
                        "AND e.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG " +
                        "GROUP BY e.gnlKisi.cinsiyet",
                Object[].class).setParameter("mahalle", mahalle).getResultList();

        Map<String, Integer> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            String key = row[0] != null ? ((Enum<?>) row[0]).name() : "Tanımsız";
            map.put(key, ((Long) row[1]).intValue());
        }
        return map;
    }

    private Map<String, Integer> getYasGrubuDagilimi(GnlMahalle mahalle) {
        List<Object[]> results = em.createQuery(
                "SELECT " +
                        "CASE " +
                        "  WHEN YEAR(CURRENT_DATE) - YEAR(e.gnlKisi.dogumTarihi) < 18 THEN '0-17' " +
                        "  WHEN YEAR(CURRENT_DATE) - YEAR(e.gnlKisi.dogumTarihi) BETWEEN 18 AND 29 THEN '18-29' " +
                        "  WHEN YEAR(CURRENT_DATE) - YEAR(e.gnlKisi.dogumTarihi) BETWEEN 30 AND 49 THEN '30-49' " +
                        "  WHEN YEAR(CURRENT_DATE) - YEAR(e.gnlKisi.dogumTarihi) BETWEEN 50 AND 59 THEN '50-59' " +
                        "  ELSE '60+' " +
                        "END, COUNT(e) " +
                        "FROM EyKisi e " +
                        "WHERE e.aktif=true AND e.gnlKisi.aktif=true " +
                        "AND e.gnlKisi.gnlMahalle=:mahalle " +
                        "AND e.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG " +
                        "AND e.gnlKisi.dogumTarihi IS NOT NULL " +
                        "GROUP BY " +
                        "CASE " +
                        "  WHEN YEAR(CURRENT_DATE) - YEAR(e.gnlKisi.dogumTarihi) < 18 THEN '0-17' " +
                        "  WHEN YEAR(CURRENT_DATE) - YEAR(e.gnlKisi.dogumTarihi) BETWEEN 18 AND 29 THEN '18-29' " +
                        "  WHEN YEAR(CURRENT_DATE) - YEAR(e.gnlKisi.dogumTarihi) BETWEEN 30 AND 49 THEN '30-49' " +
                        "  WHEN YEAR(CURRENT_DATE) - YEAR(e.gnlKisi.dogumTarihi) BETWEEN 50 AND 59 THEN '50-59' " +
                        "  ELSE '60+' " +
                        "END " +
                        "ORDER BY 1",
                Object[].class).setParameter("mahalle", mahalle).getResultList();

        Map<String, Integer> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], ((Long) row[1]).intValue());
        }
        return map;
    }

    private Map<String, Integer> getEngelOraniGrubuDagilimi(GnlMahalle mahalle) {
        List<Object[]> results = em.createQuery(
                "SELECT " +
                        "CASE " +
                        "  WHEN e.toplamVucutKayipOrani BETWEEN 0 AND 29 THEN '%0-29' " +
                        "  WHEN e.toplamVucutKayipOrani BETWEEN 30 AND 49 THEN '%30-49' " +
                        "  WHEN e.toplamVucutKayipOrani BETWEEN 50 AND 69 THEN '%50-69' " +
                        "  WHEN e.toplamVucutKayipOrani BETWEEN 70 AND 89 THEN '%70-89' " +
                        "  ELSE '%90-100' " +
                        "END, COUNT(e) " +
                        "FROM EyKisi e " +
                        "WHERE e.aktif=true AND e.gnlKisi.aktif=true " +
                        "AND e.gnlKisi.gnlMahalle=:mahalle " +
                        "AND e.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG " +
                        "AND e.toplamVucutKayipOrani IS NOT NULL " +
                        "GROUP BY " +
                        "CASE " +
                        "  WHEN e.toplamVucutKayipOrani BETWEEN 0 AND 29 THEN '%0-29' " +
                        "  WHEN e.toplamVucutKayipOrani BETWEEN 30 AND 49 THEN '%30-49' " +
                        "  WHEN e.toplamVucutKayipOrani BETWEEN 50 AND 69 THEN '%50-69' " +
                        "  WHEN e.toplamVucutKayipOrani BETWEEN 70 AND 89 THEN '%70-89' " +
                        "  ELSE '%90-100' " +
                        "END " +
                        "ORDER BY 1",
                Object[].class).setParameter("mahalle", mahalle).getResultList();

        Map<String, Integer> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], ((Long) row[1]).intValue());
        }
        return map;
    }

    private Map<String, Integer> getEngelGrubuDagilimi(GnlMahalle mahalle) {
        List<Object[]> results = em.createQuery(
                "SELECT keg.eyEngelGrubu.tanim, COUNT(keg) " +
                        "FROM EyKisiEngelGrubu keg " +
                        "WHERE keg.aktif=true AND keg.secili=true " +
                        "AND keg.eyKisi.aktif=true AND keg.eyKisi.gnlKisi.aktif=true " +
                        "AND keg.eyKisi.gnlKisi.gnlMahalle=:mahalle " +
                        "AND keg.eyKisi.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG " +
                        "GROUP BY keg.eyEngelGrubu.tanim " +
                        "ORDER BY COUNT(keg) DESC",
                Object[].class).setParameter("mahalle", mahalle).setMaxResults(5).getResultList();

        Map<String, Integer> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], ((Long) row[1]).intValue());
        }
        return map;
    }

    private Map<String, Integer> getCihazTeslimGrubuDagilimi(GnlMahalle mahalle) {
        List<Object[]> results = em.createQuery(
                "SELECT t.eyArac.tanim, COUNT(t) " +
                        "FROM EyAracCihazTeslimi t " +
                        "WHERE t.aktif=true AND t.eyKisi.aktif=true " +
                        "AND t.eyKisi.gnlKisi.gnlMahalle=:mahalle " +
                        "GROUP BY t.eyArac.tanim " +
                        "ORDER BY COUNT(t) DESC",
                Object[].class).setParameter("mahalle", mahalle).setMaxResults(5).getResultList();

        Map<String, Integer> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], ((Long) row[1]).intValue());
        }
        return map;
    }

    private Map<String, Integer> getEgitimGrubuDagilimi(GnlMahalle mahalle) {
        List<Object[]> results = em.createQuery(
                "SELECT e.gnlKisi.egitimDurumu , COUNT(e) " +
                        "FROM EyKisi e " +
                        "WHERE e.aktif=true AND e.gnlKisi.aktif=true " +
                        "AND e.gnlKisi.gnlMahalle=:mahalle " +
                        "AND e.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG " +
                        "AND e.gnlKisi.egitimDurumu IS NOT NULL " +
                        "GROUP BY e.gnlKisi.egitimDurumu " +
                        "ORDER BY COUNT(e) DESC",
                Object[].class).setParameter("mahalle", mahalle).setMaxResults(5).getResultList();

        Map<String, Integer> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            String key = row[0] != null ? ((BaseEnum) row[0]).getDisplayValue() : "Tanımsız";
            map.put(key, ((Long) row[1]).intValue());
        }
        return map;
    }

    private Map<String, Integer> getMedeniGrubuDagilimi(GnlMahalle mahalle) {
        List<Object[]> results = em.createQuery(
                "SELECT e.gnlKisi.medeniDurum, COUNT(e) " +
                        "FROM EyKisi e " +
                        "WHERE e.aktif=true AND e.gnlKisi.aktif=true " +
                        "AND e.gnlKisi.gnlMahalle=:mahalle " +
                        "AND e.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG " +
                        "AND e.gnlKisi.medeniDurum IS NOT NULL " +
                        "GROUP BY e.gnlKisi.medeniDurum " +
                        "ORDER BY COUNT(e) DESC",
                Object[].class).setParameter("mahalle", mahalle).getResultList();

        Map<String, Integer> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            String key = row[0] != null ? ((BaseEnum) row[0]).getDisplayValue() : "Tanımsız";
            map.put(key, ((Long) row[1]).intValue());
        }
        return map;
    }

    private Map<String, Integer> getTamirGrubuDagilimi(GnlMahalle mahalle) {
        List<Object[]> results = em.createQuery(
                "SELECT t.aracBilgisi, COUNT(t) " +
                        "FROM EyAracTamir t " +
                        "WHERE t.aktif=true AND t.eyKisi.gnlKisi.gnlMahalle=:mahalle " +
                        "GROUP BY t.aracBilgisi " +
                        "ORDER BY COUNT(t) DESC",
                Object[].class).setParameter("mahalle", mahalle).getResultList();

        Map<String, Integer> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            String key = row[0] != null ? ((BaseEnum) row[0]).getDisplayValue() : "Tanımsız";
            map.put(key, ((Long) row[1]).intValue());
        }
        return map;
    }

    private Map<String, Integer> getTalepGrubuDagilimi(GnlMahalle mahalle) {
        List<Object[]> results = em.createQuery(
                "SELECT t.durum , COUNT(t) " +
                        "FROM EyTalep t " +
                        "WHERE t.aktif=true AND t.eyKisi.gnlKisi.gnlMahalle=:mahalle " +
                        "GROUP BY t.durum " +
                        "ORDER BY COUNT(t) DESC",
                Object[].class).setParameter("mahalle", mahalle).getResultList();

        Map<String, Integer> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            String key = row[0] != null ? ((BaseEnum) row[0]).getDisplayValue() : "Tanımsız";
            map.put(key, ((Long) row[1]).intValue());
        }
        return map;
    }
}
