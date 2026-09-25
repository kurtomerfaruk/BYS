package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkTedaviSekli;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 25.09.2026 10:42
 */
@Getter
@Setter
@Entity
@Table(name = "PKHASTA_TEDAVI_SEKLI")
public class PkHastaTedaviSekli extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6646376685212936688L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKHASTA_ID")
    private PkHasta pkHasta;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @Enumerated(EnumType.STRING)
    @Column(name = "TEDAVI_SEKLI")
    private EnumPkTedaviSekli tedaviSekli;

    @Column(name = "TABURCU_TARIHI")
    private LocalDateTime taburcuTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKTABURCU_NEDENI_ID")
    private PkTaburcuNedeni pkTaburcuNedeni;

    @Nationalized
    @Lob
    @Column(name = "TABURCU_ACIKLAMA")
    private String taburcuAciklama;

    @Column(name = "HIZMET_SURESI")
    private Integer hizmetSuresi;


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkHastaTedaviSekli other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}