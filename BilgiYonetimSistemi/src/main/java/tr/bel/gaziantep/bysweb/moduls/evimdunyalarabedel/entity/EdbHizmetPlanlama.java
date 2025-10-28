package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:54
 */
@Getter
@Setter
@Entity
@Table(name = "EDBHIZMET_PLANLAMA")
public class EdbHizmetPlanlama extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -4862098331191341076L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EDBTALEP_ID")
    private EdbTalep edbTalep;

    @Column(name = "BASLANGIC_TARIHI")
    private LocalDateTime baslangicTarihi;

    @Column(name = "PERIYOT")
    private Integer periyot;

    @ColumnDefault("0")
    @Column(name = "HIZMETLERE_EKLENDI", nullable = false)
    private boolean hizmetlereEklendi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EdbHizmetPlanlama other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}