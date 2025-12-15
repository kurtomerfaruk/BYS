package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.genel.*;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.07.2025 10:08
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GNLKISI")
@NamedQuery(name = "GnlKisi.existByTcKimlikNo", query = "SELECT k FROM GnlKisi k WHERE k.tcKimlikNo=:tcKimlikNo")
@NamedQuery(name = "GnlKisi.findByLatLngIsNull", query = "SELECT k FROM GnlKisi k WHERE k.aktif=true AND k.latLng IS NULL")
@NamedQuery(name = "GnlKisi.findByBinaNo", query = "SELECT g FROM GnlKisi g WHERE g.aktif=true AND g.binaNo = :binaNo")
@NamedQuery(name = "GnlKisi.getTcList", query = "SELECT g FROM GnlKisi g WHERE g.tcKimlikNo IS NOT NULL AND " +
        "LENGTH(g.tcKimlikNo)=11 AND g.dogumTarihi IS NOT NULL ORDER BY g.mernisGuncellemeTarihi ASC")
@NamedQuery(name = "GnlKisi.findByTckimlikNoByDogumTarihi", query = "SELECT g FROM GnlKisi g WHERE g.tcKimlikNo = :tcKimlikNo AND " +
        "g.dogumTarihi=:dogumTarihi")
@NamedQuery(name = "GnlKisi.getByMernisGuncellemeTarihi", query = "SELECT g FROM GnlKisi g WHERE g.aktif=true " +
        "AND g.dogumTarihi IS NOT NULL AND g.tcKimlikNo IS NOT NULL " +
        "AND g.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG " +
        "ORDER BY g.mernisGuncellemeTarihi")
@NamedQuery(name = "GnlKisi.findByTcKimlikNoListToList", query = "SELECT k FROM GnlKisi k WHERE k.tcKimlikNo IN :tcList " +
        "AND k.aktif=true AND k.durum = tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG ")
