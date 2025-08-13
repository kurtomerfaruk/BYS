package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYardimTuru;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "GNLKISI_ALDIGI_YARDIMLAR")
public class GnlKisiAldigiYardimlar extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2827965350075796354L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "GNLKISI_ID", nullable = false)
    private GnlKisi gnlKisi;

    @Column(name = "TANIM")
    @Enumerated(EnumType.STRING)
    private EnumGnlYardimTuru tanim;


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
        if (!(object instanceof GnlKisiAldigiYardimlar other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}