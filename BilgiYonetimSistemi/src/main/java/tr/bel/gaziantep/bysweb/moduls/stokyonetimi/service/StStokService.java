package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyKullaniciTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepoYetki;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBakiye;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 10:56
 */
@Stateless
public class StStokService extends AbstractService<StStok> {

    @Serial
    private static final long serialVersionUID = -5967577744037968421L;

    private static final String DEPO_MIKTAR_FIELD = "depoMiktar_";
    private static final String TOPLAM_FIELD = "depoMiktar_TOPLAM";

    @Inject
    private StDepoYetkiService stDepoYetkiService;
    @Inject
    private StDepoService stDepoService;

    public StStokService() {
        super(StStok.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Toplam miktar kolonunun kapsamini belirleyen yetkili depo ID'leri.
     * Liste ile ayni kural: sistem yoneticisi tum aktif depolar, diger kullanici yetki kayitlari.
     */
    private List<Integer> getYetkiliDepoIds() {
        SyKullanici kullanici = Util.getSyKullanici();
        if (kullanici == null) {
            return List.of();
        }
        if (kullanici.getKullaniciTuru() == EnumSyKullaniciTuru.SISTEM_YONETICISI) {
            return stDepoService.findAll().stream()
                    .map(StDepo::getId)
                    .filter(Objects::nonNull)
                    .toList();
        }
        return stDepoYetkiService.findByKullanici(kullanici).stream()
                .map(StDepoYetki::getStDepo)
                .filter(Objects::nonNull)
                .map(StDepo::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    public static String depoMiktarField(Integer depoId) {
        return DEPO_MIKTAR_FIELD + depoId;
    }

    public static String toplamMiktarField() {
        return TOPLAM_FIELD;
    }

    private boolean isDepoMiktarField(String field) {
        return field != null && field.startsWith(DEPO_MIKTAR_FIELD);
    }

    private boolean hasDepoMiktarField(Map<String, ?> map) {
        return map != null && map.keySet().stream().anyMatch(this::isDepoMiktarField);
    }

    private Integer depoIdFromField(String field) {
        if (!isDepoMiktarField(field) || TOPLAM_FIELD.equals(field)) {
            return null;
        }
        try {
            return Integer.valueOf(field.substring(DEPO_MIKTAR_FIELD.length()));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Depo miktari (veya toplam) icin stok satirina bagli alt sorgu.
     */
    private Subquery<BigDecimal> miktarSubquery(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<StStok> root, String field) {
        Subquery<BigDecimal> sq = cq.subquery(BigDecimal.class);
        Root<StStokBakiye> bakiye = sq.from(StStokBakiye.class);
        sq.select(cb.coalesce(cb.sum(bakiye.get("miktar")), BigDecimal.ZERO));
        if (TOPLAM_FIELD.equals(field)) {
            List<Integer> depoIds = getYetkiliDepoIds();
            sq.where(cb.and(
                    cb.equal(bakiye.get("stStok"), root),
                    cb.isTrue(bakiye.get("aktif")),
                    depoIds.isEmpty() ? cb.disjunction() : bakiye.get("stDepo").get("id").in(depoIds)));
        } else {
            Integer depoId = depoIdFromField(field);
            sq.where(cb.and(
                    cb.equal(bakiye.get("stStok"), root),
                    cb.isTrue(bakiye.get("aktif")),
                    cb.equal(bakiye.get("stDepo").get("id"), depoId)));
        }
        return sq;
    }

    private Predicate depoMiktarPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq,
                                          Root<StStok> root, FilterMeta filter) {
        Object filterValue = filter.getFilterValue();
        if (filterValue == null || "-".equals(filterValue.toString())) {
            return cb.conjunction();
        }
        BigDecimal value;
        try {
            value = new BigDecimal(filterValue.toString().trim());
        } catch (NumberFormatException e) {
            return cb.conjunction();
        }
        Subquery<BigDecimal> sq = miktarSubquery(cb, cq, root, filter.getField());
        MatchMode mode = filter.getMatchMode() == null ? MatchMode.EQUALS : filter.getMatchMode();
        return switch (mode) {
            case EQUALS -> cb.equal(sq, value);
            case NOT_EQUALS -> cb.notEqual(sq, value);
            case GREATER_THAN -> cb.greaterThan(sq, value);
            case GREATER_THAN_EQUALS -> cb.greaterThanOrEqualTo(sq, value);
            case LESS_THAN -> cb.lessThan(sq, value);
            case LESS_THAN_EQUALS -> cb.lessThanOrEqualTo(sq, value);
            default -> cb.conjunction();
        };
    }

    private Path<?> stokPath(Root<StStok> root, String field, Map<String, Join<?, ?>> joins) {
        From<?, ?> from = root;
        String[] parts = field.split("\\.");
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            From<?, ?> current = from;
            from = (From<?, ?>) joins.computeIfAbsent(part, p -> current.join(p, JoinType.LEFT));
        }
        return from.get(parts[parts.length - 1]);
    }

    @Override
    public int count(Map<String, FilterMeta> filters) {
        if (!hasDepoMiktarField(filters)) {
            return super.count(filters);
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<StStok> root = cq.from(StStok.class);

        Map<String, FilterMeta> normalFilters = new HashMap<>(filters);
        normalFilters.keySet().removeIf(this::isDepoMiktarField);
        Predicate where = getFilterCondition(cb, root, normalFilters, new HashMap<>());
        for (FilterMeta filter : filters.values()) {
            if (isDepoMiktarField(filter.getField())) {
                where = cb.and(where, depoMiktarPredicate(cb, cq, root, filter));
            }
        }
        cq.where(where).select(cb.count(root));
        return em.createQuery(cq).getSingleResult().intValue();
    }

    @Override
    public List<StStok> loadLazy(int first, int pageSize, Map<String, SortMeta> sortMetaMap,
                                 Map<String, FilterMeta> filters) {
        if (!hasDepoMiktarField(sortMetaMap) && !hasDepoMiktarField(filters)) {
            return super.loadLazy(first, pageSize, sortMetaMap, filters);
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StStok> cq = cb.createQuery(StStok.class);
        Root<StStok> root = cq.from(StStok.class);
        Map<String, Join<?, ?>> joins = new HashMap<>();

        Map<String, FilterMeta> normalFilters = new HashMap<>(filters == null ? Map.of() : filters);
        normalFilters.keySet().removeIf(this::isDepoMiktarField);
        Predicate where = getFilterCondition(cb, root, normalFilters, joins);
        if (filters != null) {
            for (FilterMeta filter : filters.values()) {
                if (isDepoMiktarField(filter.getField())) {
                    where = cb.and(where, depoMiktarPredicate(cb, cq, root, filter));
                }
            }
        }
        cq.where(where);

        List<Order> orders = new ArrayList<>();
        if (sortMetaMap != null && !sortMetaMap.isEmpty()) {
            for (SortMeta sortMeta : sortMetaMap.values()) {
                String field = sortMeta.getField();
                boolean desc = sortMeta.getOrder() == SortOrder.DESCENDING;
                if (isDepoMiktarField(field)) {
                    Subquery<BigDecimal> sq = miktarSubquery(cb, cq, root, field);
                    orders.add(desc ? cb.desc(sq) : cb.asc(sq));
                } else if (field != null && field.contains(".")) {
                    Path<?> path = stokPath(root, field, joins);
                    orders.add(desc ? cb.desc(path) : cb.asc(path));
                } else if (field != null) {
                    orders.add(desc ? cb.desc(root.get(field)) : cb.asc(root.get(field)));
                }
            }
            cq.orderBy(orders);
        } else if (!StringUtil.isBlank(getSortCol())) {
            Path<?> path = getSortCol().contains(".") ? stokPath(root, getSortCol(), joins) : root.get(getSortCol());
            orders.add(StringUtil.isBlank(getSorting()) ? cb.asc(path) : cb.desc(path));
            cq.orderBy(orders);
        }

        return em.createQuery(cq).setFirstResult(first).setMaxResults(pageSize).getResultList();
    }

    /**
     * Barkod okutulamadiysa stok kodu / malzeme kodu ile de arama yapilir.
     */
    public StStok findByKod(String kod) {
        if (kod == null || kod.isBlank()) {
            return null;
        }
        return em.createQuery("SELECT s FROM StStok s WHERE (s.stokKodu = :kod OR s.malzemeKodu = :kod) AND s.aktif = true",
                        StStok.class)
                .setParameter("kod", kod.trim())
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(StStok entity) {
        if (entity != null) {
            if (entity.getMalzemeKodu() == null || entity.getMalzemeKodu().isBlank()) {
                entity.setMalzemeKodu(getNextMalzemeKodu());
            }
            if (entity.getStokKodu() == null || entity.getStokKodu().isBlank()) {
                entity.setStokKodu(getNextStokKodu());
            }
        }
        super.create(entity);
    }

    public String getNextMalzemeKodu() {
        List<String> codes = em.createQuery(
                        "SELECT s.malzemeKodu FROM StStok s WHERE s.malzemeKodu IS NOT NULL AND s.malzemeKodu <> '' ORDER BY s.id DESC",
                        String.class)
                .setMaxResults(100)
                .getResultList();
        return incrementCode(codes, "00001");
    }

    public String getNextStokKodu() {
        List<String> codes = em.createQuery(
                        "SELECT s.stokKodu FROM StStok s WHERE s.stokKodu IS NOT NULL AND s.stokKodu <> '' ORDER BY s.id DESC",
                        String.class)
                .setMaxResults(100)
                .getResultList();
        return incrementCode(codes, "00001");
    }

    private String incrementCode(List<String> codes, String defaultValue) {
        String bestCode = null;
        long bestNum = -1;
        String prefix = "";
        int padLength = defaultValue.replaceAll("\\D", "").length();
        boolean dotFormat = false;

        for (String code : codes) {
            if (code == null) continue;
            String trimmed = code.trim();
            if (trimmed.isEmpty()) continue;

            if (trimmed.contains(".")) {
                String[] parts = trimmed.split("\\.");
                boolean allNumeric = true;
                for (String part : parts) {
                    if (!part.matches("\\d+")) {
                        allNumeric = false;
                        break;
                    }
                }
                if (allNumeric) {
                    try {
                        long num = Long.parseLong(parts[parts.length - 1]);
                        // also consider earlier parts for comparison
                        StringBuilder sb = new StringBuilder();
                        for (String part : parts) sb.append(part);
                        long fullNum = Long.parseLong(sb.toString());
                        if (fullNum > bestNum) {
                            bestNum = fullNum;
                            bestCode = trimmed;
                            dotFormat = true;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                    continue;
                }
            }

            java.util.regex.Matcher m = java.util.regex.Pattern.compile("^(.*?)(\\d+)$").matcher(trimmed);
            if (m.matches()) {
                try {
                    long num = Long.parseLong(m.group(2));
                    // compare by full numeric value for plain suffix codes
                    StringBuilder sb = new StringBuilder();
                    for (char c : trimmed.toCharArray()) {
                        if (Character.isDigit(c)) sb.append(c);
                    }
                    long fullNum = sb.length() > 0 ? Long.parseLong(sb.toString()) : num;
                    if (!dotFormat && fullNum > bestNum) {
                        bestNum = fullNum;
                        bestCode = trimmed;
                        prefix = m.group(1);
                        padLength = m.group(2).length();
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (bestCode == null) {
            return defaultValue;
        }
        if (dotFormat) {

            return incrementDotCode(bestCode);
        }
        return prefix + String.format("%0" + padLength + "d", Long.parseLong(bestCode.substring(prefix.length())) + 1);
    }

    private String incrementDotCode(String code) {
        String[] parts = code.split("\\.");
        int lastIndex = parts.length - 1;
        try {
            int lastValue = Integer.parseInt(parts[lastIndex]);
            int width = parts[lastIndex].length();
            parts[lastIndex] = String.format("%0" + width + "d", lastValue + 1);
            return String.join(".", parts);
        } catch (NumberFormatException e) {
            return code;
        }
    }
}

