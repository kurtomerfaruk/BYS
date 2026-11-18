package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 15:58
 */
@Getter
@Setter
@Entity
@Table(name = "STTRANSFER_KALEM")
public class StTransferKalem extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -5332460820306386966L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STTRANSFER_ID")
    private StTransfer stTransfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSTOK_ID")
    private StStok stStok;

    @Column(name = "MIKTAR", precision = 18, scale = 4)
    private BigDecimal miktar = BigDecimal.ZERO;

    @Column(name = "SEVK_MIKTARI", precision = 18, scale = 4)
    private BigDecimal sevkMiktari = BigDecimal.ZERO;

    @Column(name = "TESLIM_MIKTARI", precision = 18, scale = 4)
    private BigDecimal teslimMiktari = BigDecimal.ZERO;

    @Size(max = 300)
    @Nationalized
    @Column(name = "ACIKLAMA", length = 300)
    private String aciklama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StTransferKalem other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}