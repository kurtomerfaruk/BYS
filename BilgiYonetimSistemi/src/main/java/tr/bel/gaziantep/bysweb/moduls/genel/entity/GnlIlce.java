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
@Table(name = "GNLILCE")
@NamedQuery(name = "GnlIlce.findByKod",query = "SELECT i FROM GnlIlce i WHERE i.aktif=true AND i.ilceKodu=:kod")
@NamedQuery(name = "GnlIlce.findByIlId",query = "SELECT i FROM GnlIlce i WHERE i.aktif=true AND i.gnlIl.ilKodu=:ilId")
public class GnlIlce extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -5819692747126912354L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLIL_ID")
    private GnlIl gnlIl;

    @Column(name = "ILCE_KODU")
    private Integer ilceKodu;

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
        if (!(object instanceof GnlIlce other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}