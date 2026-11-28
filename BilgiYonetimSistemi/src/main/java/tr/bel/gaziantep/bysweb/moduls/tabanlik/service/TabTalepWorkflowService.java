package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumEvetHayir;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.*;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyYetkiService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StDepoYetkiService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokHareketService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:34
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TabTalepWorkflowService implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 751374689667839785L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Inject
    private SyYetkiService syYetkiService;
    @Inject
    private TabRandevuService randevuService;
    @Inject
    private TabOlcumService olcumService;
    @Inject
    private TabUretimKararService kararService;
    @Inject
    private TabOdemeService odemeService;
    @Inject
    private TabBelgeService belgeService;
    @Inject
    private TabBelgeZorunlulukService belgeZorunlulukService;
    @Inject
    private TabUretimService uretimService;
    @Inject
    private TabTeslimatService teslimatService;
    @Inject
    private StStokHareketService stStokHareketService;
    @Inject
    private StDepoYetkiService stDepoYetkiService;
    @Inject
    private TabTalepHareketService hareketService;

    /**
     * Randevu kaydı + talep durum geçişi tek transaction'da.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createAppointmentAndTransition(Integer talepId, EnumTabRandevuTuru tur,
                                               LocalDateTime randevuTarihi, String aciklama) {
        if (talepId == null || tur == null || randevuTarihi == null) {
            throw new IllegalArgumentException("Randevu için talep, tür ve tarih zorunludur.");
        }
        TabTalep talep = em.find(TabTalep.class, talepId);
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }

        EnumTabTalepIslem action = resolveAppointmentAction(tur, talep.getDurum());
        checkPermission(action);
        checkTransition(talep, action);
        checkGuards(talep, action, tur);

        SyKullanici kullanici = Util.getSyKullanici();
        TabRandevu randevu = TabRandevu.builder()
                .tabTalep(talep)
                .tabKisi(talep.getTabKisi())
                .randevuTuru(tur)
                .randevuTarihi(randevuTarihi)
                .durum(EnumTabRandevuDurumu.PLANLANDI)
                .olusturan(kullanici)
                .aciklama(aciklama)
                .build();
        em.persist(randevu);
        em.flush();

        doTransition(talep, action, aciklama == null || aciklama.isBlank()
                ? tur.getDisplayValue() + " randevusu oluşturuldu."
                : aciklama);
    }

    /**
     * Genel durum geçişi. Yetki, geçiş kuralı ve iş kuralı kontrollerini yapar.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void applyTransition(Integer talepId, EnumTabTalepIslem action, String description) {
        if (talepId == null || action == null) {
            throw new IllegalArgumentException("Geçersiz talep işlemi.");
        }
        TabTalep talep = em.find(TabTalep.class, talepId);
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }
        checkPermission(action);
        checkTransition(talep, action);
        checkActionGuards(talep, action);
        doTransition(talep, action, description);
    }

    /**
     * Yetkili düzeltme: önceki bir aşamaya geri döner. Gerekçe zorunludur ve
     * her geri dönüş işlem geçmişine kaydedilir.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void goBack(Integer talepId, EnumTabTalepDurumu hedefDurum, String aciklama) {
        if (talepId == null || hedefDurum == null) {
            throw new IllegalArgumentException("Geri dönüş için hedef durum belirtilmelidir.");
        }
        if (StringUtil.isBlank(aciklama)) {
            throw new IllegalArgumentException("Geri dönüş gerekçesi zorunludur.");
        }
        TabTalep talep = em.find(TabTalep.class, talepId);
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }
        if (!syYetkiService.hasPermission(Util.getSyKullanici(), "tabtalep:guncelle")) {
            throw new IllegalArgumentException("Geri dönüş yetkiniz bulunmuyor.");
        }
        if (!talep.getDurum().getPreviousStatuses().contains(hedefDurum)) {
            throw new IllegalArgumentException("Bu durumdan hedef adıma geri dönülemez.");
        }
        em.lock(talep, LockModeType.OPTIMISTIC);
        EnumTabTalepDurumu eskiDurum = talep.getDurum();
        talep.setDurum(hedefDurum);
        em.merge(talep);
        em.flush();
        hareketService.add(talep, eskiDurum, hedefDurum, "GO_BACK", "Geri dönüş: " + aciklama);
        em.flush();
    }


    /**
     * Ölçüm kaydı (sağ/sol detaylarla) + talep durum geçişi tek transaction'da.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createOlcumAndTransition(TabOlcum olcum) {
        if (olcum == null || olcum.getTabTalep() == null || olcum.getTabTalep().getId() == null) {
            throw new IllegalArgumentException("Ölçüm için talep seçilmelidir.");
        }
        TabTalep talep = em.find(TabTalep.class, olcum.getTabTalep().getId());
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }
        EnumTabTalepIslem action = EnumTabTalepIslem.OLCUM_GIR;
        checkPermission(action);
        checkTransition(talep, action);
        checkActionGuards(talep, action);

        if (!randevuService.existsAttended(talep, EnumTabRandevuTuru.OLCU)) {
            throw new IllegalArgumentException("Ölçüm randevusu gerçekleşmeden ölçüm girilemez.");
        }
        if (olcum.getDetayList() == null || olcum.getDetayList().size() < 2) {
            throw new IllegalArgumentException("Sağ ve sol ayak ölçüleri girilmelidir.");
        }

        olcum.setTabTalep(talep);
        olcum.setOlcumYapan(Util.getSyKullanici());
        if (olcum.getOlcumTarihi() == null) {
            olcum.setOlcumTarihi(LocalDateTime.now());
        }
        olcum.getDetayList().forEach(detay -> detay.setTabOlcum(olcum));
        em.persist(olcum);
        em.flush();

        doTransition(talep, action, olcum.getAciklama());
    }

    /**
     * Üretim kararı: EVET → ücretlendirme, HAYIR → talep kapatılır.
     * Karar kaydı, kapatma bilgileri ve durum geçişi tek transaction'da yazılır.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void applyDecision(Integer talepId, EnumEvetHayir karar, String gerekce) {
        if (talepId == null || karar == null) {
            throw new IllegalArgumentException("Üretim kararı ve talep zorunludur.");
        }
        TabTalep talep = em.find(TabTalep.class, talepId);
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }
        EnumTabTalepIslem action = karar == EnumEvetHayir.EVET
                ? EnumTabTalepIslem.KARAR_EVET
                : EnumTabTalepIslem.KARAR_HAYIR;
        if (karar == EnumEvetHayir.HAYIR && StringUtil.isBlank(gerekce)) {
            throw new IllegalArgumentException("Üretim uygun bulunmadığında kapatma gerekçesi zorunludur.");
        }

        checkPermission(action);
        checkTransition(talep, action);
        checkActionGuards(talep, action);

        SyKullanici kullanici = Util.getSyKullanici();
        TabUretimKarar kararKaydi = TabUretimKarar.builder()
                .tabTalep(talep)
                .karar(karar)
                .kararTarihi(LocalDateTime.now())
                .karariVeren(kullanici)
                .kararGerekcesi(gerekce)
                .build();
        em.persist(kararKaydi);
        em.flush();

        if (karar == EnumEvetHayir.HAYIR) {
            talep.setKapatmaNedeni(gerekce);
            talep.setKapatmaTarihi(LocalDateTime.now());
            talep.setKapatanSyKullanici(kullanici);
        }
        doTransition(talep, action, StringUtil.isBlank(gerekce) ? action.getDisplayValue() : gerekce);
    }

    /**
     * Ücretli/ücretsiz işlem kaydını oluşturur (onay bekliyor durumunda); talep tipini günceller.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveCharge(Integer talepId, EnumTabUcretTuru ucretTuru, BigDecimal tutar,
                           LocalDateTime odemeTarihi, String odemeYontemi, String dekontNo,
                           String ucretsizGerekce, String aciklama) {
        if (talepId == null || ucretTuru == null) {
            throw new IllegalArgumentException("Ücret türü ve talep zorunludur.");
        }
        TabTalep talep = em.find(TabTalep.class, talepId);
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }
        if (talep.getDurum() != EnumTabTalepDurumu.UCRET_BEKLIYOR) {
            throw new IllegalArgumentException("Bu aşamada ücretlendirme yapılamaz.");
        }
        if (ucretTuru == EnumTabUcretTuru.UCRETLI) {
            if (tutar == null || tutar.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Ücretli işlemlerde ödeme tutarı sıfırdan büyük olmalıdır.");
            }
            if (odemeTarihi == null) {
                throw new IllegalArgumentException("Ödeme tarihi zorunludur.");
            }
        } else if (StringUtil.isBlank(ucretsizGerekce)) {
            throw new IllegalArgumentException("Ücretsiz işlem gerekçesi zorunludur.");
        }

        TabOdeme odeme = TabOdeme.builder()
                .tabTalep(talep)
                .ucretTuru(ucretTuru)
                .tutar(tutar)
                .odemeTarihi(odemeTarihi)
                .odemeYontemi(odemeYontemi)
                .dekontNo(dekontNo)
                .ucretsizGerekce(ucretsizGerekce)
                .onayDurumu(EnumTabOnayDurumu.BEKLIYOR)
                .aciklama(aciklama)
                .build();
        em.persist(odeme);
        em.flush();

        talep.setUcretTuru(ucretTuru);
        em.merge(talep);
        em.flush();
    }

    /**
     * Detay ekranındaki tek adımlık işlem: ödeme kaydı + onay + durum geçişi
     * aynı transaction'da yapılır.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void applyChargeAndApprove(Integer talepId, EnumTabUcretTuru ucretTuru, BigDecimal tutar,
                                      LocalDateTime odemeTarihi, String odemeYontemi, String dekontNo,
                                      String ucretsizGerekce, String aciklama) {
        saveCharge(talepId, ucretTuru, tutar, odemeTarihi, odemeYontemi, dekontNo, ucretsizGerekce, aciklama);
        TabTalep talep = em.find(TabTalep.class, talepId);
        TabOdeme odeme = odemeService.findLatestByTalep(talep);
        if (odeme == null) {
            throw new IllegalArgumentException("Ödeme kaydı oluşturulamadı.");
        }
        approveCharge(odeme.getId(), aciklama);
    }

    /**
     * Ödeme/ücretsiz işlem onayı: zorunlu belgeler kontrol edilir, onay yazılır ve
     * talep üretim aşamasına geçirilir. Hepsi tek transaction'da çalışır.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void approveCharge(Integer odemeId, String aciklama) {
        if (odemeId == null) {
            throw new IllegalArgumentException("Onaylanacak ödeme kaydı bulunamadı.");
        }
        TabOdeme odeme = em.find(TabOdeme.class, odemeId);
        if (odeme == null || odeme.getTabTalep() == null) {
            throw new IllegalArgumentException("Ödeme kaydı bulunamadı.");
        }
        TabTalep talep = em.find(TabTalep.class, odeme.getTabTalep().getId());
        EnumTabUcretTuru tur = odeme.getUcretTuru() == null ? talep.getUcretTuru() : odeme.getUcretTuru();
        EnumTabTalepIslem action = tur == EnumTabUcretTuru.UCRETLI
                ? EnumTabTalepIslem.UCRETLI_ODEME_ONAYLA
                : EnumTabTalepIslem.UCRETSIZ_ONAYLA;

        checkPermission(action);
        checkTransition(talep, action);
        checkActionGuards(talep, action);
        checkRequiredDocuments(talep, action, tur);

        odeme.setOnayDurumu(EnumTabOnayDurumu.ONAYLANDI);
        odeme.setOnaylayanSyKullanici(Util.getSyKullanici());
        odeme.setOnayTarihi(LocalDateTime.now());
        em.merge(odeme);
        em.flush();

        doTransition(talep, action, aciklama);
    }

    /**
     * Üretim kaydını açar ve talebi üretim aşamasına geçirir.
     * Ödeme/ücretsiz işlem onayı ve stok/depo bilgileri zorunludur.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void startProduction(Integer talepId, Integer sorumluKullaniciId, Integer stStokId,
                                Integer stDepoId, BigDecimal miktar, String aciklama) {
        if (talepId == null || stStokId == null || stDepoId == null) {
            throw new IllegalArgumentException("Üretim için talep, stok kartı ve depo zorunludur.");
        }
        TabTalep talep = em.find(TabTalep.class, talepId);
        SyKullanici sorumlu = sorumluKullaniciId == null ? Util.getSyKullanici() : em.find(SyKullanici.class, sorumluKullaniciId);
        StStok stStok = em.find(StStok.class, stStokId);
        StDepo stDepo = em.find(StDepo.class, stDepoId);
        if (talep == null || stStok == null || stDepo == null || sorumlu == null) {
            throw new IllegalArgumentException("Üretim için seçilen kayıtlar bulunamadı.");
        }

        EnumTabTalepIslem action = EnumTabTalepIslem.URETIM_BASLAT;
        checkPermission(action);
        checkTransition(talep, action);
        checkActionGuards(talep, action);
        checkDepoYetki(stDepo);

        TabOdeme odeme = odemeService.findLatestByTalep(talep);
        if (odeme == null || odeme.getOnayDurumu() != EnumTabOnayDurumu.ONAYLANDI) {
            throw new IllegalArgumentException("Ödeme/ücretsiz işlem onaylanmadan üretime başlanamaz.");
        }

        TabUretim uretim = TabUretim.builder()
                .tabTalep(talep)
                .asama(EnumTabUretimAsamasi.URETIMDE)
                .baslangicTarihi(LocalDateTime.now())
                .sorumluSyKullanici(sorumlu)
                .stStok(stStok)
                .stDepo(stDepo)
                .miktar(miktar == null ? BigDecimal.ONE : miktar)
                .aciklama(aciklama)
                .build();
        em.persist(uretim);
        em.flush();

        doTransition(talep, action, aciklama);
    }

    /**
     * Üretimi tamamlar ve mamul için URETIM_GIRISI stok hareketi oluşturur.
     * Bakiye, maliyet katmanı ve stok işlem logu StStokHareketService tarafından yazılır.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void completeProduction(Integer talepId, String aciklama) {
        if (talepId == null) {
            throw new IllegalArgumentException("Üretim tamamlanacak talep bulunamadı.");
        }
        TabTalep talep = em.find(TabTalep.class, talepId);
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }
        EnumTabTalepIslem action = EnumTabTalepIslem.URETIM_TAMAMLA;
        checkPermission(action);
        checkTransition(talep, action);
        checkActionGuards(talep, action);

        TabUretim uretim = uretimService.findLatestByTalep(talep);
        if (uretim == null || uretim.getAsama() != EnumTabUretimAsamasi.URETIMDE) {
            throw new IllegalArgumentException("Devam eden üretim kaydı bulunamadı.");
        }
        if (uretim.getStStok() == null || uretim.getStDepo() == null) {
            throw new IllegalArgumentException("Üretim stok kartı/depo bilgisi eksik.");
        }

        if (uretim.getUretimGirisHareket() == null) {
            StStokHareket hareket = new StStokHareket();
            hareket.setStStok(uretim.getStStok());
            hareket.setStDepo(uretim.getStDepo());
            hareket.setHareketTuru(EnumStHareketTuru.URETIM_GIRISI);
            hareket.setYon(EnumGirisCikis.GIRIS);
            hareket.setMiktar(uretim.getMiktar() == null ? BigDecimal.ONE : uretim.getMiktar());
            hareket.setIslemTarihi(LocalDateTime.now());
            hareket.setBelgeTuru("TABTALEP");
            hareket.setBelgeNo(talep.getTalepNo());
            hareket.setAciklama("Tabanlık üretim girişi - " + talep.getTalepNo()
                    + (StringUtil.isBlank(aciklama) ? "" : " / " + aciklama));
            StStokHareket kaydedilen = stStokHareketService.hareketEkle(hareket);
            uretim.setUretimGirisHareket(kaydedilen);
            em.merge(uretim);
            em.flush();
        }

        uretim.setAsama(EnumTabUretimAsamasi.URETIM_TAMAMLANDI);
        uretim.setTamamlanmaTarihi(LocalDateTime.now());
        em.merge(uretim);
        em.flush();

        doTransition(talep, action, aciklama);
    }

    /**
     * Teslimat kaydı + stok çıkışı + talep kapatma tek transaction'da.
     * Ücretli → SATIS, ücretsiz → URUN_CIKISI.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void applyDelivery(Integer talepId, String aciklama) {
        if (talepId == null) {
            throw new IllegalArgumentException("Teslimat yapılacak talep bulunamadı.");
        }
        TabTalep talep = em.find(TabTalep.class, talepId);
        if (talep == null) {
            throw new IllegalArgumentException("Talep kaydı bulunamadı.");
        }
        EnumTabTalepIslem action = EnumTabTalepIslem.TESLIM_ET;
        checkPermission(action);
        checkTransition(talep, action);
        checkActionGuards(talep, action);

        if (!randevuService.existsAttended(talep, EnumTabRandevuTuru.TESLIMAT)) {
            throw new IllegalArgumentException("Teslimat randevusu gerçekleşmeden teslimat yapılamaz.");
        }
        if (!belgeService.existsByType(talep, EnumTabBelgeTuru.TESLIMAT_FORMU)) {
            throw new IllegalArgumentException("Teslimat öncesinde imzalı teslimat formu yüklenmelidir.");
        }
        TabUretim uretim = uretimService.findLatestByTalep(talep);
        if (uretim == null || uretim.getAsama() != EnumTabUretimAsamasi.URETIM_TAMAMLANDI) {
            throw new IllegalArgumentException("Üretimi tamamlanmayan ürün teslim edilemez.");
        }

        if (!teslimatService.existsByTalep(talep)) {
            TabRandevu randevu = randevuService.findByTalepAndTur(talep, EnumTabRandevuTuru.TESLIMAT).stream()
                    .filter(r -> r.getDurum() == EnumTabRandevuDurumu.GERCEKLESTI)
                    .findFirst()
                    .orElse(null);

            EnumStHareketTuru hareketTuru = talep.getUcretTuru() == EnumTabUcretTuru.UCRETLI
                    ? EnumStHareketTuru.SATIS
                    : EnumStHareketTuru.URUN_CIKISI;
            StStokHareket hareket = new StStokHareket();
            hareket.setStStok(uretim.getStStok());
            hareket.setStDepo(uretim.getStDepo());
            hareket.setHareketTuru(hareketTuru);
            hareket.setYon(EnumGirisCikis.CIKIS);
            hareket.setMiktar(uretim.getMiktar() == null ? BigDecimal.ONE : uretim.getMiktar());
            hareket.setIslemTarihi(LocalDateTime.now());
            hareket.setBelgeTuru("TABTALEP");
            hareket.setBelgeNo(talep.getTalepNo());
            hareket.setAciklama("Tabanlık teslimatı - " + talep.getTalepNo()
                    + (StringUtil.isBlank(aciklama) ? "" : " / " + aciklama));
            StStokHareket kaydedilen = stStokHareketService.hareketEkle(hareket);

            String urunBilgileri = uretim.getStStok() == null ? "" : uretim.getStStok().getMalzemeKodu();
            TabTeslimat teslimat = TabTeslimat.builder()
                    .tabTalep(talep)
                    .tabRandevu(randevu)
                    .teslimatTarihi(LocalDateTime.now())
                    .teslimEdenSyKullanici(Util.getSyKullanici())
                    .urunBilgileri(urunBilgileri)
                    .stStokHareket(kaydedilen)
                    .stokHareketTuru(hareketTuru)
                    .aciklama(aciklama)
                    .build();
            em.persist(teslimat);
            em.flush();
        }

        doTransition(talep, action, aciklama);
    }

    /**
     * Mevcut durumdan geri dönülebilecek adımlar.
     */
    public Set<EnumTabTalepDurumu> getBackOptions(TabTalep talep) {
        if (talep == null || talep.getDurum() == null) {
            return Set.of();
        }
        return talep.getDurum().getPreviousStatuses();
    }

    /**
     * Mevcut durumda uygulanabilir işlemler (yetki filtreli).
     */
    public boolean isActionAvailable(TabTalep talep, EnumTabTalepIslem action) {
        if (talep == null || action == null) {
            return false;
        }
        if (!action.getSourceStatuses().contains(talep.getDurum())) {
            return false;
        }
        return syYetkiService.hasPermission(Util.getSyKullanici(), action.getPermission());
    }

    private EnumTabTalepIslem resolveAppointmentAction(EnumTabRandevuTuru tur, EnumTabTalepDurumu durum) {
        if (tur == EnumTabRandevuTuru.OLCU && durum == EnumTabTalepDurumu.YENI) {
            return EnumTabTalepIslem.RANDEVU_VER;
        }
        if (tur == EnumTabRandevuTuru.TESLIMAT && durum == EnumTabTalepDurumu.TESLIMAT_RANDEVUSU_BEKLIYOR) {
            return EnumTabTalepIslem.TESLIMAT_RANDEVUSU_VER;
        }
        throw new IllegalArgumentException("Bu aşamada " + tur.getDisplayValue() + " randevusu verilemez.");
    }

    private void checkPermission(EnumTabTalepIslem action) {
        if (!syYetkiService.hasPermission(Util.getSyKullanici(), action.getPermission())) {
            throw new IllegalArgumentException("Bu işlem için yetkiniz bulunmuyor: " + action.getDisplayValue());
        }
    }

    private void checkTransition(TabTalep talep, EnumTabTalepIslem action) {
        if (!action.getSourceStatuses().contains(talep.getDurum())) {
            throw new IllegalArgumentException(
                    "Talep mevcut durumda (" + talep.getDurum().getDisplayValue() +
                            ") bu işlem uygulanamaz: " + action.getDisplayValue());
        }
        if (talep.getDurum().isClosed()) {
            throw new IllegalArgumentException("Kapatılmış talep üzerinde işlem yapılamaz.");
        }
    }

    private void checkGuards(TabTalep talep, EnumTabTalepIslem action, EnumTabRandevuTuru tur) {
        if (action == EnumTabTalepIslem.RANDEVU_VER) {
            if (talep.getTabKisi() == null || talep.getTabKisi().getId() == null) {
                throw new IllegalArgumentException("Randevu verilmeden önce talep kişisi seçilmelidir.");
            }
            if (tur != EnumTabRandevuTuru.OLCU) {
                throw new IllegalArgumentException("Bu aşamada yalnızca ölçüm randevusu verilebilir.");
            }
        }
        if (action == EnumTabTalepIslem.TESLIMAT_RANDEVUSU_VER) {
            TabUretim uretim = uretimService.findLatestByTalep(talep);
            if (uretim == null || uretim.getAsama() != EnumTabUretimAsamasi.URETIM_TAMAMLANDI) {
                throw new IllegalArgumentException("Üretim tamamlanmadan teslimat randevusu verilemez.");
            }
        }
    }

    private void checkDepoYetki(StDepo depo) {
        SyKullanici kullanici = Util.getSyKullanici();
        if (kullanici == null || stDepoYetkiService.isSistemYoneticisi(kullanici)) {
            return;
        }
        if (stDepoYetkiService.findByKullaniciByDepo(kullanici, depo) == null) {
            throw new IllegalArgumentException("Seçili depoda işlem yapma yetkiniz bulunmuyor.");
        }
    }

    private void checkRequiredDocuments(TabTalep talep, EnumTabTalepIslem action, EnumTabUcretTuru tur) {
        for (EnumTabBelgeTuru belgeTuru : belgeZorunlulukService.getRequiredTurler(tur, action.name())) {
            if (!belgeService.existsByType(talep, belgeTuru)) {
                throw new IllegalArgumentException("Onay için zorunlu belge yüklenmelidir: "
                        + belgeTuru.getDisplayValue());
            }
        }
    }

    private void checkActionGuards(TabTalep talep, EnumTabTalepIslem action) {
        if (action == EnumTabTalepIslem.TESLIM_ET) {
            TabUretim uretim = uretimService.findLatestByTalep(talep);
            if (uretim == null || uretim.getAsama() != EnumTabUretimAsamasi.URETIM_TAMAMLANDI) {
                throw new IllegalArgumentException("Üretim tamamlanmadan teslimat yapılamaz.");
            }
        }
        if (action == EnumTabTalepIslem.URETIM_TAMAMLA) {
            TabUretim uretim = uretimService.findLatestByTalep(talep);
            if (uretim == null || uretim.getAsama() != EnumTabUretimAsamasi.URETIMDE) {
                throw new IllegalArgumentException("Devam eden üretim kaydı olmadan üretim tamamlanamaz.");
            }
        }
        if (action == EnumTabTalepIslem.UCRETLI_ODEME_ONAYLA || action == EnumTabTalepIslem.UCRETSIZ_ONAYLA) {
            if (talep.getUcretTuru() == null) {
                throw new IllegalArgumentException("Ücret türü belirlenmeden onay yapılamaz.");
            }
        }
        if (action == EnumTabTalepIslem.KARAR_EVET || action == EnumTabTalepIslem.KARAR_HAYIR) {
            if (!olcumService.existsByTalep(talep)) {
                throw new IllegalArgumentException("Ölçüm kaydı olmadan üretim kararı verilemez.");
            }
        }
        if (action == EnumTabTalepIslem.OLCUM_TAMAMLA) {
            TabOlcum sonOlcum = olcumService.findByTalep(talep).stream().findFirst().orElse(null);
            if (sonOlcum == null || !olcumService.hasBothSides(sonOlcum)) {
                throw new IllegalArgumentException("Ölçüm tamamlanmadan bu adıma geçilemez. Sağ ve sol ayak ölçüleri zorunludur.");
            }
        }
    }

    private void doTransition(TabTalep talep, EnumTabTalepIslem action, String description) {
        em.lock(talep, LockModeType.OPTIMISTIC);
        EnumTabTalepDurumu eskiDurum = talep.getDurum();
        EnumTabTalepDurumu yeniDurum = action.getTargetStatus();
        talep.setDurum(yeniDurum);
        em.merge(talep);
        em.flush();

        hareketService.add(talep, eskiDurum, yeniDurum, action.name(),
                StringUtil.isBlank(description) ? action.getDisplayValue() : description);
        em.flush();
    }
}

