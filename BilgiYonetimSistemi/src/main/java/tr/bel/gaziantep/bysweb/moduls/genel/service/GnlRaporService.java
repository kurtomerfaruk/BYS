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

            // Diğer kolonları ekle (aggregate değerleri)
            List<GnlRaporKolonDto> normalKolonlar = raporIstek.getKolonlar();
            //int offset = gruplamaKolonlari.size();

            for (int i = 0; i < normalKolonlar.size(); i++) {
                GnlRaporKolonDto kolon = normalKolonlar.get(i);
                //int dataIndex = offset + i;
                if (i < row.length) {
                    rowMap.put(kolon.getAlanAdi(), row[i].toString());
                }
            }

            reportData.add(rowMap);
        }

        return reportData;
    }

    private String buildGroupedJPQL(GnlRaporDto raporIstek) {
        StringBuilder jpql = new StringBuilder("SELECT ");

        if (raporIstek.getKolonlar() != null && !raporIstek.getKolonlar().isEmpty()) {
            List<GnlRaporKolonDto> normalKolonlar = raporIstek.getKolonlar();
            for (int i = 0; i < normalKolonlar.size(); i++) {
                GnlRaporKolonDto kolon = normalKolonlar.get(i);
                GnlRaporKolonDto group = raporIstek.getGruplamaKolonlari()
                        .stream().filter(x -> x.getAlanAdi().equals(kolon.getAlanAdi())).findFirst().orElse(null);
                if (group == null) {
                    jpql.append(kolon.getAlanAdi());
                    if (i < normalKolonlar.size() - 1) {
                        jpql.append(", ");
                    }
                }
            }
        }

        List<GnlRaporKolonDto> gruplamaKolonlari = raporIstek.getGruplamaKolonlari();
        assert raporIstek.getKolonlar() != null;
        for (int i = 0; i < gruplamaKolonlari.size(); i++) {
            GnlRaporKolonDto kolon = gruplamaKolonlari.get(i);

            jpql.append(kolon.getGrupTuru()).append("(").append(kolon.getAlanAdi()).append(")");
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
            jpql.append(" AND ").append(mainEntityCamel).append(".aktif=true ");
        } else {
            jpql.append(" WHERE ");
            jpql.append(mainEntityCamel).append(".aktif=true ");
        }


        if (raporIstek.getModul().getGnlRaporEntityBaglantiList() != null && !raporIstek.getModul().getGnlRaporEntityBaglantiList().isEmpty()) {
            for (int i = 0; i < raporIstek.getModul().getGnlRaporEntityBaglantiList().size(); i++) {
                GnlRaporEntityBaglanti connection = raporIstek.getModul().getGnlRaporEntityBaglantiList().get(i);
                String entityClass = connection.getEntityClass();
                String entityCamel = StringUtil.toCamelCase(entityClass);
                jpql.append(" AND ");
                jpql.append(entityCamel).append(".aktif=true ");
            }
        }

//        // WHERE koşulları
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
//            }
//        }

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
        List<String> groupColumns = raporIstek.getKolonlar().stream()
                .map(GnlRaporKolonDto::getAlanAdi)
                .filter(alanAdi -> raporIstek.getGruplamaKolonlari().stream()
                        .noneMatch(g -> g.getAlanAdi().equals(alanAdi)))
                .toList();

        jpql.append(" GROUP BY ").append(String.join(", ", groupColumns));


        jpql.append(" ORDER BY ").append(String.join(", ", groupColumns));

        return jpql.toString();
    }

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
            jpql.append(" AND ").append(mainEntityCamel).append(".aktif=true ");
        } else {
            jpql.append(" WHERE ");
            jpql.append(mainEntityCamel).append(".aktif=true ");
        }


        if (raporIstek.getModul().getGnlRaporEntityBaglantiList() != null && !raporIstek.getModul().getGnlRaporEntityBaglantiList().isEmpty()) {
            for (int i = 0; i < raporIstek.getModul().getGnlRaporEntityBaglantiList().size(); i++) {
                GnlRaporEntityBaglanti connection = raporIstek.getModul().getGnlRaporEntityBaglantiList().get(i);
                String entityClass = connection.getEntityClass();
                String entityCamel = StringUtil.toCamelCase(entityClass);
                jpql.append(" AND ");
                jpql.append(entityCamel).append(".aktif=true ");
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
}
