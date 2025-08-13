package tr.bel.gaziantep.bysweb.moduls.hafriyat.entity;

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
@Table(name = "HFHAFRIYAT_IS_ARAC")
public class HfHafriyatIsArac extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2409463861872790512L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HFHAFRIYAT_IS_ID")
    private HfHafriyatIs hfHafriyatIs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HFFIRMA_ID")
    private HfFirma hfFirma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HFARAC_ID")
    private HfArac hfArac;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HfHafriyatIsArac other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}