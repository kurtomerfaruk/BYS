package tr.bel.gaziantep.bysweb.moduls.moralevi.entity;

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
@Table(name = "MEETKINLIK_RESIM")
public class MeEtkinlikResim extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -1668660642893750372L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEETKINLIK_ID")
    private MeEtkinlik meEtkinlik;

    @Size(max = 150)
    @Nationalized
    @Column(name = "RESIM_ADI", length = 150)
    private String resimAdi;

    @Size(max = 150)
    @Nationalized
    @Column(name = "RESIM_YOLU", length = 150)
    private String resimYolu;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MeEtkinlikResim other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}