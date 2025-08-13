package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "GNLBANKA")
public class GnlBanka extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7733305451498837681L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KOD", length = 50)
    private String kod;

    @Size(max = 50)
    @Nationalized
    @Column(name = "BANKA_ADI", length = 50)
    private String bankaAdi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SUBE", length = 50)
    private String sube;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SUBE_KOD", length = 50)
    private String subeKod;

    @Size(max = 255)
    @Nationalized
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
    @Column(name = "FAKS", length = 50)
    private String faks;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlBanka other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}