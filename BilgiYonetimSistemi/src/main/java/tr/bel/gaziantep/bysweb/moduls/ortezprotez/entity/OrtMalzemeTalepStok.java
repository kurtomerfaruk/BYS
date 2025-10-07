package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORTMALZEME_TALEP_STOK")
public class OrtMalzemeTalepStok extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -4280917838479263672L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTMALZEME_TALEP_ID")
    private OrtMalzemeTalep ortMalzemeTalep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTSTOK_ID")
    private OrtStok ortStok;

    @Column(name = "MIKTAR", precision = 18, scale = 2)
    private BigDecimal miktar;

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
        if (!(object instanceof OrtMalzemeTalepStok other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}