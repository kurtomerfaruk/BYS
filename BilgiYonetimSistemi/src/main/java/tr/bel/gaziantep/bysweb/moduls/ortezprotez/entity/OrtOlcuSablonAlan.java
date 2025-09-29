package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtSablonAlanTuru;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 26.09.2025 09:15
 */

@Getter
@Setter
@Entity
@Table(name = "ORTOLCU_SABLON_ALAN")
@NamedQuery(name = "OrtOlcuSablonAlan.findByOrtOlcuSablonId",query = "SELECT o FROM OrtOlcuSablonAlan o WHERE o.aktif=true AND o.ortOlcuSablon.id =:ortOlcuSablonId")
public class OrtOlcuSablonAlan extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -802767522540801137L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTOLCU_SABLON_ID")
    private OrtOlcuSablon ortOlcuSablon;

    @Size(max = 50)
    @Nationalized
    @Column(name = "TANIM", length = 50)
    private String tanim;

    @Column(name = "X")
    private Integer x;

    @Column(name = "Y")
    private Integer y;

    @Column(name = "GENISLIK")
    private Integer genislik;

    @Column(name = "YUKSEKLIK")
    private Integer yukseklik;

    @Column(name = "TUR")
    @Enumerated(EnumType.STRING)
    private EnumOrtSablonAlanTuru tur;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ETIKET", length = 50)
    private String etiket;

    @Nationalized
    @Lob
    @Column(name = "SECENEK")
    private String secenek;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtOlcuSablonAlan other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}