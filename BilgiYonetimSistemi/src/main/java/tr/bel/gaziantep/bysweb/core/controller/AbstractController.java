package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJBException;
import jakarta.el.ELContext;
import jakarta.el.ValueExpression;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.ExcelOptions;
import org.primefaces.event.ColumnResizeEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.Visibility;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.*;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKolon;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullaniciKolon;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyKolonService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyKullaniciKolonService;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 08:19
 */
@Slf4j
public abstract class AbstractController<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -991999085838138765L;

    @Inject
    private AbstractService<T> ejbService;
    @Inject
    private SyKullaniciKolonService syKullaniciKolonService;
    @Inject
    private SyKolonService syKolonService;


    private Class<T> itemClass;
    @Getter
    private T selected;
    @Setter
    private Collection<T> items;
    @Setter
    private LazyDataModel<T> lazyItems;
    @Getter
    @Setter
    private List<T> filtered = new ArrayList<>();
    @Getter
    @Setter
    private Object paramItems;
    @Getter
    @Setter
    private String tableName;
    @Getter
    @Setter
    private Map<String, FilterMeta> filterMap = new HashMap<>();
    @Getter
    @Setter
    private List<SyKolon> columns;
    @Getter
    @Setter
    private List<SyKullaniciKolon> kullaniciKolons;
    @Setter
    private ExcelOptions excelOpt;
    @Setter
    private SyKullanici syKullanici;

    private enum PersistAction {
        CREATE,
        DELETE,
        UPDATE
    }

    public AbstractController() {

    }

    public AbstractController(Class<T> itemClass) {
        this.itemClass = itemClass;
    }


    public Class<T> getItemClass() {
        return itemClass;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        // Nothing to do if entity does not have any embeddable key.
    }

    protected void initializeEmbeddableKey() {
        // Nothing to do if entity does not have any embeddable key.
    }

    public Collection<T> getItems() {
        if (items == null) {
            items = this.ejbService.findAll();
        }
        return items;
    }

    public LazyDataModel<T> getLazyItems() {
        if (lazyItems == null) {
            lazyItems = new LazyDataModel<T>() {

                @Serial
                private static final long serialVersionUID = -2280622519706862946L;

                List<T> result;

                @Override
                public int count(Map<String, FilterMeta> map) {
                    return ejbService.count(map);
                }

                @Override
                public List<T> load(int first, int pageSize, Map<String, SortMeta> sortMetaMap, Map<String, FilterMeta> filters) {
                    Map<String, FilterMeta> newMaps = new HashMap<>(filters);
                    if (filterMap != null && !filterMap.isEmpty()) {
                        newMaps.putAll(filterMap);
                    }

                    if (getParamItems() != null) {
                        result = (List<T>) paramItems;
                        lazyItems.setRowCount(result.size());
                    } else {
                        result = ejbService.loadLazy(first, pageSize, sortMetaMap, newMaps);

                        int count = ejbService.count(newMaps);
                        setRowCount(count);
                    }
                    return result;
                }

                @Override
                public T getRowData(String rowKey) {
                    String type = Function.pkFieldType(itemClass);
                    if (StringUtil.isBlank(type)) {
                        return null;
                    }
                    if (type.contains("Short")) {
                        return rowKey.equals("null") ? null : ejbService.find(Short.parseShort(rowKey));
                    } else if (type.contains("Integer")) {
                        return rowKey.equals("null") ? null : ejbService.find(Integer.parseInt(rowKey));
                    } else if (type.contains("Long")) {
                        return rowKey.equals("null") ? null : ejbService.find(Long.parseLong(rowKey));
                    } else {
                        return null;
                    }
                }

                @Override
                public String getRowKey(T rowClass) {
                    return String.valueOf(Function.pkFieldValue(rowClass));
                }
            };
        }
        return lazyItems;
    }


    public ExcelOptions getExcelOpt() {
        if (excelOpt == null) {
            excelOpt = new ExcelOptions();
            excelOpt.setStronglyTypedCells(false);
        }
        return excelOpt;
    }

    public SyKullanici getSyKullanici() {
        if(syKullanici==null){
            syKullanici= Util.getSyKullanici();
        }
        return syKullanici;
    }

    public FilterMeta getFilter(String field) {
        return filterMap.computeIfAbsent(field, k -> FilterMeta.builder().field(field).build());
    }

    public void save(ActionEvent event) {
        String msg = FacesUtil.message(Constants.KAYIT_GUNCELLENDI);
        persist(PersistAction.UPDATE, msg);
    }

    public void saveNew(ActionEvent event) {
        String msg = FacesUtil.message(Constants.KAYIT_EKLENDI);
        persist(PersistAction.CREATE, msg);
        if (!isValidationFailed()) {
            items = null;
            lazyItems = null;
        }
    }

    public void delete(ActionEvent event) {
        String msg = FacesUtil.message(Constants.KAYIT_SILINDI);
        persist(PersistAction.DELETE, msg);
        if (!isValidationFailed()) {
            selected = null;
            items = null;
            lazyItems = null;
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            this.setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    this.ejbService.create(selected);
                } else if (persistAction == PersistAction.UPDATE) {
                    this.ejbService.edit(selected);
                } else {
                    ((BaseEntity) selected).setAktif(false);
                    this.ejbService.edit(selected);

                }
                FacesUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                Throwable cause = FacesUtil.getRootCause(ex.getCause());
                if (cause != null) {
                    if (cause instanceof ConstraintViolationException) {
                        ConstraintViolationException excp = (ConstraintViolationException) cause;
                        for (ConstraintViolation s : excp.getConstraintViolations()) {
                            FacesUtil.addErrorMessage(s.getMessage());
                        }
                    } else {
                        String msg = cause.getLocalizedMessage();
                        if (msg.isEmpty()) {
                            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
                        } else {
                            FacesUtil.addErrorMessage(msg);
                        }

                    }
                }
            } catch (Exception ex) {
                log.error(null,ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public T prepareCreate(ActionEvent event) {
        T newItem;
        try {
            newItem = itemClass.getDeclaredConstructor().newInstance();
            this.selected = newItem;
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    public boolean isValidationFailed() {
        return FacesUtil.isValidationFailed();
    }

    public void resetParents() {

    }

    @PostConstruct
    public void init() {
        readColumns();
    }

    public void readColumns() {
        readColumns(this.getSyKullanici());
    }

    public void readColumns(SyKullanici kullanici) {
        if (kullanici != null) {
            try {
                if (StringUtil.isBlank(tableName)) {
                    tableName = itemClass.getSimpleName();
                }
                kullaniciKolons = syKullaniciKolonService.findByKullaniciByTablo(kullanici, tableName);
                columns = syKolonService.findByTabloAdi(tableName);

                int count = 0;
                for (SyKolon column : columns) {
                    SyKullaniciKolon kullaniciKolon = kullaniciKolons
                            .stream()
                            .filter(x -> x.getSyKolon().equals(column) && x.isAktif())
                            .findFirst()
                            .orElse(null);
                    if (kullaniciKolon == null) {
                        kullaniciKolon = SyKullaniciKolon.builder()
                                .syKolon(column)
                                .syKullanici(kullanici)
                                .genislik(400)
                                .gorunurluk(true)
                                .siraNo(count)
                                .build();
                        syKullaniciKolonService.create(kullaniciKolon);
                        kullaniciKolons.add(kullaniciKolon);
                    }
                    count++;
                }
            } catch (Exception ex) {
                log.error(null,ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void onToggle(org.primefaces.event.ToggleEvent e) {
        try {
            SyKullaniciKolon kullaniciKolon = kullaniciKolons.get((Integer) e.getData());
            if (kullaniciKolon != null) {
                kullaniciKolon.setGorunurluk(e.getVisibility() == Visibility.VISIBLE);
                syKullaniciKolonService.edit(kullaniciKolon);
            }
        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void onResize(ColumnResizeEvent event) {
        try {
            String[] str = event.getColumn().getColumnKey().split(":");
            int length = str.length;
            boolean isParsable = NumberUtil.isParsable(str[length - 1]);
            SyKullaniciKolon kullaniciKolon;
            if (isParsable) {
                int colIndex = Integer.parseInt(str[length - 1]);
                kullaniciKolon = kullaniciKolons.get(colIndex);
            } else {
                kullaniciKolon = kullaniciKolons.stream().filter(x -> x.getSyKolon().getColumnProperty().equals(str[length - 1])).findFirst().orElse(null);
            }

            if (kullaniciKolon != null) {
                kullaniciKolon.setGenislik(event.getWidth());
                syKullaniciKolonService.edit(kullaniciKolon);
            }
        } catch (Exception ex) {
            log.error(null,ex);
//            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            throw ex;
        }

    }

    public void onReorder(AjaxBehaviorEvent event) {
        try {
            DataTable dt = (DataTable) event.getSource();
            List<UIColumn> columnList = dt.getColumns();
            int count = 0;
            for (UIColumn uiColumn : columnList) {
                DynamicColumn dynamicColumn = (DynamicColumn) uiColumn;
                SyKullaniciKolon kullaniciKolon = kullaniciKolons.get(dynamicColumn.getIndex());
                if (kullaniciKolon != null) {
                    kullaniciKolon.setSiraNo(count);
                    syKullaniciKolonService.edit(kullaniciKolon);
                }
                count++;
            }
        } catch (Exception ex) {
            log.error(null,ex);
            //FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            throw ex;
        }
    }


    public void saveColumns() {
        if (kullaniciKolons != null && !kullaniciKolons.isEmpty()) {
            try {
                for (SyKullaniciKolon kolonlar : kullaniciKolons) {
                    syKullaniciKolonService.edit(kolonlar);
                }
                FacesUtil.successMessage("kolonlarKaydedildi");
            } catch (Exception ex) {
                log.error(null,ex);
                throw ex;
            }

        }
    }

    public void resetColumns() {
        if (kullaniciKolons != null && !kullaniciKolons.isEmpty()) {
            try {
                kullaniciKolons.forEach(column -> {
                    column.setGorunurluk(Boolean.TRUE);
                    column.setGenislik(400);
                    syKullaniciKolonService.edit(column);
                });
                FacesUtil.successMessage("kolonlarKaydedildi");
            } catch (Exception ex) {
                log.error(null,ex);
//                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
                throw ex;
            }
        }
    }

    public Object resolveProperty(Object item, String property) {
        if (item == null || property == null || property.isEmpty()) {
            return null;
        }
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ELContext elContext = facesContext.getELContext();
            ValueExpression ve = facesContext.getApplication()
                    .getExpressionFactory()
                    .createValueExpression(elContext, "#{item." + property + "}", Object.class);
            return ve.getValue(elContext);
        } catch (Exception e) {
            throw e;
//            return null;
        }
    }

    public Object getResolvedValue(Object item, String property) {
        return resolveProperty(item, property);
    }

    public void changeFilterMatchMode(SyKullaniciKolon kullaniciKolon) {
        if (kullaniciKolon == null) return;
        syKullaniciKolonService.edit(kullaniciKolon);
        kullaniciKolons = syKullaniciKolonService.findByKullaniciByTablo(kullaniciKolon.getSyKullanici(), tableName);
    }
}
