package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "STSTOK")
public class StStok extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "MALZEME_KODU", nullable = false, length = 50)
    private String malzemeKodu;

    @Size(max = 50)
    @Nationalized
    @Column(name = "STOK_KODU", length = 50)
    private String stokKodu;

    @Size(max = 200)
    @Nationalized
    @Column(name = "STOK_ADI", length = 200)
    private String stokAdi;

    @Size(max = 100)
    @Nationalized
    @Column(name = "MARKA", length = 100)
    private String marka;

    @Size(max = 100)
    @Nationalized
    @Column(name = "MODEL", length = 100)
    private String model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSTOK_KATEGORI_ID")
    private StStokKategori stStokKategori;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STBIRIM_ID")
    private StBirim stBirim;

    @Column(name = "KDV_ORANI")
    private Integer kdvOrani;

    @Column(name = "MIN_STOK", precision = 18, scale = 4)
    private BigDecimal minStok;

    @Column(name = "MAX_STOK", precision = 18, scale = 4)
    private BigDecimal maxStok;

    @Column(name = "KRITIK_STOK", precision = 18, scale = 4)
    private BigDecimal kritikStok;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_ID")
    private StDepo stDepo;

    @ColumnDefault("0")
    @Column(name = "LOT_TAKIBI", nullable = false)
    private boolean lotTakibi;

    @ColumnDefault("0")
    @Column(name = "SERI_NO_TAKIBI", nullable = false)
    private boolean seriNoTakibi;

    @ColumnDefault("0")
    @Column(name = "SKT_TAKIBI", nullable = false)
    private boolean sktTakibi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "MALIYET_YONTEMI", length = 50)
    private String maliyetYontemi;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "stStok")
    private Set<StStokBarkod> ststokBarkods = new LinkedHashSet<>();

    @OneToMany(mappedBy = "stStok")
    private Set<StStokBirimDonusum> ststokBirimDonusums = new LinkedHashSet<>();


}