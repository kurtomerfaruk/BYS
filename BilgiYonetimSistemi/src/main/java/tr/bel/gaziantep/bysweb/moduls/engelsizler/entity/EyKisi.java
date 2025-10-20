package tr.bel.gaziantep.bysweb.moduls.engelsizler.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyAnketDurumu;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyHizmetlerdenMemnun;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyYardimaUygun;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlCalismaDurumu;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlEbeveynDurumu;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 28.06.2025 08:32
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EYKISI")
@NamedQuery(name = "EyKisi.findByKisiTcKimlikNoList", query = "SELECT DISTINCT e.gnlKisi.tcKimlikNo FROM EyKisi e WHERE e.gnlKisi.tcKimlikNo IN :tcList")
@NamedQuery(name = "EyKisi.findByAdress", query = "SELECT e FROM EyKisi e WHERE e.aktif=true AND e.gnlKisi.binaNo IS NOT NULL" +
        " AND e.gnlKisi.latLng IS NULL")
@NamedQuery(name = "EyKisi.getTcList", query = "SELECT e.gnlKisi FROM EyKisi e WHERE e.gnlKisi.tcKimlikNo IS NOT NULL AND " +
        "LENGTH(e.gnlKisi.tcKimlikNo)=11 AND e.gnlKisi.dogumTarihi IS NOT NULL AND " +
        "e.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG ORDER BY e.gnlKisi.mernisGuncellemeTarihi ASC")
@NamedQuery(name = "EyKisi.findByKisiTcKimlikNo", query = "SELECT e FROM EyKisi e WHERE e.aktif=true AND e.gnlKisi.tcKimlikNo = :tcKimlikNo")
@NamedQuery(name = "EyKisi.findByKisi", query = "SELECT e FROM EyKisi e WHERE e.gnlKisi = :gnlKisi")
@NamedQuery(name = "EyKisi.getTotalDeadCount", query = "SELECT COUNT(e.id) FROM EyKisi e WHERE e.aktif=true AND " +
        "e.gnlKisi.durum IN (tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.OLU,tr.bel.gaziantep.bysweb.core.enums.genel" +
        ".EnumGnlDurum.OLUM,tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.OLUMUN_TESPITI)")
@NamedQuery(name = "EyKisi.getTotalActiveCount", query = "SELECT COUNT(e.id) FROM EyKisi e WHERE e.aktif=true AND " +
        "e.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG")
