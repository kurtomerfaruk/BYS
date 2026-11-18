package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 16:10
 */
@Getter
@Setter
@Entity
@Table(name = "STALIS_KALEM")
public class StAlisKalem extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -2036824748247018721L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STALIS_ID")
    private StAlis stAlis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSTOK_ID")
    private StStok stStok;

    @Column(name = "MIKTAR", precision = 18, scale = 4)
    private BigDecimal miktar;

    @Column(name = "BIRIM_MALIYET", precision = 18, scale = 4)
    private BigDecimal birimMaliyet;

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
        if (!(object instanceof StAlisKalem other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}