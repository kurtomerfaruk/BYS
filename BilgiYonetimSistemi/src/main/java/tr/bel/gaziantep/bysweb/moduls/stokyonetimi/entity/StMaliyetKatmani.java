package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 11:06
 */
@Getter
@Setter
@Entity
@Table(name = "STMALIYET_KATMANI")
public class StMaliyetKatmani extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2652178583253469613L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSTOK_ID")
    private StStok stStok;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_ID")
    private StDepo stDepo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GIRIS_HAREKET_ID")
    private StStokHareket girisHareket;

    @Column(name = "GIRIS_TARIHI")
    private LocalDateTime girisTarihi;

    @Column(name = "MIKTAR", precision = 18, scale = 4)
    private BigDecimal miktar;

    @Column(name = "KALAN_MIKTAR", precision = 18, scale = 4)
    private BigDecimal kalanMiktar;

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
        if (!(object instanceof StMaliyetKatmani other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}