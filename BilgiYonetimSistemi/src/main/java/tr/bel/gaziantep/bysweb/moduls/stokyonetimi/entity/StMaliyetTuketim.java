package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 19.11.2026 16:00
 */
@Getter
@Setter
@Entity
@Table(name = "STMALIYET_TUKETIM")
public class StMaliyetTuketim extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 6365856877813648689L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STMALIYET_KATMANI_ID")
    private StMaliyetKatmani stMaliyetKatmani;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSTOK_HAREKET_ID")
    private StStokHareket stStokHareket;

    @Column(name = "TUKETILEN_MIKTAR", precision = 18, scale = 4)
    private BigDecimal tuketilenMiktar;

    @Column(name = "BIRIM_MALIYET", precision = 18, scale = 4)
    private BigDecimal birimMaliyet;

    @Column(name = "TOPLAM_MALIYET", precision = 18, scale = 4)
    private BigDecimal toplamMaliyet;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StMaliyetTuketim other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}