public class GnlKisi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -4297582255151551064L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "KAYIT_TARIHI")
    private LocalDate kayitTarihi;

    @Column(name = "UYRUK")
    @Enumerated(EnumType.STRING)
    private EnumGnlUyruk uyruk;

    @Size(max = 11)
    @Nationalized
    @Column(name = "TC_KIMLIK_NO", length = 11)
    private String tcKimlikNo;

    @Size(max = 15)
    @Nationalized
    @Column(name = "KIMLIK_SERI_NO", length = 15)
    private String kimlikSeriNo;

    @Column(name = "SON_GECERLILIK_TARIHI")
    private LocalDate sonGecerlilikTarihi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "AD", length = 50)
    private String ad;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SOYAD", length = 50)
    private String soyad;

    @Column(name = "DOGUM_TARIHI")
    private LocalDate dogumTarihi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "DOGUM_YERI", length = 50)
    private String dogumYeri;

    @Column(name = "CINSIYET")
    @Enumerated(EnumType.STRING)
    private EnumGnlCinsiyet cinsiyet;

    @Column(name = "MEDENI_DURUM")
    @Enumerated(EnumType.STRING)
    private EnumGnlMedeniDurum medeniDurum;

    @Column(name = "EGITIM_DURUMU", length = 50)
    @Enumerated(EnumType.STRING)
    private EnumGnlEgitimDurumu egitimDurumu;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ANA_ADI", length = 50)
    private String anaAdi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "BABA_ADI", length = 50)
    private String babaAdi;

    @Size(max = 20)
    @Nationalized
    @Column(name = "TELEFON", length = 20)
    private String telefon;

    @Size(max = 20)
    @Nationalized
    @Column(name = "TELEFON_2", length = 20)
    private String telefon2;

    @Size(max = 100)
    @Nationalized
    @Email(message = "Lütfen E-Posta adresini doğrulayın")
    @Column(name = "EPOSTA_ADRESI", length = 100)
    private String epostaAdresi;

    @Column(name = "COCUK_SAYISI")
    private Short cocukSayisi;

    @Column(name = "EVDE_YASAYAN")
    private Short evdeYasayan;

    @Column(name = "ADRES_NO")
    private Long adresNo;

    @Column(name = "ADRES")
    @Nationalized
    @Size(max = 2147483647)
    private String adres;

    @Column(name = "ADRES_2")
    @Nationalized
    @Size(max = 2147483647)
    private String adres2;

    @Column(name = "BINA_NO")
    private Integer binaNo;

    @Column(name = "BINA_KODU")
    private Long binaKodu;

    @Size(max = 50)
    @Nationalized
    @Column(name = "LAT_LNG", length = 50)
    private String latLng;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLIL_ID")
    private GnlIl gnlIl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLILCE_ID")
    private GnlIlce gnlIlce;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLMAHALLE_ID")
    private GnlMahalle gnlMahalle;

    @Column(name = "DURUM")
    @Enumerated(EnumType.STRING)
    private EnumGnlDurum durum;

    @Column(name = "MERNIS_GUNCELLEME_TARIHI")
    private LocalDateTime mernisGuncellemeTarihi;

    @Column(name = "OLUM_TARIHI")
    private LocalDate olumTarihi;

    @Size(max = 100)
    @Nationalized
    @Column(name = "RESIM_ADI", length = 100)
    private String resimAdi;

    @Size(max = 250)
    @Nationalized
    @Column(name = "RESIM_YOLU", length = 250)
    private String resimYolu;

    @Column(name = "KIMINLE_YASIYOR")
    @Enumerated(EnumType.STRING)
    private EnumGnlKiminleYasiyor kiminleYasiyor;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KIMINLE_YASIYOR_ACIKLAMA", length = 50)
    private String kiminleYasiyorAciklama;

    @Size(max = 100)
    @Nationalized
    @Column(name = "MESLEK", length = 100)
    private String meslek;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ESININ_MESLEGI", length = 100)
    private String esininMeslegi;

    @Column(name = "AYLIK_GELIRI", precision = 8, scale = 2)
    private BigDecimal aylikGeliri;

    @Size(max = 2147483647)
    @Nationalized
    @Column(name = "GELIR_KAYNAGI_ACIKLAMA")
    private String gelirKaynagiAciklama;

    @Column(name = "SOSYAL_GUVENCE")
    @Enumerated(EnumType.STRING)
    private EnumGnlSosyalGuvence sosyalGuvence;

    @Column(name = "ARAC_VAR_MI")
    private boolean aracVarMi;

    @Size(max = 50)
    @Column(name = "MAL_VARLIGI", length = 50)
    @Nationalized
    private String malVarligi;

    @Column(name = "EV_TURU")
    @Enumerated(EnumType.STRING)
    private EnumGnlEvTuru evTuru;

    @Column(name = "EVIN_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumGnlEvinDurumu evinDurumu;

    @Size(max = 50)
    @Column(name = "EVIN_DURUMU_ACIKLAMA", length = 50)
    @Nationalized
    private String evinDurumuAciklama;

    @Column(name = "ISINMA_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumGnlIsinmaDurumu isinmaDurumu;

    @Column(name = "SABIKA_KAYDI")
    private boolean sabikaKaydi;

    @Size(max = 50)
    @Column(name = "SABIKA_KAYDI_ACIKLAMA", length = 50)
    @Nationalized
    private String sabikaKaydiAciklama;

    @Column(name = "ISKUR_KAYDI")
    private boolean iskurKaydi;

    @Column(name = "EHLIYET")
    private boolean ehliyet;

    @Size(max = 50)
    @Nationalized
    @Column(name = "EHLIYET_ACIKLAMA", length = 50)
    private String ehliyetAciklama;

    @Column(name = "CALISMA_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumGnlCalismaDurumu calismaDurumu;


    @Column(name = "SOSYAL_YARDIM_ALIYOR_MU")
    private boolean sosyalYardimAliyorMu;

    @Column(name = "EKLEME_YERI")
    @Enumerated(EnumType.STRING)
    private EnumModul eklemeYeri;

    @Column(name = "ASKERLIK_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumGnlAskerlikDurumu askerlikDurumu;

    @Column(name = "KIRA_BEDELI", precision = 8, scale = 2)
    private BigDecimal kiraBedeli;

    @OneToMany(mappedBy = "gnlKisi", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    private List<GnlKisiFaydalandigiHaklar> gnlKisiFaydalandigiHaklarList = new ArrayList<>();

    @OneToMany(mappedBy = "gnlKisi", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    private List<GnlKisiGelirKaynagi> gnlKisiGelirKaynagiList = new ArrayList<>();

    @OneToMany(mappedBy = "gnlKisi", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    private List<GnlKisiYardimAlinanYerler> gnlKisiYardimAlinanYerlerList = new ArrayList<>();

    @OneToMany(mappedBy = "gnlKisi", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    private List<GnlKisiAldigiYardimlar> gnlKisiAldigiYardimlarList = new ArrayList<>();

    @Transient
    @Getter(AccessLevel.NONE)
    private String adSoyad;

    public String getAdSoyad() {
        String result = "";
        if (StringUtil.isNotBlank(ad)) result += ad;
        if (StringUtil.isNotBlank(soyad)) result += " " + soyad;
        return result;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlKisi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}