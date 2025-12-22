package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlRaporParametreTipi;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyVeriTipi;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.12.2025 13:33
 */
@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_PARAMETRE")
public class GnlRaporParametre extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -5217815469027841446L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_ID")
    private GnlRapor gnlRapor;

    @Size(max = 50)
    @Nationalized
    @Column(name = "PARAMETRE_ADI", length = 50)
    private String parametreAdi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "GORUNUR_ADI", length = 50)
    private String gorunurAdi;

    @Column(name = "PARAMETRE_TIPI")
    @Enumerated(EnumType.STRING)
    private EnumGnlRaporParametreTipi parametreTipi;

    @Column(name = "VERI_TIPI")
    @Enumerated(EnumType.STRING)
    private EnumSyVeriTipi veriTipi;

    @ColumnDefault("0")
    @Column(name = "ZORUNLU")
    private boolean zorunlu;

    @Size(max = 200)
    @Nationalized
    @Column(name = "VARSAYILAN_DEGER", length = 200)
    private String varsayilanDeger;

    @Size(max = 50)
    @Nationalized
    @Column(name = "VARSAYILAN_OPERATOR", length = 50)
    private String varsayilanOperator;

    @Size(max = 500)
    @Nationalized
    @Column(name = "SQL_KOSUL", length = 500)
    private String sqlKosul;

    @Size(max = 500)
    @Nationalized
    @Column(name = "LOOKUP_QUERY", length = 500)
    private String lookupQuery;

    @Size(max = 500)
    @Nationalized
    @Column(name = "LOOKUP_ENUM_CLASS", length = 500)
    private String lookupEnumClass;

    @ColumnDefault("0")
    @Column(name = "SIRALAMA")
    private Integer siralama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlRaporParametre other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}