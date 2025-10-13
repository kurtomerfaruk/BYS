package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.10.2025 11:05
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORTBASVURU_MALZEME_TESLIMI")
public class OrtBasvuruMalzemeTeslimi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 7795718781512761984L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTBASVURU_ID")
    private OrtBasvuru ortBasvuru;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTSTOK_ID")
    private OrtStok ortStok;

    @Column(name = "MIKTAR", precision = 18, scale = 2)
    private BigDecimal miktar;

    @Column(name = "TESLIM_TARIHI")
    private LocalDateTime teslimTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TESLIM_EDEN_ORTPERSONEL_ID")
    private OrtPersonel teslimEdenOrtPersonel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TESLIM_ALAN_GNLKISI_ID")
    private GnlKisi teslimAlanGnlKisi;

    @ColumnDefault("0")
    @Column(name = "STOKTAN_DUS", nullable = false)
    private boolean stoktanDus;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtBasvuruMalzemeTeslimi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}