package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyKullaniciTuru;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStIslemLogTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 16:10
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StAlisService extends AbstractService<StAlis> {

    @Serial
    private static final long serialVersionUID = 6760274692488802540L;

    @Inject
    private StStokHareketService hareketService;
    @Inject
    private StIslemLogService stIslemLogService;
    @Inject
    private StDepoYetkiService stDepoYetkiService;
    @Inject
    private StDepoService stDepoService;

    public StAlisService() {
        super(StAlis.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StAlis alisOlustur(StAlis alis, SyKullanici kullanici) {
        validate(alis);
        if (alis.getTarih() == null) {
            alis.setTarih(LocalDateTime.now());
        }
        resolveRefs(alis);
        if (alis.getKalemler() == null) {
            alis.setKalemler(new java.util.ArrayList<>());
        }
        for (StAlisKalem k : alis.getKalemler()) {
            k.setStAlis(alis);
        }
        create(alis);
        flush();

        createStockMovements(alis, kullanici);

        stIslemLogService.log(EnumStIslemLogTuru.HAREKET, kullanici, alis.getStDepo(),
                "StAlis", alis.getBelgeNo(),
                "Alış girişi: " + alis.getBelgeTuru().getDisplayValue() + " / " + alis.getKalemler().size() + " kalem");

        return alis;
    }

    private void createStockMovements(StAlis alis, SyKullanici kullanici) {
        for (StAlisKalem k : alis.getKalemler()) {
            StStokHareket hareket = new StStokHareket();
            hareket.setStStok(k.getStStok());
            hareket.setStDepo(alis.getStDepo());
            hareket.setHareketTuru(EnumStHareketTuru.SATIN_ALMA);
            hareket.setYon(EnumGirisCikis.GIRIS);
            hareket.setMiktar(k.getMiktar());
            hareket.setBirimMaliyet(k.getBirimMaliyet());
            hareket.setBelgeTuru(alis.getBelgeTuru().getDisplayValue());
            hareket.setBelgeNo(alis.getBelgeNo());
            hareket.setIslemTarihi(alis.getTarih());
            hareket.setAciklama(k.getAciklama());
            hareket.setEkleyen(kullanici);
            hareketService.hareketEkle(hareket);
        }
    }

    /**
     * Hatali/eksik girilen alis belgesini gunceller. Eski stok hareketleri ters hareket
     * ile iptal edilir, yeni hareketler olusturulur (insert-only duzeltme).
     */
    public StAlis updatePurchase(StAlis form, SyKullanici kullanici) {
        validate(form);
        if (form.getId() == null) {
            throw new IllegalArgumentException("Guncellenecek alis belgesi secilmedi.");
        }
        StAlis managed = em.find(StAlis.class, form.getId());
        if (managed == null) {
            throw new IllegalArgumentException("Alis belgesi bulunamadi.");
        }

        String oldDocumentType = managed.getBelgeTuru() == null ? null : managed.getBelgeTuru().getDisplayValue();
        String oldDocumentNo = managed.getBelgeNo();
        for (StStokHareket old : hareketService.findActiveByPurchaseDocument(oldDocumentType, oldDocumentNo)) {
            hareketService.tersHareketOlustur(old);
        }

        managed.setBelgeTuru(form.getBelgeTuru());
        managed.setBelgeNo(form.getBelgeNo());
        managed.setTarih(form.getTarih() == null ? LocalDateTime.now() : form.getTarih());
        managed.setStDepo(em.find(StDepo.class, form.getStDepo().getId()));
        if (form.getStTedarikci() != null && form.getStTedarikci().getId() != null) {
            managed.setStTedarikci(em.find(StTedarikci.class, form.getStTedarikci().getId()));
        } else {
            managed.setStTedarikci(null);
        }
        managed.setAciklama(form.getAciklama());

        managed.getKalemler().clear();
        em.flush();
        for (StAlisKalem k : form.getKalemler()) {
            StAlisKalem yeni = new StAlisKalem();
            yeni.setStStok(em.find(StStok.class, k.getStStok().getId()));
            yeni.setMiktar(k.getMiktar());
            yeni.setBirimMaliyet(k.getBirimMaliyet());
            yeni.setAciklama(k.getAciklama());
            yeni.setStAlis(managed);
            managed.getKalemler().add(yeni);
        }
        em.flush();

        createStockMovements(managed, kullanici);
        stIslemLogService.log(EnumStIslemLogTuru.HAREKET, kullanici, managed.getStDepo(),
                "StAlis", managed.getBelgeNo(),
                "Alış güncelleme: " + managed.getBelgeTuru().getDisplayValue() + " / " + managed.getKalemler().size() + " kalem");
        return managed;
    }

    private void validate(StAlis alis) {
        if (alis == null) {
            throw new IllegalArgumentException("Alış bilgisi boş");
        }
        if (alis.getStDepo() == null) {
            throw new IllegalArgumentException("Alış için hedef depo seçin");
        }
        if (alis.getKalemler() == null || alis.getKalemler().isEmpty()) {
            throw new IllegalArgumentException("En az bir alış kalemi eklenmelidir.");
        }
        for (StAlisKalem k : alis.getKalemler()) {
            if (k.getStStok() == null) {
                throw new IllegalArgumentException("Alış kalemi için stok seçin");
            }
            if (k.getMiktar() == null || k.getMiktar().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Miktar sıfırdan büyük olmalıdır.");
            }
        }
    }

    private void resolveRefs(StAlis alis) {
        if (alis.getStDepo() != null && alis.getStDepo().getId() != null) {
            alis.setStDepo(em.find(StDepo.class, alis.getStDepo().getId()));
        }
        if (alis.getStTedarikci() != null && alis.getStTedarikci().getId() != null) {
            alis.setStTedarikci(em.find(StTedarikci.class, alis.getStTedarikci().getId()));
        }
        if (alis.getKalemler() != null) {
            for (StAlisKalem k : alis.getKalemler()) {
                if (k.getStStok() != null && k.getStStok().getId() != null) {
                    k.setStStok(em.find(StStok.class, k.getStStok().getId()));
                }
            }
        }
    }

    private Path<?> alisPath(Root<StAlis> root, String field, Map<String, Join<?, ?>> joins) {
        From<?, ?> from = root;
        String[] parts = field.split("\\.");
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            From<?, ?> current = from;
            from = (From<?, ?>) joins.computeIfAbsent(part, p -> current.join(p, JoinType.LEFT));
        }
        return from.get(parts[parts.length - 1]);
    }

    /**
     * Alis listesi yalnizca kullanicinin yetkili oldugu depolari gosterir.
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

    private Predicate yetkiliDepoPredicate(CriteriaBuilder cb, Root<StAlis> root) {
        List<Integer> depoIds = getYetkiliDepoIds();
        return depoIds.isEmpty() ? cb.disjunction() : root.get("stDepo").get("id").in(depoIds);
    }

    @Override
    public int count(Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<StAlis> root = cq.from(StAlis.class);
        Predicate where = cb.and(
                getFilterCondition(cb, root, filters == null ? Map.of() : filters, new HashMap<>()),
                yetkiliDepoPredicate(cb, root));
        cq.where(where).select(cb.count(root));
        return em.createQuery(cq).getSingleResult().intValue();
    }

    @Override
    public List<StAlis> loadLazy(int first, int pageSize, Map<String, SortMeta> sortMetaMap,
                                 Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StAlis> cq = cb.createQuery(StAlis.class);
        Root<StAlis> root = cq.from(StAlis.class);
        Map<String, Join<?, ?>> joins = new HashMap<>();
        cq.where(cb.and(
                getFilterCondition(cb, root, filters == null ? Map.of() : filters, joins),
                yetkiliDepoPredicate(cb, root)));

        List<Order> orders = new ArrayList<>();
        if (sortMetaMap != null && !sortMetaMap.isEmpty()) {
            for (SortMeta sortMeta : sortMetaMap.values()) {
                String field = sortMeta.getField();
                boolean desc = sortMeta.getOrder() == SortOrder.DESCENDING;
                if (field == null) {
                    continue;
                }
                Path<?> path = field.contains(".") ? alisPath(root, field, joins) : root.get(field);
                orders.add(desc ? cb.desc(path) : cb.asc(path));
            }
            cq.orderBy(orders);
        } else if (!StringUtil.isBlank(getSortCol())) {
            Path<?> path = getSortCol().contains(".") ? alisPath(root, getSortCol(), joins) : root.get(getSortCol());
            orders.add(StringUtil.isBlank(getSorting()) ? cb.asc(path) : cb.desc(path));
            cq.orderBy(orders);
        }
        return em.createQuery(cq).setFirstResult(first).setMaxResults(pageSize).getResultList();
    }
}
