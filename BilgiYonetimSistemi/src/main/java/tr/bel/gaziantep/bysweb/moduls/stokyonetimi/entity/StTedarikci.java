package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIl;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 15:41
 */
@Getter
@Setter
@Entity
@Table(name = "STTEDARIKCI")
public class StTedarikci extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 2374322936363427770L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "TEDARIKCI_KODU", length = 50)
    private String tedarikciKodu;

    @Size(max = 150)
    @Nationalized
    @Column(name = "TEDARIKCI_ADI", length = 150)
    private String tedarikciAdi;

    @Size(max = 20)
    @Nationalized
    @Column(name = "VERGI_NO", length = 20)
    private String vergiNo;

    @Nationalized
    @Lob
    @Column(name = "ADRES")
    private String adres;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLIL_ID")
    private GnlIl gnlIl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLILCE_ID")
    private GnlIlce gnlIlce;

    @Size(max = 50)
    @Nationalized
    @Column(name = "TELEFON", length = 50)
    private String telefon;

    @Size(max = 50)
    @Nationalized
    @Column(name = "GSM", length = 50)
    private String gsm;

    @Size(max = 200)
    @Nationalized
    @Column(name = "EPOSTA", length = 200)
    private String eposta;

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