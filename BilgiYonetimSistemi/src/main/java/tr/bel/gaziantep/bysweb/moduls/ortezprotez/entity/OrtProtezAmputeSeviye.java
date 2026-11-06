package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
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
@Table(name = "ORTPROTEZ_AMPUTE_SEVIYE")
public class OrtProtezAmputeSeviye extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 5966094849969283820L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 150)
    @NotNull
    @Nationalized
    @Column(name = "TANIM", nullable = false, length = 150)
    private String tanim;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtProtezAmputeSeviye other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}