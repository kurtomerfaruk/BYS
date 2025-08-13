package tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity;

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
@Table(name = "AYETKINLIK_KISI")
public class AyEtkinlikKisi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 3568802958557509832L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AYETKINLIK_ID")
    private AyEtkinlik ayEtkinlik;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AYKISI_ID")
    private AyKisi ayKisi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AyEtkinlikKisi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}