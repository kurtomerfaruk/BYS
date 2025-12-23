package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyVeriTipi;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.service.EnumService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.dtos.GnlRaporDto;
import tr.bel.gaziantep.bysweb.moduls.genel.dtos.GnlRaporKolonDto;
import tr.bel.gaziantep.bysweb.moduls.genel.dtos.GnlRaporParametreDegeriDto;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRapor;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporEntityBaglanti;

import java.io.Serial;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.12.2025 13:33
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GnlRaporService extends AbstractService<GnlRapor> {
    @Serial
    private static final long serialVersionUID = 5330594353232284355L;

    @Inject
    private GnlRaporKullaniciRaporSablonService gnlRaporKullaniciRaporSablonService;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private EnumService enumService;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GnlRaporService() {
        super(GnlRapor.class);
    }

//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public byte[] dinamikRaporOlustur(GnlRaporDto raporIstek) {
//        try {
//            // 1. Şablon kontrolü
//            if (raporIstek.getSablonId() != null) {
//                raporIstek = gnlRaporKullaniciRaporSablonService.sablonuRaporIstegineCevir(raporIstek);
//            }
//
//            // 2. Modül bilgilerini getir
//            if (raporIstek.getModul() == null) {
//                throw new RuntimeException("Modül bulunamadı: ");
//            }
//            // 3. Dinamik sorgu oluştur
//            String jpql = buildDynamicJPQL(raporIstek);
//            Query query = em.createQuery(jpql);
//
//            // 4. Parametreleri bind et
//            bindParameters(query, raporIstek.getParametreler());
//
//            // 5. Sorguyu çalıştır
//            List<Object[]> results = query.getResultList();
//
//            // 6. Sonuçları Map listesine çevir
//            List<Map<String, Object>> reportData = convertResultsToMap(results, raporIstek.getKolonlar());
//
//            // 7. JasperReport ile rapor oluştur
//            return jasperReportService.generateReport(reportData, raporIstek);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Rapor oluşturma hatası: " + e.getMessage(), e);
//        }
//    }
//
//
//    private String buildDynamicJPQL(GnlRaporDto raporIstek) {
//        StringBuilder jpql = new StringBuilder("SELECT ");
//
//        // SELECT kısmı
//        List<GnlRaporKolonDto> kolonlar = raporIstek.getKolonlar();
//        for (int i = 0; i < kolonlar.size(); i++) {
//            GnlRaporKolonDto kolon = kolonlar.get(i);
//            jpql.append(kolon.getAlanAdi());
//            if (i < kolonlar.size() - 1) {
//                jpql.append(", ");
//            }
//        }
//
//        String mainEntityCamel = StringUtil.toCamelCase(raporIstek.getModul().getAnaEntity());
//        jpql.append(" FROM ").append(raporIstek.getModul().getAnaEntity()).append(" ").append(mainEntityCamel);
//
//        if (raporIstek.getModul().getGnlRaporEntityBaglantiList() != null && !raporIstek.getModul().getGnlRaporEntityBaglantiList().isEmpty()) {
//            for (GnlRaporEntityBaglanti connection : raporIstek.getModul().getGnlRaporEntityBaglantiList()) {
//                String entityClass = connection.getEntityClass();
//                String entityCamel = StringUtil.toCamelCase(entityClass);
//                jpql.append(" ").append(connection.getJoinTipi())
//                        .append(" JOIN ").append(entityClass)
//                        .append(" ").append(entityCamel)
//                        .append(" ON ").append(connection.getJoinKosulu());
//            }
//        }
//
////        jpql.append(" FROM ").append(raporIstek.getModul().getAnaEntity()).append(" e");
////
////        if (raporIstek.getModul().getGnlRaporEntityBaglantiList() != null && !raporIstek.getModul().getGnlRaporEntityBaglantiList().isEmpty()) {
////            for (GnlRaporEntityBaglanti baglanti : raporIstek.getModul().getGnlRaporEntityBaglantiList()) {
////                jpql.append(" ").append(baglanti.getJoinTipi())
////                        .append(" JOIN ").append(baglanti.getEntityClass())
////                        .append(" ").append(getAlias(baglanti.getEntityClass()))
////                        .append(" ON ").append(baglanti.getJoinKosulu());
////            }
////        }
//
//        List<GnlRaporParametreDegeriDto> parametreler = raporIstek.getParametreler();
//        if (parametreler != null && !parametreler.isEmpty()) {
//            jpql.append(" WHERE ");
//            for (int i = 0; i < parametreler.size(); i++) {
//                GnlRaporParametreDegeriDto param = parametreler.get(i);
//                if (param.getDeger() != null && !param.getDeger().isEmpty()) {
//                    jpql.append(buildWhereCondition(param, i));
//                    if (i < parametreler.size() - 1) {
//                        jpql.append(" AND ");
//                    }
//                }
////                if (StringUtil.isNotBlank(param.getSqlKosul())) {
////                    jpql.append(param.getSqlKosul());
////                }
//            }
//        }
//
//        if(raporIstek.getOzelFiltreler()!=null && !raporIstek.getOzelFiltreler().isEmpty()) {
//            boolean hasAnd = parametreler != null && !parametreler.isEmpty();
//            if(hasAnd) jpql.append(" AND ");
//            for (int i = 0; i < raporIstek.getOzelFiltreler().size(); i++) {
//                String ozelFiltre = raporIstek.getOzelFiltreler().get(i);
//                if(StringUtil.isNotBlank(ozelFiltre)){
//                    jpql.append(ozelFiltre);
//                    if (i < raporIstek.getOzelFiltreler().size() - 1) {
//                        jpql.append(" AND ");
//                    }
//                }
//            }
//        }
//
//        return jpql.toString();
//
////        StringBuilder jpql = new StringBuilder("SELECT ");
////
////        // 1. SELECT kısmı - Direkt entity.field formatında
////        List<GnlRaporKolonDto> kolonlar = raporIstek.getKolonlar();
////        for (int i = 0; i < kolonlar.size(); i++) {
////            jpql.append(kolonlar.get(i).getAlanAdi()); // moralEvleri.evAdi
////            if (i < kolonlar.size() - 1) {
////                jpql.append(", ");
////            }
////        }
////
////        // 2. FROM kısmı - Ana entity camelCase
////        String anaEntityCamel = StringUtil.toCamelCase(raporIstek.getModul().getAnaEntity());
////        jpql.append(" FROM ").append(raporIstek.getModul().getAnaEntity()).append(" ").append(anaEntityCamel);
////
////        // 3. JOIN'ler - CamelCase entity isimleriyle
////        if (raporIstek.getModul().getGnlRaporEntityBaglantiList() != null) {
////            for (GnlRaporEntityBaglanti baglanti : raporIstek.getModul().getGnlRaporEntityBaglantiList()) {
////                String entityClass = baglanti.getEntityClass();
////                String entityCamel = StringUtil.toCamelCase(entityClass);
////
////                jpql.append(" ").append(baglanti.getJoinTipi())
////                        .append(" JOIN ").append(entityClass)
////                        .append(" ").append(entityCamel)
////                        .append(" ON ").append(baglanti.getJoinKosulu()); // kisiler.id = kisiMoralEvi.kisiId
////            }
////        }
////
////        // 4. WHERE kısmı
////        String whereClause = buildWhereClause(raporIstek.getParametreler());
////        if (!whereClause.isEmpty()) {
////            jpql.append(" WHERE ").append(whereClause);
////        }
////
////        return jpql.toString();
//    }

//    private String buildWhereClause(List<GnlRaporParametreDegeriDto> parametreler) {
//        if (parametreler == null || parametreler.isEmpty()) {
//            return "";
//        }
//
//        List<String> conditions = new ArrayList<>();
//
//        for (int i = 0; i < parametreler.size(); i++) {
//            GnlRaporParametreDegeriDto param = parametreler.get(i);
//            if (param.getDeger() != null && !param.getDeger().isEmpty()) {
//                // Parametre adına göre field belirle
//                String field = param.getParametreAdi();
//                String operator = param.getOperator() != null ? param.getOperator() : "=";
//
//                String condition;
//                if ("BETWEEN".equalsIgnoreCase(operator)) {
//                    condition = String.format("%s BETWEEN :param%d_start AND :param%d_end",
//                            field, i, i);
//                } else if ("IN".equalsIgnoreCase(operator)) {
//                    condition = String.format("%s IN (:%s)", field, param.getParametreAdi());
//                } else {
//                    condition = String.format("%s %s :param%d", field, operator, i);
//                }
//
//                conditions.add(condition);
//            }
//        }
//
//        return String.join(" AND ", conditions);
//    }

//    private String buildWhereCondition(GnlRaporParametreDegeriDto param, int index) {
//        String field = param.getParametreAdi();
//        String operator = param.getOperator() != null ? param.getOperator() : "=";
//
//        if ("BETWEEN".equalsIgnoreCase(operator)) {
//            return field + " BETWEEN :param" + index + "_start AND :param" + index + "_end";
//        } else if ("LIKE".equalsIgnoreCase(operator)) {
//            return field + " LIKE :param" + index;
//        } else if ("IN".equalsIgnoreCase(operator)) {
//            return field + " IN (:" + param.getParametreAdi() + ")";
//        } else {
//            return field + " " + operator + " :param" + index;
//        }
//    }

//    private String getAlias(String entityClass) {
//        String[] parts = entityClass.split("\\.");
//        String className = parts[parts.length - 1];
//        return className.substring(0, 1).toLowerCase();
//    }

//    private void bindParameters(Query query, List<GnlRaporParametreDegeriDto> parametreler) {
//        if (parametreler == null) return;
//
//        int paramIndex = 0;
//        for (GnlRaporParametreDegeriDto param : parametreler) {
//            if (param.getDeger() != null && !param.getDeger().isEmpty()) {
//                Object convertedValue = convertParameterValue(
//                        param.getDeger(),
//                        param.getVeriTipi(),
//                        param.getLookupEnumClass()
//                );
//
//                if ("IN".equalsIgnoreCase(param.getOperator()) && convertedValue instanceof List) {
//                    query.setParameter("param" + paramIndex, (List<?>) convertedValue);
//                } else {
//                    query.setParameter("param" + paramIndex, convertedValue);
//                }
//
//                paramIndex++;
//            }
//        }
//    }

//    private void bindParameters(Query query, List<GnlRaporParametreDegeriDto> parametreler) {
//        if (parametreler == null) return;
//
//        int paramIndex = 0;
//        for (GnlRaporParametreDegeriDto param : parametreler) {
//            if (param.getDeger() != null && !param.getDeger().isEmpty()) {
//                String operator = param.getOperator() != null ? param.getOperator() : "=";
//
//                if ("BETWEEN".equalsIgnoreCase(operator)) {
//                    query.setParameter("param" + paramIndex + "_start", convertParameterValue(param.getDeger(), param.getVeriTipi()));
//                    query.setParameter("param" + paramIndex + "_end", convertParameterValue(param.getIkinciDeger(), param.getVeriTipi()));
//                } else if ("IN".equalsIgnoreCase(operator)) {
//                    List<?> values = Arrays.asList(param.getDeger().split(","));
//                    query.setParameter(param.getParametreAdi(), values);
//                } else if ("LIKE".equalsIgnoreCase(operator)) {
//                    query.setParameter("param" + paramIndex, "%" + param.getDeger() + "%");
//                } else {
//                    query.setParameter("param" + paramIndex, convertParameterValue(param.getDeger(), param.getVeriTipi()));
//                }
//
//                paramIndex++;
//            }
//        }
//    }

//    private Object convertParameterValue(String value, EnumSyVeriTipi type, String lookupEnumClass) {
//        if (value == null || value.trim().isEmpty()) {
//            return null;
//        }
//
//        try {
//            switch (type) {
//                case ENUM:
//                    if (lookupEnumClass != null && !lookupEnumClass.isEmpty()) {
//                        return enumService.convertToEnum(lookupEnumClass, value);
//                    }
//                    return value;
//
//                case MULTI_ENUM:
//                    if (lookupEnumClass != null && !lookupEnumClass.isEmpty()) {
//                        List<Object> enumList = new ArrayList<>();
//                        String[] values = value.split(",");
//                        for (String val : values) {
//                            enumList.add(enumService.convertToEnum(lookupEnumClass, val.trim()));
//                        }
//                        return enumList;
//                    }
//                    return Arrays.asList(value.split(","));
//                case INTEGER:
//                    return Integer.parseInt(value);
//                case LONG:
//                    return Long.parseLong(value);
//                case DOUBLE:
//                    return Double.parseDouble(value);
//                case BOOLEAN:
//                    return Boolean.parseBoolean(value);
//                case DATE:
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
//                    return sdf.parse(value);
//                case DATE_TIME:
//                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//                    return sdf2.parse(value);
//                default:
//                    return value;
//            }
//        } catch (Exception e) {
//            System.err.println("Parametre dönüşüm hatası: " + e.getMessage());
//            return value;
//        }
//    }
//
//    private List<Map<String, Object>> convertResultsToMap(List<?> results, List<GnlRaporKolonDto> kolonlar) {
//        List<Map<String, Object>> mapList = new ArrayList<>();
//
//        if (results == null || results.isEmpty() || kolonlar == null || kolonlar.isEmpty()) {
//            return mapList;
//        }
//
//        try {
//            boolean isSingleColumn = kolonlar.size() == 1;
//
//            for (Object resultRow : results) {
//                Map<String, Object> rowMap = new HashMap<>();
//
//                if (isSingleColumn) {
//                    GnlRaporKolonDto kolon = kolonlar.get(0);
//                    rowMap.put(kolon.getAlanAdi(), resultRow);
//                } else {
//                    Object[] rowArray;
//
//                    if (resultRow instanceof Object[]) {
//                        rowArray = (Object[]) resultRow;
//                    } else if (resultRow instanceof List) {
//                        rowArray = ((List<?>) resultRow).toArray();
//                    } else {
//                        continue;
//                    }
//
//                    int columnCount = Math.min(kolonlar.size(), rowArray.length);
//                    for (int i = 0; i < columnCount; i++) {
//                        GnlRaporKolonDto kolon = kolonlar.get(i);
//                        rowMap.put(kolon.getAlanAdi(), rowArray[i]);
//                    }
//                }
//
//                mapList.add(rowMap);
//            }
//        } catch (Exception e) {
//            System.err.println("Result conversion error: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return mapList;
//    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public byte[] dinamikRaporOlustur(GnlRaporDto raporIstek) {
        try {
            // 1. Şablon kontrolü
            if (raporIstek.getSablonId() != null) {
                raporIstek = gnlRaporKullaniciRaporSablonService.sablonuRaporIstegineCevir(raporIstek);
            }

            // 2. Modül bilgilerini getir
            if (raporIstek.getModul() == null) {
                throw new RuntimeException("Modül bulunamadı: ");
            }

            // 3. Gruplama varsa özel işlem
            List<Map<String, Object>> reportData;
            if (raporIstek.isGruplamaYapilsin()
                    && raporIstek.getGruplamaKolonlari() != null
                    && !raporIstek.getGruplamaKolonlari().isEmpty()) {

                // Gruplama için verileri al
                reportData = getRegularGroupedData(raporIstek);
            } else {
                // Normal sorgu ile verileri al
                reportData = getRegularReportData(raporIstek);
            }

            // 7. JasperReport ile rapor oluştur
            return jasperReportService.generateReport(reportData, raporIstek);

        } catch (Exception e) {
            throw new RuntimeException("Rapor oluşturma hatası: " + e.getMessage(), e);
        }
    }

    /**
     * Gruplama yapılacak rapor verilerini getir
     */
