package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SYYETKI")
public class SyYetki extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -3211208783080785130L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)

    @Column(name = "YETKI", length = 50)
    private String yetki;

    @Size(max = 250)

    @Column(name = "ACIKLAMA", length = 250)
    private String aciklama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SyYetki other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}