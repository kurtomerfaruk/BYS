package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStTedarikciTuru;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIl;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 14:13
 */
@Getter
@Setter
@Entity
@Table(name = "STTEDARIKCI")
public class StTedarikci extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -3801446318117282093L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TUR")
    private EnumStTedarikciTuru tur;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KOD", length = 50)
    private String kod;

    @Size(max = 200)
    @Nationalized
    @Column(name = "AD", length = 200)
    private String ad;

    @Size(max = 50)
    @Nationalized
    @Column(name = "VERGI_NO", length = 50)
    private String vergiNo;

    @Size(max = 150)
    @Nationalized
    @Column(name = "ILGILI_KISI", length = 150)
    private String ilgiliKisi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "TELEFON", length = 50)
    private String telefon;

    @Size(max = 150)
    @Nationalized
    @Column(name = "EPOSTA", length = 150)
    private String eposta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLIL_ID")
    private GnlIl gnlIl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLILCE_ID")
    private GnlIlce gnlIlce;

    @Nationalized
    @Lob
    @Column(name = "ADRES")
    private String adres;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StTedarikci other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}