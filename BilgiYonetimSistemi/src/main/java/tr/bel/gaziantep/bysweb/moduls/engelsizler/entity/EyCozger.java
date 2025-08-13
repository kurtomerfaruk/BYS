package tr.bel.gaziantep.bysweb.moduls.engelsizler.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "EYCOZGER")
public class EyCozger extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6740961370830562582L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "KOD", columnDefinition = "tinyint not null")
    private Short kod;

    @Size(max = 100)
    @Column(name = "DUZEY", length = 100)
    private String duzey;

    @Size(max = 20)
    @Column(name = "ENGEL_ORANI", length = 20)
    private String engelOrani;

    @Size(max = 10)
    @Column(name = "DERECESI", length = 10)
    private String derecesi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EyCozger other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}