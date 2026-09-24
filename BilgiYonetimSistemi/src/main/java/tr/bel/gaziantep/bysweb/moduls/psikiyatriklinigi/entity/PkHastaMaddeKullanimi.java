package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 24.09.2026 13:26
 */
@Getter
@Setter
@Entity
@Table(name = "PKHASTA_MADDE_KULLANIMI")
public class PkHastaMaddeKullanimi extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -3971382037521323745L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKHASTA_ID")
    private PkHasta pkHasta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKMADDE_ID")
    private PkMadde pkMadde;

    @ColumnDefault("0")
    @Column(name = "SECILI", nullable = false)
    private boolean secili;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkHastaMaddeKullanimi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }


}