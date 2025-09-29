package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 26.09.2025 09:15
 */

@Getter
@Setter
@Entity
@Table(name = "ORTOLCU")
public class OrtOlcu extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 3900129679175092645L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTHASTA_ID")
    private OrtHasta ortHasta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTOLCU_SABLON_ID")
    private OrtOlcuSablon ortOlcuSablon;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtOlcu other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}