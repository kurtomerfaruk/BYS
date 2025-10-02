package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 26.09.2025 09:15
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORTOLCU_DEGER")
public class OrtOlcuDeger extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 5945080744548067630L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTOLCU_ID")
    private OrtOlcu ortOlcu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTOLCU_SABLON_ALAN_ID")
    private OrtOlcuSablonAlan ortOlcuSablonAlan;

    @Nationalized
    @Lob
    @Column(name = "DEGER")
    private String deger;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtOlcuDeger other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}