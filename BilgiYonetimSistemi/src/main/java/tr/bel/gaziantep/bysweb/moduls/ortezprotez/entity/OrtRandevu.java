package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 24.09.2025 09:02
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORTRANDEVU")
@NamedQuery(name = "OrtRandevu.findByStartDateByEndDate", query = "SELECT e FROM OrtRandevu e WHERE e.aktif=true AND " +
        "(e.randevuTarihi>=:start AND e.randevuTarihi<=:end) ORDER BY e.randevuTarihi ASC")
public class OrtRandevu extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 186993199640698158L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTHASTA_ID")
    private OrtHasta ortHasta;

    @Column(name = "RANDEVU_TARIHI")
    private LocalDateTime randevuTarihi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KONU", length = 50)
    private String konu;

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
        if (!(object instanceof OrtRandevu other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}