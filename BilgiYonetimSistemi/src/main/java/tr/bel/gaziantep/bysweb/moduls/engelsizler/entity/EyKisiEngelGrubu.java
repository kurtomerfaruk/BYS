package tr.bel.gaziantep.bysweb.moduls.engelsizler.entity;

import jakarta.persistence.*;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 28.06.2025 08:32
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EYKISI_ENGEL_GRUBU")
@NamedQuery(name = "EyKisiEngelGrubu.getEyEngelGrubuByEyKisi",query = "SELECT e.eyEngelGrubu FROM EyKisiEngelGrubu e WHERE e.aktif=true AND e.secili=true AND e.eyKisi = :eyKisi")
public class EyKisiEngelGrubu extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2182641467628477839L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYKISI_ID")
    private EyKisi eyKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYENGEL_GRUBU_ID")
    private EyEngelGrubu eyEngelGrubu;

    @Column(name = "SECILI")
    private boolean secili;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EyKisiEngelGrubu other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}