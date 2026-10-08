package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 1.10.2026 12:06
 */
@Getter
@Setter
@Entity
@Table(name = "PKHEMSIRE_GOZLEM_FORMU")
public class PkHemsireGozlemFormu extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -5037650761102294932L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKHASTA_ID")
    private PkHasta pkHasta;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KARANTINA_NO", length = 50)
    private String karantinaNo;

    @Nationalized
    @Lob
    @Column(name = "SERVIS")
    private String servis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOKTOR_PKPERSONEL_ID")
    private PkPersonel doktorPkPersonel;

    @Nationalized
    @Lob
    @Column(name = "TESHIS")
    private String teshis;

    @Nationalized
    @Lob
    @Column(name = "TESHIS_ORDER")
    private String teshisOrder;

    @Size(max = 10)
    @Nationalized
    @Column(name = "SAAT", length = 10)
    private String saat;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ATES", length = 50)
    private String ates;

    @Size(max = 50)
    @Nationalized
    @Column(name = "NABIZ", length = 50)
    private String nabiz;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KAN_BASINCI", length = 50)
    private String kanBasinci;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SPO2", length = 50)
    private String spo2;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SOLUNUM", length = 50)
    private String solunum;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KAN_SEKERI", length = 50)
    private String kanSekeri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UYGULAYAN_HEMSIRE_PKPERSONEL_ID")
    private PkPersonel uygulayanHemsirePkPersonel;

    @Nationalized
    @Lob
    @Column(name = "CV")
    private String cv;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkHemsireGozlemFormu other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}