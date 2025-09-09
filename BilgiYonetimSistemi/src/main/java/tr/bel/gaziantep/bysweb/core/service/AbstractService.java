package tr.bel.gaziantep.bysweb.core.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:34
 */
public abstract class AbstractService<T> implements java.io.Serializable {


    @Serial
    private static final long serialVersionUID = 2741890903506445646L;
    private Class<T> entityClass;

    @Getter
    @Setter
    private String sortCol;
    @Getter
    @Setter
    private String sorting;

    public AbstractService() {
    }

    public AbstractService(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void createAll(List<T> entities) {
        entities.forEach(this::create);
    }

    public void createRefresh(T entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
        getEntityManager().refresh(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void editAll(List<T> entities) {
        entities.forEach(this::edit);
    }

    public T refreshEdit(T entity) {
        entity = getEntityManager().merge(entity);
        return entity;
    }

    public void flush() {
        getEntityManager().flush();
    }

    public void refresh(T entity) {
        getEntityManager().refresh(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public void removeSlient(T entity) {
        ((BaseEntity) entity).setAktif(false);
        getEntityManager().merge(entity);
    }

    public void clearCache() {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public T findLast() {
        return getEntityManager()
                .createQuery("SELECT s FROM " + entityClass.getSimpleName() + " s ORDER BY s.id DESC", entityClass)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public List<T> findAll() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        cq.where(cb.equal(root.get(Constants.AKTIF), true));
        if (!StringUtil.isBlank(getSortCol())) {
            cq.orderBy(cb.asc(root.get(getSortCol())));
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findAll(Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        cq.where(getFilterCondition(cb, root, filters));
        if (!StringUtil.isBlank(getSortCol())) {
            cq.orderBy(cb.asc(root.get(getSortCol())));
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        jakarta.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        jakarta.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        cq.where(cb.equal(rt.get(Constants.AKTIF), true));
        jakarta.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<T> loadLazy(int first, int pageSize, Map<String, SortMeta> sortMetaMap, Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.where(getFilterCondition(cb, root, filters));

        if (!sortMetaMap.isEmpty()) {
            List<Order> orderList = new ArrayList();
            for (SortMeta sortMeta : sortMetaMap.values()) {
                String sortField = sortMeta.getField();
                if (sortField.contains(".")) {
                    String[] sortFields = sortField.split(Pattern.quote("."));
                    Path path = pathGenerate(root, sortFields);
                    if (sortMeta.getOrder().equals(SortOrder.ASCENDING)) {
                        orderList.add(cb.asc(path));
                    } else if (sortMeta.getOrder().equals(SortOrder.DESCENDING)) {
                        orderList.add(cb.desc(path));
                    } else {
                        orderList.add(cb.desc(root.get(getSortCol())));
                    }
                } else {
                    if (sortMeta.getOrder().equals(SortOrder.ASCENDING)) {
                        orderList.add(cb.asc(root.get(sortField)));
                    } else if (sortMeta.getOrder().equals(SortOrder.DESCENDING)) {
                        orderList.add(cb.desc(root.get(sortField)));
                    } else {
                        orderList.add(cb.asc(root.get(getSortCol())));
                    }
                }
            }
            cq.orderBy(orderList);
        } else if (!StringUtil.isBlank(getSortCol())) {
            if (StringUtil.isBlank(getSorting())) {
                if (getSortCol().contains(".")) {
                    String[] sortFields = getSortCol().split(Pattern.quote("."));
                    Path path = pathGenerate(root, sortFields);
                    cq.orderBy(cb.asc(path));
                } else {
                    cq.orderBy(cb.asc(root.get(getSortCol())));
                }
            } else {
                cq.orderBy(cb.desc(root.get(getSortCol())));
            }

        }

        return this.getEntityManager().createQuery(cq).setFirstResult(first).setMaxResults(pageSize).getResultList();
    }

    protected Predicate getFilterCondition(CriteriaBuilder cb, Root<T> root, Map<String, FilterMeta> filters) {
        Predicate filterCondition = cb.conjunction();

        for (FilterMeta filter : filters.values()) {
            if (filter.getFilterValue() == null) {
                continue;
            }

            String filterField = filter.getField();
            String value = filter.getFilterValue().toString().toLowerCase(Constants.LOCALE);
            if ("SyKullanici".equals(entityClass.getSimpleName())) {
                value = filter.getFilterValue().toString().toLowerCase(Constants.LOCALE);
            }

            MatchMode matchMode = filter.getMatchMode();
            Path<?> path = pathGenerate(root, filterField.split(Pattern.quote(".")));

            if ("BigDecimal".equals(path.getJavaType().getSimpleName()) && "-".equals(value)) {
                continue;
            }

            filterCondition = cb.and(filterCondition, applyMatchMode(cb, path, value, filter, matchMode));
        }

        if(!"SyKullanici".equals(entityClass.getSimpleName())){
            filterCondition = cb.and(filterCondition, cb.equal(root.get(Constants.AKTIF), true));
        }

        return filterCondition;
    }

    private Predicate applyMatchMode(CriteriaBuilder cb, Path<?> path, String value, FilterMeta filter, MatchMode matchMode) {
        switch (matchMode) {
            case EQUALS:
                return handleEquals(cb, path, value, filter);
            case STARTS_WITH:
                return cb.like(path.as(String.class), value + "%");
            case CONTAINS:
                return cb.like(path.as(String.class), "%" + value + "%");
            case IN:
                return handleIn(cb, path, filter);
            case NOT_IN:
                return  handleNotIn(cb,path,filter);
            case BETWEEN:
                return handleBetween(cb, path, filter);
            case GREATER_THAN_EQUALS, GREATER_THAN, LESS_THAN, LESS_THAN_EQUALS:
                return comparableResult(cb, path, filter, matchMode);
            case NOT_EQUALS:
                return cb.notEqual(path, filter.getFilterValue());
            default:
                throw new UnsupportedOperationException("Unsupported match mode: " + matchMode);
        }
    }

    private Predicate comparableResult(CriteriaBuilder cb, Path<?> path, FilterMeta filter, MatchMode matchMode) {
        switch (matchMode) {
            case GREATER_THAN_EQUALS -> {
                if (Comparable.class.isAssignableFrom(path.getJavaType())) {
                    Path<Comparable<Object>> comparablePath = (Path<Comparable<Object>>) path;
                    Comparable<Object> compValue = (Comparable<Object>) filter.getFilterValue();
                    return cb.greaterThanOrEqualTo(comparablePath, compValue);
                } else {
                    throw new IllegalArgumentException("The type of the path is not comparable for GREATER_THAN.");
                }
            }
            case GREATER_THAN -> {
                if (Comparable.class.isAssignableFrom(path.getJavaType())) {
                    Path<Comparable<Object>> comparablePath = (Path<Comparable<Object>>) path;
                    Comparable<Object> compValue = (Comparable<Object>) filter.getFilterValue();
                    return cb.greaterThan(comparablePath, compValue);
                } else {
                    throw new IllegalArgumentException("The type of the path is not comparable for GREATER_THAN.");
                }
            }
            case LESS_THAN_EQUALS -> {
                if (Comparable.class.isAssignableFrom(path.getJavaType())) {
                    Path<Comparable<Object>> comparablePath = (Path<Comparable<Object>>) path;
                    Comparable<Object> compValue = (Comparable<Object>) filter.getFilterValue();
                    return cb.lessThanOrEqualTo(comparablePath, compValue);
                } else {
                    throw new IllegalArgumentException("The type of the path is not comparable for LESS_THAN_EQUALS.");
                }
            }
            case LESS_THAN -> {
                if (Comparable.class.isAssignableFrom(path.getJavaType())) {
                    Path<Comparable<Object>> comparablePath = (Path<Comparable<Object>>) path;
                    Comparable<Object> compValue = (Comparable<Object>) filter.getFilterValue();
                    return cb.lessThan(comparablePath, compValue);
                } else {
                    throw new IllegalArgumentException("The type of the path is not comparable for LESS_THAN.");
                }
            }
        }
        return null;
    }

    private Predicate handleEquals(CriteriaBuilder cb, Path<?> path, String value, FilterMeta filter) {
        if (filter.getFilterValue().getClass().isEnum()) {
            return cb.equal(path, filter.getFilterValue());
        } else if (filter.getFilterValue() instanceof LocalDate || filter.getFilterValue() instanceof LocalDateTime) {
            LocalDate date = DateUtil.stringToLocalDate(filter.getFilterValue().toString(), "yyyy-MM-dd");
            return cb.equal(path, date);
        } else if (filter.getFilterValue() instanceof Boolean result) {
            Expression<Boolean> booleanExpression = path.as(Boolean.class);
            return result ? cb.isTrue(booleanExpression) : cb.isFalse(booleanExpression);
        } else if (value.equals("null")) {
            return cb.isNull(path);
        } else if (value.contains("_month")) {
            return cb.equal(cb.function("MONTH", Integer.class, path), value.replace("_month", ""));
        } else if (value.contains("_year")) {
            return cb.equal(cb.function("YEAR", Integer.class, path), value.replace("_year", ""));
        } else if ("BigDecimal".equals(path.getJavaType().getSimpleName())) {
            return cb.equal(path, new BigDecimal(value));
        } else {
            return cb.equal(path, value);
        }
    }

    private Predicate handleIn(CriteriaBuilder cb, Path<?> path, FilterMeta filter) {
        List<?> valueList = (List<?>) filter.getFilterValue();
        if (valueList.get(0).getClass().isEnum()) {
            CriteriaBuilder.In<Object> inClause = cb.in(path);
            for (Object value : valueList) {
                inClause.value(value);
            }
            return inClause;
        } else {
            return path.in(valueList);
        }
    }

    private Predicate handleNotIn(CriteriaBuilder cb, Path<?> path, FilterMeta filter) {
        List<?> valueList = (List<?>) filter.getFilterValue();
        if (valueList.get(0).getClass().isEnum()) {
            CriteriaBuilder.In<Object> inClause = cb.in(path);
            for (Object value : valueList) {
                inClause.value(value);
            }
            return cb.not(inClause);
        } else {
            return cb.not(path.in(valueList));
        }
    }

    private Predicate handleBetween(CriteriaBuilder cb, Path<?> path, FilterMeta filter) {
        String[] values = filter.getFilterValue().toString().replace("[", "").replace("]", "").split("\\s*,\\s*");
        LocalDateTime before = DateUtil.atStartOfDay(DateUtil.stringToLocalDate(values[0], "yyyy-MM-dd"));
        LocalDateTime after = DateUtil.atEndOfDay(DateUtil.stringToLocalDate(values[1], "yyyy-MM-dd"));
        return cb.between(path.as(LocalDateTime.class), before, after);
    }

    private Path pathGenerate(Root<T> root, String[] parts) {
        Path expression = null;

        expression = root.get(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            expression = expression.get(part);
        }
        return expression;
    }

    public int count(Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> myObj = cq.from(entityClass);
        cq.where(getFilterCondition(cb, myObj, filters));
        cq.select(cb.count(myObj));
        return this.getEntityManager().createQuery(cq).getSingleResult().intValue();
    }


}