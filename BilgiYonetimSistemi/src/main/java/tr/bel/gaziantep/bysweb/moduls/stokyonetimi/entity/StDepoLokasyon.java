package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStDepoLokasyonTipi;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "STDEPO_LOKASYON")
public class StDepoLokasyon extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -1783824187718254197L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KOD", length = 50)
    private String kod;

    @Size(max = 150)
    @Nationalized
    @Column(name = "AD", length = 150)
    private String ad;

    @Enumerated(EnumType.STRING)
    @Column(name = "LOKASYON_TIPI")
    private EnumStDepoLokasyonTipi lokasyonTipi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_ID")
    private StDepo stDepo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UST_LOKASYON_ID")
    private StDepoLokasyon ustDepoLokasyon;

    @Column(name = "SIRA_NO")
    private Integer siraNo;

    @Size(max = 300)
    @Nationalized
    @Column(name = "ACIKLAMA", length = 300)
    private String aciklama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StDepoLokasyon other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}