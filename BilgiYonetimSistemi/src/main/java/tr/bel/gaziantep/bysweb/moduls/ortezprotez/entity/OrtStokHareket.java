package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtStokHareketTur;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.10.2025 13:57
 */

@Getter
@Setter
@Entity
@Table(name = "ORTSTOK_HAREKET")
@NamedQuery(name = "OrtStokHareket.findByIslemIdByHareketTur", query = "SELECT s FROM OrtStokHareket s WHERE s.aktif=true AND s.islemId=:islemId " +
        "AND s.hareketTur = :hareketTur AND s.ortStok = :ortStok")
public class OrtStokHareket extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -8492524138021401375L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTSTOK_ID")
    private OrtStok ortStok;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @Column(name = "HAREKET_TUR")
    @Enumerated(EnumType.STRING)
    private EnumOrtStokHareketTur hareketTur;

    @Column(name = "DURUM")
    @Enumerated(EnumType.STRING)
    private EnumGirisCikis durum;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Column(name = "MIKTAR", precision = 18, scale = 2)
    private BigDecimal miktar;

    @Column(name = "ISLEM_ID")
    private Integer islemId;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtStokHareket other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}