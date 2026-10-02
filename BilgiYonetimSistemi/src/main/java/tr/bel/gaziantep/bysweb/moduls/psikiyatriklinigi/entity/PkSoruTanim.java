package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 02.10.2026 09:08
 */
@Getter
@Setter
@Entity
@Table(name = "PKSORU_TANIM")
public class PkSoruTanim extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -7801985813694453905L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKSORU_TUR_ID")
    private PkSoruTur pkSoruTur;

    @Size(max = 150)
    @NotNull
    @Nationalized
    @Column(name = "TANIM", nullable = false, length = 150)
    private String tanim;

    @Column(name = "SIRA_NO")
    private Integer siraNo;

    @ColumnDefault("0")
    @Column(name = "ACIKLAMA_GEREKLI")
    private boolean aciklamaGerekli;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkSoruTanim other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}