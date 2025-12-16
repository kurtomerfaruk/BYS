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
 * @since 10.12.2025 13:33
 */
@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_MODUL")
public class GnlRaporModul extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -3424431238659439869L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Nationalized
    @Column(name = "MODUL_ADI", length = 100)
    private String modulAdi;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ANA_ENTITY", length = 100)
    private String anaEntity;

    @Nationalized
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "gnlRaporModul", fetch = FetchType.LAZY)
    @SQLRestriction("AKTIF=true")
    private List<GnlRaporKolon> gnlRaporKolonList = new ArrayList<>();

    @OneToMany(mappedBy = "gnlRaporModul", fetch = FetchType.LAZY)
    @SQLRestriction("AKTIF=true")
    private List<GnlRaporParametre> gnlRaporParametreList = new ArrayList<>();

    @OneToMany(mappedBy = "gnlRaporModul", fetch = FetchType.LAZY)
    @SQLRestriction("AKTIF=true")
    private List<GnlRaporModulEntityBaglanti> gnlRaporModulEntityBaglantiList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlRaporModul other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}