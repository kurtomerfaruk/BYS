package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlJoinTipi;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.12.2025 13:33
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_ENTITY_BAGLANTI")
public class GnlRaporEntityBaglanti extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -4867407215348357411L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_ID")
    private GnlRapor gnlRapor;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ENTITY_ADI", length = 100)
    private String entityAdi;

    @Size(max = 200)
    @Nationalized
    @Column(name = "ENTITY_CLASS", length = 200)
    private String entityClass;

    @Size(max = 500)
    @Nationalized
    @Column(name = "JOIN_KOSULU", length = 500)
    private String joinKosulu;

    @Column(name = "JOIN_TIPI")
    @Enumerated(EnumType.STRING)
    private EnumGnlJoinTipi joinTipi;

    @ColumnDefault("0")
    @Column(name = "SIRALAMA")
    private Integer siralama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlRaporEntityBaglanti other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}