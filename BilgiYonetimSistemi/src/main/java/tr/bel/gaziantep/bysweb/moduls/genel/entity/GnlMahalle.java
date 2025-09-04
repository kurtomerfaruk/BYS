package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.locationtech.jts.geom.Polygon;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GNLMAHALLE")
@NamedQuery(name = "GnlMahalle.findByKod", query = "SELECT m FROM GnlMahalle m WHERE m.aktif=true AND m.kod=:kod")
@NamedQuery(name = "GnlMahalle.findByIlce", query = "SELECT m FROM GnlMahalle m WHERE m.aktif=true AND m.gnlIlce=:gnlIlce ORDER BY m.tanim")
public class GnlMahalle extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1820603637008975667L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLILCE_ID")
    private GnlIlce gnlIlce;

    @Column(name = "KOD")
    private Integer kod;

    @Size(max = 100)

    @Column(name = "TANIM", length = 100)
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
        if (!(object instanceof GnlMahalle other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}