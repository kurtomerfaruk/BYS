package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKurs;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 8.12.2025 08:33
 */
@Getter
@Setter
@Entity
@Table(name = "EKMKURSIYER_KURS")
public class EkmKursiyerKurs extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 2845149834978335707L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EKMKURSIYER_ID")
    private EkmKursiyer ekmKursiyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLKURS_ID")
    private GnlKurs gnlKurs;

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
        if (!(object instanceof EkmKursiyerKurs other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}