//    private List<Map<String, Object>> getGroupedReportData(GnlRaporDto raporIstek) {
//        try {
//            List<Map<String, Object>> groupedData = getRegularGroupedData(raporIstek);
//
//            return groupedData;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Gruplu rapor verisi alınamadı: " + e.getMessage(), e);
//        }
//    }

    /**
     * Gruplanmış verileri JPQL ile al
     */
    private List<Map<String, Object>> getRegularGroupedData(GnlRaporDto raporIstek) {
        // 1. Gruplama JPQL'sini oluştur
        String jpql = buildGroupedJPQL(raporIstek);
        Query query = em.createQuery(jpql);

        // 2. Parametreleri bind et
        bindParameters(query, raporIstek.getParametreler());

        // 3. Sorguyu çalıştır
        List<Object[]> results = query.getResultList();

        // 4. Sonuçları Map listesine çevir
        List<Map<String, Object>> reportData = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> rowMap = new HashMap<>();

            // Gruplama kolonlarını ekle
            List<GnlRaporKolonDto> gruplamaKolonlari = raporIstek.getGruplamaKolonlari();
            for (int i = 0; i < gruplamaKolonlari.size(); i++) {
                GnlRaporKolonDto kolon = gruplamaKolonlari.get(i);
                rowMap.put("GROUP_" + kolon.getAlanAdi(), row[i]);
            }

            // Diğer kolonları ekle (aggregate değerleri)
            List<GnlRaporKolonDto> normalKolonlar = raporIstek.getKolonlar();
            int offset = gruplamaKolonlari.size();

            for (int i = 0; i < normalKolonlar.size(); i++) {
                GnlRaporKolonDto kolon = normalKolonlar.get(i);
                int dataIndex = offset + i;
                if (dataIndex < row.length) {
                    rowMap.put(kolon.getAlanAdi(), row[dataIndex].toString());
                }
            }

            reportData.add(rowMap);
        }

        return reportData;
    }

    /**
     * Gruplanmış JPQL sorgusunu oluştur
     */
    private String buildGroupedJPQL(GnlRaporDto raporIstek) {
        StringBuilder jpql = new StringBuilder("SELECT ");

        // Gruplama kolonlarını ekle


        // Diğer kolonları aggregate fonksiyonları ile ekle
        if (raporIstek.getKolonlar() != null && !raporIstek.getKolonlar().isEmpty()) {


            List<GnlRaporKolonDto> normalKolonlar = raporIstek.getKolonlar();
            for (int i = 0; i < normalKolonlar.size(); i++) {
                GnlRaporKolonDto kolon = normalKolonlar.get(i);
                jpql.append(kolon.getAlanAdi());
                // Veri tipine göre aggregate fonksiyonu seç
//                String aggregateFunction = getAggregateFunctionForColumn(kolon);
//                jpql.append(aggregateFunction).append("(").append(kolon.getAlanAdi()).append(")");

                if (i < normalKolonlar.size() - 1) {
                    jpql.append(", ");
                }
            }
        }

        List<GnlRaporKolonDto> gruplamaKolonlari = raporIstek.getGruplamaKolonlari();
        if (raporIstek.getKolonlar().size() > 0) {
            jpql.append(", ");
        }
        for (int i = 0; i < gruplamaKolonlari.size(); i++) {
            GnlRaporKolonDto kolon = gruplamaKolonlari.get(i);
            jpql.append(kolon.getAlanAdi());
            if (i < gruplamaKolonlari.size() - 1) {
                jpql.append(", ");
            }
        }

        // FROM kısmı
        String mainEntityCamel = StringUtil.toCamelCase(raporIstek.getModul().getAnaEntity());
        jpql.append(" FROM ").append(raporIstek.getModul().getAnaEntity()).append(" ").append(mainEntityCamel);

        // JOIN'ler
        if (raporIstek.getModul().getGnlRaporEntityBaglantiList() != null &&
                !raporIstek.getModul().getGnlRaporEntityBaglantiList().isEmpty()) {
            for (GnlRaporEntityBaglanti connection : raporIstek.getModul().getGnlRaporEntityBaglantiList()) {
                String entityClass = connection.getEntityClass();
                String entityCamel = StringUtil.toCamelCase(entityClass);
                jpql.append(" ").append(connection.getJoinTipi())
                        .append(" JOIN ").append(entityClass)
                        .append(" ").append(entityCamel)
                        .append(" ON ").append(connection.getJoinKosulu());
            }
        }

        // WHERE koşulları
        List<GnlRaporParametreDegeriDto> parametreler = raporIstek.getParametreler();
        if (parametreler != null && !parametreler.isEmpty()) {
            jpql.append(" WHERE ");
            for (int i = 0; i < parametreler.size(); i++) {
                GnlRaporParametreDegeriDto param = parametreler.get(i);
                if (param.getDeger() != null && !param.getDeger().isEmpty()) {
                    jpql.append(buildWhereCondition(param, i));
                    if (i < parametreler.size() - 1) {
                        jpql.append(" AND ");
                    }
                }
            }
        }

        // Özel filtreler
        if (raporIstek.getOzelFiltreler() != null && !raporIstek.getOzelFiltreler().isEmpty()) {
            boolean hasAnd = parametreler != null && !parametreler.isEmpty();
            if (hasAnd) jpql.append(" AND ");
            for (int i = 0; i < raporIstek.getOzelFiltreler().size(); i++) {
                String ozelFiltre = raporIstek.getOzelFiltreler().get(i);
                if (StringUtil.isNotBlank(ozelFiltre)) {
                    jpql.append(ozelFiltre);
                    if (i < raporIstek.getOzelFiltreler().size() - 1) {
                        jpql.append(" AND ");
                    }
                }
            }
        }

        // GROUP BY
        jpql.append(" GROUP BY ");
        for (int i = 0; i < gruplamaKolonlari.size(); i++) {
            GnlRaporKolonDto kolon = gruplamaKolonlari.get(i);
            jpql.append(kolon.getAlanAdi());
            if (i < gruplamaKolonlari.size() - 1) {
                jpql.append(", ");
            }
        }

        // ORDER BY (gruplama sırasına göre)
        jpql.append(" ORDER BY ");
        for (int i = 0; i < gruplamaKolonlari.size(); i++) {
            GnlRaporKolonDto kolon = gruplamaKolonlari.get(i);
            jpql.append(kolon.getAlanAdi());
            if (i < gruplamaKolonlari.size() - 1) {
                jpql.append(", ");
            }
        }

        return jpql.toString();
    }

    /**
     * Kolon veri tipine göre aggregate fonksiyonu belirle
     */
    private String getAggregateFunctionForColumn(GnlRaporKolonDto kolon) {
        if (kolon.getVeriTipi() == null) {
            return "COUNT";
        }

        switch (kolon.getVeriTipi()) {
            case INTEGER:
            case LONG:
            case DOUBLE:
            case DECIMAL:
                return "SUM"; // Sayısal tipler için SUM
            case DATE:
            case DATE_TIME:
            case STRING:
            case BOOLEAN:
            case ENUM:
            default:
                return "COUNT"; // Diğer tipler için COUNT
        }
    }

    /**
     * Grup toplamlarını hesapla
     */
    private void calculateGroupTotals(List<Map<String, Object>> groupedData, GnlRaporDto raporIstek) {
        if (groupedData == null || groupedData.isEmpty()) {
            return;
        }

        // Toplamları hesapla
        Map<String, Object> totals = new HashMap<>();
        List<GnlRaporKolonDto> normalKolonlar = raporIstek.getKolonlar();

        for (Map<String, Object> row : groupedData) {
            for (GnlRaporKolonDto kolon : normalKolonlar) {
                String columnName = kolon.getAlanAdi();
                Object value = row.get(columnName);

                if (value != null) {
                    // Veri tipine göre toplam hesapla
                    switch (kolon.getVeriTipi()) {
                        case INTEGER:
                        case LONG:
                        case DOUBLE:
                            Double currentTotal = (Double) totals.getOrDefault(columnName, 0.0);
                            Double rowValue = ((Number) value).doubleValue();
                            totals.put(columnName, currentTotal + rowValue);
                            break;
                        default:
                            // Sayısal olmayanlar için COUNT
                            Integer count = (Integer) totals.getOrDefault(columnName, 0);
                            totals.put(columnName, count + 1);
                            break;
                    }
                }
            }
        }

        // Toplamları her satıra ekle
        for (Map<String, Object> row : groupedData) {
            row.put("_GROUP_TOTALS", totals);

            // Ayrıca her kolon için grup toplamını da ekleyebilirsiniz
            for (Map.Entry<String, Object> entry : totals.entrySet()) {
                row.put("TOTAL_" + entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Normal (gruplanmamış) rapor verilerini getir
     */
    private List<Map<String, Object>> getRegularReportData(GnlRaporDto raporIstek) {
        // 1. Dinamik sorgu oluştur
        String jpql = buildDynamicJPQL(raporIstek);
        Query query = em.createQuery(jpql);

        // 2. Parametreleri bind et
        bindParameters(query, raporIstek.getParametreler());

        // 3. Sorguyu çalıştır
        List<Object[]> results = query.getResultList();

        // 4. Sonuçları Map listesine çevir
        return convertResultsToMap(results, raporIstek.getKolonlar());
    }

    private String buildDynamicJPQL(GnlRaporDto raporIstek) {
        StringBuilder jpql = new StringBuilder("SELECT ");

        // SELECT kısmı
        List<GnlRaporKolonDto> kolonlar = raporIstek.getKolonlar();
        for (int i = 0; i < kolonlar.size(); i++) {
            GnlRaporKolonDto kolon = kolonlar.get(i);
            jpql.append(kolon.getAlanAdi());
            if (i < kolonlar.size() - 1) {
                jpql.append(", ");
            }
        }

        String mainEntityCamel = StringUtil.toCamelCase(raporIstek.getModul().getAnaEntity());
        jpql.append(" FROM ").append(raporIstek.getModul().getAnaEntity()).append(" ").append(mainEntityCamel);

        if (raporIstek.getModul().getGnlRaporEntityBaglantiList() != null && !raporIstek.getModul().getGnlRaporEntityBaglantiList().isEmpty()) {
            for (GnlRaporEntityBaglanti connection : raporIstek.getModul().getGnlRaporEntityBaglantiList()) {
                String entityClass = connection.getEntityClass();
                String entityCamel = StringUtil.toCamelCase(entityClass);
                jpql.append(" ").append(connection.getJoinTipi())
                        .append(" JOIN ").append(entityClass)
                        .append(" ").append(entityCamel)
                        .append(" ON ").append(connection.getJoinKosulu());
            }
        }


        List<GnlRaporParametreDegeriDto> parametreler = raporIstek.getParametreler();
        if (parametreler != null && !parametreler.isEmpty()) {
            jpql.append(" WHERE ");
            for (int i = 0; i < parametreler.size(); i++) {
                GnlRaporParametreDegeriDto param = parametreler.get(i);
                if (param.getDeger() != null && !param.getDeger().isEmpty()) {
                    jpql.append(buildWhereCondition(param, i));
                    if (i < parametreler.size() - 1) {
                        jpql.append(" AND ");
                    }
                }
            }
        }

        if (raporIstek.getOzelFiltreler() != null && !raporIstek.getOzelFiltreler().isEmpty()) {
            boolean hasAnd = parametreler != null && !parametreler.isEmpty();
            if (hasAnd) jpql.append(" AND ");
            for (int i = 0; i < raporIstek.getOzelFiltreler().size(); i++) {
                String ozelFiltre = raporIstek.getOzelFiltreler().get(i);
                if (StringUtil.isNotBlank(ozelFiltre)) {
                    jpql.append(ozelFiltre);
                    if (i < raporIstek.getOzelFiltreler().size() - 1) {
                        jpql.append(" AND ");
                    }
                }
            }
        }

        return jpql.toString();
    }


    private String buildWhereCondition(GnlRaporParametreDegeriDto param, int index) {
        String field = param.getParametreAdi();
        String operator = param.getOperator() != null ? param.getOperator() : "=";

        if ("BETWEEN".equalsIgnoreCase(operator)) {
            return field + " BETWEEN :param" + index + "_start AND :param" + index + "_end";
        } else if ("LIKE".equalsIgnoreCase(operator)) {
            return field + " LIKE :param" + index;
        } else if ("IN".equalsIgnoreCase(operator)) {
            return field + " IN (:" + param.getParametreAdi() + ")";
        } else {
            return field + " " + operator + " :param" + index;
        }
    }


    private void bindParameters(Query query, List<GnlRaporParametreDegeriDto> parametreler) {
        if (parametreler == null) return;

        int paramIndex = 0;
        for (GnlRaporParametreDegeriDto param : parametreler) {
            if (param.getDeger() != null && !param.getDeger().isEmpty()) {
                Object convertedValue = convertParameterValue(
                        param.getDeger(),
                        param.getVeriTipi(),
                        param.getLookupEnumClass()
                );

                if ("IN".equalsIgnoreCase(param.getOperator()) && convertedValue instanceof List) {
                    query.setParameter("param" + paramIndex, (List<?>) convertedValue);
                } else {
                    query.setParameter("param" + paramIndex, convertedValue);
                }

                paramIndex++;
            }
        }
    }

    private Object convertParameterValue(String value, EnumSyVeriTipi type, String lookupEnumClass) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            switch (type) {
                case ENUM:
                    if (lookupEnumClass != null && !lookupEnumClass.isEmpty()) {
                        return enumService.convertToEnum(lookupEnumClass, value);
                    }
                    return value;

                case MULTI_ENUM:
                    if (lookupEnumClass != null && !lookupEnumClass.isEmpty()) {
                        List<Object> enumList = new ArrayList<>();
                        String[] values = value.split(",");
                        for (String val : values) {
                            enumList.add(enumService.convertToEnum(lookupEnumClass, val.trim()));
                        }
                        return enumList;
                    }
                    return Arrays.asList(value.split(","));
                case INTEGER:
                    return Integer.parseInt(value);
                case LONG:
                    return Long.parseLong(value);
                case DOUBLE:
                    return Double.parseDouble(value);
                case BOOLEAN:
                    return Boolean.parseBoolean(value);
                case DATE:
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    return sdf.parse(value);
                case DATE_TIME:
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    return sdf2.parse(value);
                default:
                    return value;
            }
        } catch (Exception e) {
            System.err.println("Parametre dönüşüm hatası: " + e.getMessage());
            return value;
        }
    }

    private List<Map<String, Object>> convertResultsToMap(List<?> results, List<GnlRaporKolonDto> kolonlar) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        if (results == null || results.isEmpty() || kolonlar == null || kolonlar.isEmpty()) {
            return mapList;
        }

        try {
            boolean isSingleColumn = kolonlar.size() == 1;

            for (Object resultRow : results) {
                Map<String, Object> rowMap = new HashMap<>();

                if (isSingleColumn) {
                    GnlRaporKolonDto kolon = kolonlar.get(0);
                    rowMap.put(kolon.getAlanAdi(), resultRow);
                } else {
                    Object[] rowArray;

                    if (resultRow instanceof Object[]) {
                        rowArray = (Object[]) resultRow;
                    } else if (resultRow instanceof List) {
                        rowArray = ((List<?>) resultRow).toArray();
                    } else {
                        continue;
                    }

                    int columnCount = Math.min(kolonlar.size(), rowArray.length);
                    for (int i = 0; i < columnCount; i++) {
                        GnlRaporKolonDto kolon = kolonlar.get(i);
                        rowMap.put(kolon.getAlanAdi(), rowArray[i]);
                    }
                }

                mapList.add(rowMap);
            }
        } catch (Exception e) {
            System.err.println("Result conversion error: " + e.getMessage());
            e.printStackTrace();
        }

        return mapList;
    }

    /**
     * Gruplama için ek yardımcı metodlar
     */
    public List<Map<String, Object>> getDistinctValuesForGrouping(GnlRaporDto raporIstek, String columnName) {
        try {
            String jpql = "SELECT DISTINCT " + columnName +
                    " FROM " + raporIstek.getModul().getAnaEntity() +
                    " ORDER BY " + columnName;

            Query query = em.createQuery(jpql);
            List<?> results = query.getResultList();

            List<Map<String, Object>> distinctValues = new ArrayList<>();
            for (Object value : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("value", value);
                map.put("label", value != null ? value.toString() : "");
                distinctValues.add(map);
            }

            return distinctValues;
        } catch (Exception e) {
            throw new RuntimeException("Gruplama değerleri alınamadı: " + e.getMessage(), e);
        }
    }
}
