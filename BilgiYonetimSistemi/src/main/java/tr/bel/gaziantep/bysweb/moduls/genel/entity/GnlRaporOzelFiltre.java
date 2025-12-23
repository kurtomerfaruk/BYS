package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 22.12.2025 16:20
 */
@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_OZEL_FILTRE")
public class GnlRaporOzelFiltre extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7114386352924597926L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Nationalized
    @Column(name = "FILTRE_ADI", length = 100)
    private String filtreAdi;

    @Nationalized
    @Lob
    @Column(name = "SQL_KOSULU")
    private String sqlKosulu;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "gnlRaporOzelFiltre", fetch = FetchType.LAZY)
    @SQLRestriction("AKTIF=true")
    private List<GnlRaporParametre> gnlRaporParametreList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlRaporOzelFiltre other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}