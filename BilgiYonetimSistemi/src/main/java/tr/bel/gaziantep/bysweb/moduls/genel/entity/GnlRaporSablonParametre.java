package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.12.2025 13:33
 */
@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_SABLON_PARAMETRE")
@NamedQuery(name = "GnlRaporSablonParametre.findBySablon",query = "SELECT s FROM GnlRaporSablonParametre s WHERE s.aktif=true " +
        "AND s.gnlRaporKullaniciRaporSablon.id =:sablonId ")
public class GnlRaporSablonParametre extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -8360968437372984961L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_KULLANICI_RAPOR_SABLON_ID")
    private GnlRaporKullaniciRaporSablon gnlRaporKullaniciRaporSablon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_PARAMETRE_ID")
    private GnlRaporParametre gnlRaporParametre;

    @Size(max = 50)
    @Nationalized
    @Column(name = "DEGER", length = 50)
    private String deger;

    @Size(max = 50)
    @Nationalized
    @Column(name = "OPERATOR", length = 50)
    private String operator;

    @Size(max = 500)
    @Nationalized
    @Column(name = "IKINCI_DEGER", length = 500)
    private String ikinciDeger;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlRaporSablonParametre other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}