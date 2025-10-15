package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumAlisSatis;

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
@Table(name = "ORTSTOK_FIYAT_HAREKET")
public class OrtStokFiyatHareket extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6841113812686632701L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTSTOK_ID")
    private OrtStok ortStok;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @Column(name = "TUR")
    @Enumerated(EnumType.STRING)
    private EnumAlisSatis birim;

    @Column(name = "FIYAT", precision = 18, scale = 2)
    private BigDecimal fiyat;


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtStokFiyatHareket other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}