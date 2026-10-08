package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 05.10.2026 10:07
 */
@Getter
@Setter
@Entity
@Table(name = "PKVERILEN_HIZMET")
public class PkVerilenHizmet extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -6769353479567007177L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKHIZMET_TUR_ID")
    private PkHizmetTur pkHizmetTur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKHASTA_ID")
    private PkHasta pkHasta;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

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
        if (!(object instanceof PkVerilenHizmet other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}