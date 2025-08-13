package tr.bel.gaziantep.bysweb.moduls.hafriyat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlBelediye;
import tr.bel.gaziantep.bysweb.core.enums.hafriyat.EnumHfAracTuru;
import tr.bel.gaziantep.bysweb.core.enums.hafriyat.EnumHfAtikCinsi;
import tr.bel.gaziantep.bysweb.core.enums.hafriyat.EnumHfIsTuru;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "HFHAFRIYAT_IS")
public class HfHafriyatIs extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -1556455013264749102L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "EVRAK_NO", length = 50)
    private String evrakNo;

    @Column(name = "EVRAK_TARIHI")
    private LocalDate evrakTarihi;

    @Column(name = "EVRAK_BELEDIYE")
    @Enumerated(EnumType.STRING)
    private EnumGnlBelediye evrakBelediye;

    @Size(max = 50)
    @Column(name = "SOZLESME_NO", length = 50)
    private String sozlesmeNo;

    @Column(name = "SOZLESME_TARIHI")
    private LocalDate sozlesmeTarihi;

    @Size(max = 250)
    @Nationalized
    @Column(name = "NEREDEN", length = 250)
    private String nereden;

    @Size(max = 250)
    @Nationalized
    @Column(name = "NEREYE", length = 250)
    private String nereye;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "URETICI_HFFIRMA_ID")
    private HfFirma ureticiHfFirma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASIYICI_HFFIRMA_ID")
    private HfFirma tasiyiciHfFirma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPOLAMA_TESISI_HFFIRMA_ID")
    private HfFirma depolamaTesisiHfFirma;

    @Column(name = "ATIK_CINSI")
    @Enumerated(EnumType.STRING)
    private EnumHfAtikCinsi atikCinsi;

    @Column(name = "ATIK_CINSI_FIYAT", precision = 18, scale = 2)
    private BigDecimal atikCinsiFiyat=BigDecimal.ZERO;

    @Column(name = "TAHMINI_MIKTAR", precision = 18, scale = 2)
    private BigDecimal tahminiMiktar=BigDecimal.ZERO;

    @Column(name = "TAHMINI_TUTAR", precision = 18, scale = 2)
    private BigDecimal tahminiTutar=BigDecimal.ZERO;

    @Column(name = "GERCEK_MIKTAR", precision = 18, scale = 2)
    private BigDecimal gercekMiktar=BigDecimal.ZERO;

    @Column(name = "GERCEK_TUTAR", precision = 18, scale = 2)
    private BigDecimal gercekTutar=BigDecimal.ZERO;

    @Column(name = "EVRAK_TESLIM_EDILDI_MI")
    private boolean evrakTeslimEdildiMi;

    @Size(max = 50)
    @Column(name = "FATURA_NO", length = 50)
    private String faturaNo;

    @Column(name = "FATURA_TARIHI")
    private LocalDate faturaTarihi;

    @Nationalized
    @Lob
    @Column(name = "MEVKI")
    private String mevki;

    @Size(max = 50)
    @Column(name = "\"ADA\"", length = 50)
    private String ada;

    @Size(max = 50)
    @Column(name = "PAFTA", length = 50)
    private String pafta;

    @Size(max = 50)
    @Column(name = "PARSEL", length = 50)
    private String parsel;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Column(name = "IS_TURU")
    @Enumerated(EnumType.STRING)
    private EnumHfIsTuru isTuru;

    @Column(name = "IHALE_BASLANGIC_TARIHI")
    private LocalDate ihaleBaslangicTarihi;

    @Column(name = "IHALE_BITIS_TARIHI")
    private LocalDate ihaleBitisTarihi;

    @Column(name = "AYAK_YERI")
    private boolean ayakYeri;

    @Column(name = "TASIMA_ARAC_TURU")
    @Enumerated(EnumType.STRING)
    private EnumHfAracTuru tasimaAracTuru;

    @Column(name = "TASIMA_UCRETI", precision = 18, scale = 2)
    private BigDecimal tasimaUcreti=BigDecimal.ZERO;

    @Column(name = "TASIMA_ARAC_MIKTARI")
    private Integer tasimaAracMiktari=0;

    @OneToMany(mappedBy = "hfHafriyatIs")
    private List<HfHafriyatIsArac> hfHafriyatIsAracList = new ArrayList<>();

    @OneToMany(mappedBy = "hfHafriyatIs")
    private List<HfHafriyatIsFatura> hfHafriyatIsFaturaList = new ArrayList<>();

    @OneToMany(mappedBy = "hfHafriyatIs")
    private List<HfKasa> hfKasaList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HfHafriyatIs other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}