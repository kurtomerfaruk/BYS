package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStTransferDurum;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 10:17
 */
@Getter
@Setter
@Entity
@Table(name = "STTRANSFER_KALEM")
public class StTransferKalem extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2355097694446546678L;

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
    private BigDecimal miktar;

    @Column(name = "SEVK_MIKTAR", precision = 18, scale = 4)
    private BigDecimal sevkMiktar;

    @Column(name = "TESLIM_MIKTAR", precision = 18, scale = 4)
    private BigDecimal teslimMiktar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STBIRIM_ID")
    private StBirim stBirim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KAYNAK_STDEPO_LOKASYON_ID")
    private StDepoLokasyon kaynakStDepoLokasyon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HEDEF_STDEPO_LOKASYON_ID")
    private StDepoLokasyon hedefStDepoLokasyon;

    @Size(max = 50)
    @Nationalized
    @Enumerated(EnumType.STRING)
    @Column(name = "DURUM", length = 50)
    private EnumStTransferDurum durum;

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