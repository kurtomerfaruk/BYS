package tr.bel.gaziantep.bysweb.moduls.hafriyat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.hafriyat.EnumHfMalCinsi;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.07.2025 11:19
 */
@Getter
@Setter
@Entity
@Table(name = "HFDEPOLAMA_TESISI_IS")
public class HfDepolamaTesisiIs extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -3712772701678853926L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HFARAC_ID")
    private HfArac hfArac;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HFFIRMA_ID")
    private HfFirma hfFirma;

    @Column(name = "MAL_CINSI")
    @Enumerated(EnumType.STRING)
    private EnumHfMalCinsi malCinsi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "GELDIGI_YER", length = 50)
    private String geldigiYer;

    @Size(max = 50)
    @Nationalized
    @Column(name = "GITTIGI_YER", length = 50)
    private String gittigiYer;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SOFOR", length = 50)
    private String sofor;

    @Column(name = "MIKTAR", precision = 18, scale = 2)
    private BigDecimal miktar;

    @Column(name = "FIYAT", precision = 18, scale = 2)
    private BigDecimal fiyat;

    @Column(name = "TUTAR", precision = 18, scale = 2)
    private BigDecimal tutar;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HfDepolamaTesisiIs other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}