package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.locationtech.jts.geom.Polygon;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.07.2025 10:08
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GNLIL")
@NamedQuery(name = "GnlIl.findByKod",query = "SELECT i FROM GnlIl i WHERE i.aktif=true AND i.ilKodu=:kod")
public class GnlIl extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -1402323434119608270L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "IL_KODU")
    private Integer ilKodu;

    @Size(max = 2)

    @Column(name = "PLAKA", length = 2)
    private String plaka;

    @Size(max = 50)

    @Column(name = "TANIM", length = 50)
    @Nationalized
    private String tanim;

    @Column(name = "GEOMETRY", columnDefinition = "geometry")
    private Polygon geometry;

/*
 TODO [Reverse Engineering] create field to map the 'GEOMETRY' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "GEOMETRY", columnDefinition = "geometry")
    private Object geometry;
*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlIl other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}