package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;

import java.io.Serial;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 15:52
 */
@Getter
@Setter
@Entity
@Table(name = "ORTPERSONEL")
public class OrtPersonel extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -5266999115290770699L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLPERSONEL_ID")
    private GnlPersonel gnlPersonel;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtPersonel other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}