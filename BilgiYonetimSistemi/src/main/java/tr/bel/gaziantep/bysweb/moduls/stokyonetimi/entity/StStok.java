package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 10:56
 */
@Getter
@Setter
@Entity
@Table(name = "STSTOK")
public class StStok extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -1621611929348806812L;

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

    @Column(name = "KRITIK_STOK", precision = 18, scale = 4)
    private BigDecimal kritikStok;

    @ColumnDefault("0")
    @Column(name = "SERI_NO_TAKIBI", nullable = false)
    private boolean seriNoTakibi;

    @ColumnDefault("0")
    @Column(name = "SKT_TAKIBI", nullable = false)
    private boolean sktTakibi;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "stStok", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @SQLRestriction("AKTIF = true")
    private List<StStokBarkod> stokBarkodList = new ArrayList<>();

    @OneToMany(mappedBy = "stStok", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @SQLRestriction("AKTIF = true")
    private List<StStokBirimDonusum> stokBirimDonusumList = new ArrayList<>();

    @OneToMany(mappedBy = "stStok", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @SQLRestriction("AKTIF = true")
    private List<StStokBakiye> stokBakiyeList = new ArrayList<>();

    @OneToMany(mappedBy = "stStok", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @SQLRestriction("AKTIF = true")
    private List<StStokHareket> stokHareketList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StStok other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}