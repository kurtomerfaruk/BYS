package tr.bel.gaziantep.bysweb.moduls.moralevi.entity;

import jakarta.persistence.*;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MEETKINLIK_KISI")
@NamedQuery(name = "MeEtkinlikKisi.findByGnlKisi", query = "SELECT m FROM MeEtkinlikKisi m WHERE m.aktif=true " +
        "AND m.meEtkinlik.aktif=true AND m.meKisi.eyKisi.gnlKisi=:gnlKisi ORDER BY m.meEtkinlik.tarih DESC")
public class MeEtkinlikKisi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 726432974288220150L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEETKINLIK_ID")
    private MeEtkinlik meEtkinlik;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEKISI_ID")
    private MeKisi meKisi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MeEtkinlikKisi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}