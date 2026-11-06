package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.27.0
 * @since 6.11.2026 09:43
 */
@Getter
@Setter
@Entity
@Table(name = "ORTHASTA_PROTEZ_AMPUTE_SEVIYE")
public class OrtHastaProtezAmputeSeviye extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6922873847477702803L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTHASTA_ID")
    private OrtHasta ortHasta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTPROTEZ_AMPUTE_SEVIYE_ID")
    private OrtProtezAmputeSeviye ortProtezAmputeSeviye;

    @ColumnDefault("0")
    @Column(name = "SECILI", nullable = false)
    private boolean secili;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtHastaProtezAmputeSeviye other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}