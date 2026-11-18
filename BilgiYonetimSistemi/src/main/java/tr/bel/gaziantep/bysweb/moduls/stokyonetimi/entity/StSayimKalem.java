package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 10:41
 */
@Getter
@Setter
@Entity
@Table(name = "STSAYIM_KALEM")
public class StSayimKalem extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -1785722648664901079L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSAYIM_ID")
    private StSayim stSayim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSTOK_ID")
    private StStok stStok;

    @Column(name = "SISTEM_MIKTARI", precision = 18, scale = 4)
    private BigDecimal sistemMiktari;

    @Column(name = "SAYILAN_MIKTAR", precision = 18, scale = 4)
    private BigDecimal sayilanMiktar;

    @Column(name = "FARK", precision = 18, scale = 4)
    private BigDecimal fark;

    @Size(max = 300)
    @Nationalized
    @Column(name = "ACIKLAMA", length = 300)
    private String aciklama;


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StSayimKalem other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}