package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStLokasyonTipi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 10:52
 */
@Getter
@Setter
@Entity
@Table(name = "STDEPO_LOKASYON")
public class StDepoLokasyon extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -8488937936393807435L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "STDEPO_ID", nullable = false)
    private StDepo stDepo;

    @Size(max = 50)
    @Nationalized
    @Column(name = "LOKASYON_KODU", length = 50)
    private String lokasyonKodu;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KORIDOR", length = 50)
    private String koridor;

    @Size(max = 50)
    @Nationalized
    @Column(name = "RAF", length = 50)
    private String raf;

    @Size(max = 50)
    @Nationalized
    @Column(name = "BOLUM", length = 50)
    private String bolum;

    @Size(max = 50)
    @Nationalized
    @Column(name = "GOZ_NO", length = 50)
    private String gozNo;

    @Size(max = 30)
    @Nationalized
    @Enumerated(EnumType.STRING)
    @Column(name = "LOKASYON_TIPI", length = 30)
    private EnumStLokasyonTipi lokasyonTipi;

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