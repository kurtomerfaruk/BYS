package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 25.11.2026 14:10
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ORTTALEP_ODEME")
public class OrtTalepOdeme extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -8000676390210574479L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTTALEP_ID")
    private OrtTalep ortTalep;

    @Column(name = "TUTAR", precision = 18, scale = 2)
    private BigDecimal tutar;

    @Column(name = "ODEME_TARIHI")
    private LocalDateTime odemeTarihi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ODEME_SEKLI", length = 50)
    private String odemeSekli;

    @Size(max = 100)
    @Nationalized
    @Column(name = "DEKONT_NO", length = 100)
    private String dekontNo;

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
        if (!(object instanceof OrtTalepOdeme other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}