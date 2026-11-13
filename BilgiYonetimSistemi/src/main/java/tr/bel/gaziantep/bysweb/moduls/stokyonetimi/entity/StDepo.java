package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStDepoTipi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 09:49
 */
@Getter
@Setter
@Entity
@Table(name = "STDEPO")
public class StDepo extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6909272856427926911L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Nationalized
    @Column(name = "DEPO_KODU", nullable = false, length = 30)
    private String depoKodu;

    @Size(max = 150)
    @NotNull
    @Nationalized
    @Column(name = "DEPO_ADI", nullable = false, length = 150)
    private String depoAdi;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEPO_TIPI", nullable = false, length = 30)
    private EnumStDepoTipi depoTipi;

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