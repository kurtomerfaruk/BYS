package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYardimAlinanYerler;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.07.2025 10:08
 */
@Getter
@Setter
@Entity
@Table(name = "GNLKISI_YARDIM_ALINAN_YERLER")
public class GnlKisiYardimAlinanYerler extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7324748232868517292L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLKISI_ID")
    private GnlKisi gnlKisi;

    @Column(name = "TANIM")
    @Enumerated(EnumType.STRING)
    private EnumGnlYardimAlinanYerler tanim;


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
        if (!(object instanceof GnlKisiYardimAlinanYerler other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}