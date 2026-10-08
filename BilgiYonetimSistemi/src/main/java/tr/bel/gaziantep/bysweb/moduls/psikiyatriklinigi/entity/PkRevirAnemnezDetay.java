package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 1.10.2026 09:03
 */
@Getter
@Setter
@Entity
@Table(name = "PKREVIR_ANEMNEZ_DETAY")
public class PkRevirAnemnezDetay extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -1087490571019630292L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKREVIR_ANEMNEZ_ID")
    private PkRevirAnemnez pkRevirAnemnez;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKSORU_TANIM_ID")
    private PkSoruTanim pkSoruTanim;

    @Size(max = 250)
    @Nationalized
    @Column(name = "DIGER_ACIKLAMA", length = 250)
    private String digerAciklama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkRevirAnemnezDetay other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}