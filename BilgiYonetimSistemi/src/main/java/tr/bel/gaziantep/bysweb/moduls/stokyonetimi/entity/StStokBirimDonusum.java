package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 10:59
 */
@Getter
@Setter
@Entity
@Table(name = "STSTOK_BIRIM_DONUSUM")
public class StStokBirimDonusum extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2589841456095199186L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "DONUSUM_CARPANI", precision = 18, scale = 8)
    private BigDecimal donusumCarpani;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STBIRIM_ID")
    private StBirim stBirim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSTOK_ID")
    private StStok stStok;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StStokBirimDonusum other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}