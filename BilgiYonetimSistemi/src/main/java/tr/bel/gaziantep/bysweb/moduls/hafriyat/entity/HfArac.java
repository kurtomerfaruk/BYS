package tr.bel.gaziantep.bysweb.moduls.hafriyat.entity;

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
@Table(name = "HFARAC")
public class HfArac extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 5416710428085408145L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "PLAKA", length = 50)
    private String plaka;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HFFIRMA_ID")
    private HfFirma hfFirma;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HfArac other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}