@NamedQuery(name = "EyKisi.getAllCoordinates", query = "SELECT e.gnlKisi.latLng FROM EyKisi e WHERE e.aktif=true AND e.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG AND e.gnlKisi.latLng IS NOT NULL")
public class EyKisi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6659959936794498821L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "GNLKISI_ID")
    private GnlKisi gnlKisi;

    @Column(name = "ANKET_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumEyAnketDurumu anketDurumu;

    @Lob
    @Nationalized
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Lob
    @Nationalized
    @Column(name = "KATILDIGI_KURSLAR")
    private String katildigiKurslar;

    @Size(max = 50)
    @Column(name = "HAKLAR", length = 50)
    private String haklar;

    @Lob
    @Nationalized
    @Column(name = "TALEP_SIKAYET_ONERI")
    private String talepSikayetOneri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IRTIBAT_KURAN_GNLPERSONEL_ID")
    private GnlPersonel irtibatKuranGnlPersonel;

    @Column(name = "EBEVEYN_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumGnlEbeveynDurumu ebeveynDurumu;

    @Lob
    @Nationalized
    @Column(name = "OZEL_DURUMLAR")
    private String ozelDurumlar;

    @Lob
    @Nationalized
    @Column(name = "AILEDEKI_HUKUMLU_DURUMU")
    private boolean ailedekiHukumluDurumu;

    @Column(name = "AILEDEKI_HUKUMLU_DURUMU_ACIKLAMA")
    @Lob
    @Nationalized
    private String ailedekiHukumluDurumuAciklama;

    @Column(name = "ANNE_DURUM")
    @Enumerated(EnumType.STRING)
    private EnumGnlDurum anneDurum;

    @Column(name = "ANNE_CALISMA_DURUMU", length = 50)
    @Enumerated(EnumType.STRING)
    private EnumGnlCalismaDurumu anneCalismaDurumu;

    @Column(name = "BABA_DURUM")
    @Enumerated(EnumType.STRING)
    private EnumGnlDurum babaDurum;

    @Column(name = "BABA_CALISMA_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumGnlCalismaDurumu babaCalismaDurumu;

    @Column(name = "COCUK_CALISMA_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumGnlCalismaDurumu cocukCalismaDurumu;

    @Column(name = "COCUK_CALISMA_DURUMU_ACIKLAMA")
    private String cocukCalismaDurumuAciklama;

    @Column(name = "AILEDEKI_ENGELLI_SAYISI")
    private Integer ailedekiEngelliSayisi;

    @Column(name = "KULLANDIGI_CIHAZ_ACIKLAMA")
    private String kullandigiCihazAciklama;

    @Column(name = "KAYITLI_DERNEK_VAR_MI")
    private boolean kayitliDernekVarMi;

    @Column(name = "KAYITLI_DERNEK_ACIKLAMA")
    @Lob
    @Nationalized
    private String kayitliDernekAciklama;

    @Column(name = "REHABILITE_MERKEZINE_GIDIYOR_MUSUNUZ")
    private boolean rehabiliteMerkezineGidiyorMusunuz;

    @Size(max = 50)
    @Column(name = "REHABILITASYON_MERKEZININ_ADI", length = 50)
    private String rehabilitasyonMerkezininAdi;

    @Size(max = 50)
    @Column(name = "REHABILITE_SURESI", length = 50)
    private String rehabiliteSuresi;

    @Lob
    @Nationalized
    @Column(name = "IZLEME_YAPAN_KISININ_GORUSU")
    private String izlemeYapanKisininGorusu;

    @Column(name = "YARDIMA_UYGUN_MU")
    @Enumerated(EnumType.STRING)
    private EnumEyYardimaUygun yardimaUygunMu;

    @Column(name = "HIZMETLERDEN_MEMNUN_MU")
    @Enumerated(EnumType.STRING)
    private EnumEyHizmetlerdenMemnun hizmetlerdenMemnunMu;

    @Column(name = "ENGELLI_SAGLIK_RAPORU_VARMI")
    private boolean engelliSaglikRaporuVarmi;

    @Size(max = 50)
    @Column(name = "RAPOR_NO", length = 50)
    private String raporNo;

    @Column(name = "RAPOR_TARIHI")
    private LocalDate raporTarihi;

    @Column(name = "SUREKLI")
    private boolean surekli;

    @Column(name = "SUREKLI_ACIKLAMA")
    private String surekliAciklama;

    @Column(name = "RAPOR_BITIS_TARIHI")
    private LocalDate raporBitisTarihi;

    @Lob
    @Nationalized
    @Column(name = "HASTAHANE")
    private String hastahane;

    @Column(name = "TOPLAM_VUCUT_KAYIP_ORANI")
    private Integer toplamVucutKayipOrani;

    @Column(name = "AGIR_OZURLU")
    private boolean agirOzurlu;

    @Lob
    @Nationalized
    @Column(name = "ENGELLI_ACIKLAMA")
    private String engelliAciklama;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANKETI_YAPAN_GNLPERSONEL_ID")
    private GnlPersonel anketiYapanGnlpersonel;

    @Column(name = "ANKET_BASLANGIC_TARIHI")
    private LocalDateTime anketBaslangicTarihi;

    @Column(name = "ANKET_BITIS_TARIHI")
    private LocalDateTime anketBitisTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYCOZGER_ID")
    private EyCozger eyCozger;

    @OneToMany(mappedBy = "eyKisi", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    private List<EyKisiDosya> eyKisiDosyaList = new ArrayList<>();

    @OneToMany(mappedBy = "eyKisi", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    private List<EyKisiEngelGrubu> eyKisiEngelGrubuList = new ArrayList<>();

    @OneToMany(mappedBy = "eyKisi", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    private List<EyKisiKullandigiCihaz> eyKisiKullandigiCihazList = new ArrayList<>();

    @OneToMany(mappedBy = "eyKisi", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    private List<EyKisiMaddeKullanimi> eyKisiMaddeKullanimiList = new ArrayList<>();

    @OneToMany(mappedBy = "eyKisi", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    private List<EyKisiRapor> eyKisiRaporList = new ArrayList<>();

    @OneToMany(mappedBy = "eyKisi")
    @Builder.Default
    private List<EyTalep> eyTalepList = new ArrayList<>();

    @Transient
    @Getter(AccessLevel.NONE)
    private String kisiEngelGrubuStr;

    public String getKisiEngelGrubuStr() {
        kisiEngelGrubuStr = "";
        if (getEyKisiEngelGrubuList() != null && !getEyKisiEngelGrubuList().isEmpty()) {
            getEyKisiEngelGrubuList().stream().filter(EyKisiEngelGrubu::isSecili).forEach(keg -> kisiEngelGrubuStr += keg.getEyEngelGrubu().getTanim() + " ");
        }
        return kisiEngelGrubuStr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EyKisi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}