package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 10:37
 */
@Getter
@Setter
@Entity
@Table(name = "STSAYIM_KALEM")
public class StSayimKalem extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -6572653322360046199L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSAYIM_ID")
    private StSayim stSayim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSTOK_ID")
    private StStok stStok;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_LOKASYON_ID")
    private StDepoLokasyon stDepoLokasyon;

    @Column(name = "SISTEM_MIKTAR", precision = 18, scale = 4)
    private BigDecimal sistemMiktar;

    @Column(name = "SAYILAN_MIKTAR", precision = 18, scale = 4)
    private BigDecimal sayilanMiktar;

    @Column(name = "FARK_MIKTAR", precision = 18, scale = 4)
    private BigDecimal farkMiktar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STBIRIM_ID")
    private StStokHareket stBirim;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @ColumnDefault("0")
    @Column(name = "ONAYLANDI", nullable = false)
    private boolean onaylandi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StSayimKalem other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}