package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "SYGENEL_AYAR")
@NamedQuery(name = "SyGenelAyar.findByTanim",query = "SELECT g FROM SyGenelAyar g WHERE g.tanim=:tanim")
public class SyGenelAyar extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6578528371490278074L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 500)
    @Nationalized
    @Column(name = "TANIM", length = 500)
    private String tanim;

    @Size(max = 500)
    @Nationalized
    @Column(name = "DEGER", length = 500)
    private String deger;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SyGenelAyar other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}