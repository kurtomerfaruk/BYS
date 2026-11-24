package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtCihazTur;

import java.io.Serial;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 13:19
 */
@Getter
@Setter
@Entity
@Table(name = "ORTCIHAZ")
@NamedQuery(name = "OrtCihaz.findByCihazTur", query = "SELECT c FROM OrtCihaz c WHERE c.aktif=true AND c.cihazTur=:cihazTur ORDER BY c.tanim")
public class OrtCihaz extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 286886288517378434L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 250)
    @Nationalized
    @Column(name = "TANIM", length = 250)
    private String tanim;

    @Enumerated(EnumType.STRING)
    @Column(name = "CIHAZ_TUR")
    private EnumOrtCihazTur cihazTur;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtCihaz other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}