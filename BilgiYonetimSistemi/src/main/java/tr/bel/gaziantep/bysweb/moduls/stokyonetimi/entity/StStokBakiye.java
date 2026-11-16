package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 10:55
 */
@Getter
@Setter
@Entity
@Table(name = "STSTOK_BAKIYE")
public class StStokBakiye extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -309496208265333904L;

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
    @JoinColumn(name = "STDEPO_LOKASYON_ID")
    private StDepoLokasyon stDepoLokasyon;

    @Column(name = "SON_KULLANMA_TARIHI")
    private LocalDate sonKullanmaTarihi;

    @Column(name = "MIKTAR", precision = 18, scale = 4)
    private BigDecimal miktar;

    @Column(name = "REZERVE_MIKTAR", precision = 18, scale = 4)
    private BigDecimal rezerveMiktar;

    @Column(name = "TRANSFERDEKI_MIKTAR", precision = 18, scale = 4)
    private BigDecimal transferdekiMiktar;

    @Column(name = "ORTALAMA_MALIYET", precision = 18, scale = 4)
    private BigDecimal ortalamaMaliyet;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StStokBakiye other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}