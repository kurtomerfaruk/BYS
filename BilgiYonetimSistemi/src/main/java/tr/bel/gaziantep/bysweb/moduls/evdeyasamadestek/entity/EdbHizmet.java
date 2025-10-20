package tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.evdeyasamadestek.EnumEdbHizmetTuru;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:54
 */
@Getter
@Setter
@Entity
@Table(name = "EDBHIZMET")
@NamedQuery(name = "EdbHizmet.findByHizmetTuru", query = "SELECT e FROM EdbHizmet e WHERE e.aktif=true AND e.hizmetTuru=:hizmetTuru")
public class EdbHizmet extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -8461689179353719160L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "HIZMET_TURU")
    @Enumerated(EnumType.STRING)
    private EnumEdbHizmetTuru hizmetTuru;

    @Size(max = 50)
    @Nationalized
    @Column(name = "TANIM", length = 50)
    private String tanim;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EdbHizmet other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}