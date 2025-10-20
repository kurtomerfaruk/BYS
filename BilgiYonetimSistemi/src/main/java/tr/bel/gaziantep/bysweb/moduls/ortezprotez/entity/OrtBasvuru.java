package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruHareketDurumu;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORTBASVURU")
public class OrtBasvuru extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6351967435105213583L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTHASTA_ID")
    private OrtHasta ortHasta;

    @Column(name = "BASVURU_TARIHI")
    private LocalDateTime basvuruTarihi;

    @Column(name = "BASVURU_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumOrtBasvuruDurumu basvuruDurumu;

    @Column(name = "BASVURU_HAREKET_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumOrtBasvuruHareketDurumu basvuruHareketDurumu;

    @Column(name = "DEGERLENDIRME_TARIHI")
    private LocalDateTime degerlendirmeTarihi;

    @Nationalized
    @Lob
    @Column(name = "KARAR_GORUS")
    private String kararGorus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MUAYENE_YAPAN_ORTPERSONEL_ID")
    private OrtPersonel muayeneYapanOrtPersonel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RAPORU_ONAYLAYAN_ORTPERSONEL_ID")
    private OrtPersonel raporuOnaylayanOrtPersonel;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @ColumnDefault("0")
    @Column(name = "UCRETLI")
    private boolean ucretli;

    @Column(name = "TUTAR")
    private BigDecimal tutar;

    @ColumnDefault("0")
    @Column(name = "ODENDI")
    private boolean odendi;

    @Nationalized
    @Size(max = 50)
    @Column(name = "MAKBUZ_NO",length = 50)
    private String makbuzNo;

    @Nationalized
    @Size(max = 50)
    @Column(name = "SUT_KODU",length = 50)
    private String sutKodu;

    @OneToMany(mappedBy = "ortBasvuru", fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    @Builder.Default
    private List<OrtBasvuruHareket> ortBasvuruHareketList = new ArrayList<>();

    @OneToMany(mappedBy = "ortBasvuru")
    @SQLRestriction("AKTIF=true")
    @Builder.Default
    private List<OrtBasvuruMalzemeTeslimi> ortBasvuruMalzemeTeslimiList = new ArrayList<>();

    @OneToMany(mappedBy = "ortBasvuru")
    @SQLRestriction("AKTIF=true")
    @Builder.Default
    private List<OrtBasvuruDosya> ortBasvuruDosyaList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtBasvuru other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}