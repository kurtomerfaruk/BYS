package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlBirim;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 09:33
 */
@Getter
@Setter
@Entity
@Table(name = "STDEPO")
public class StDepo extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2924233349296310805L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAIRE_BASKANLIGI_GNLBIRIM_ID")
    private GnlBirim daireBaskanligi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBE_MUDURLUGU_GNLBIRIM_ID")
    private GnlBirim subeMudurlugu;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KOD", length = 50)
    private String kod;

    @Size(max = 150)
    @Nationalized
    @Column(name = "AD", length = 150)
    private String ad;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StDepo other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}