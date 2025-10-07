package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtMalzemeOnayDurumu;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.10.2025 10:02
 */

@Getter
@Setter
@Entity
@Table(name = "ORTBASVURU_MALZEME_TALEP")
public class OrtBasvuruMalzemeTalep extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -200950588565030717L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTBASVURU_ID")
    private OrtBasvuru ortBasvuru;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTSTOK_ID")
    private OrtStok ortStok;

    @Column(name = "MIKTAR", precision = 18, scale = 2)
    private BigDecimal miktar;

    @Column(name = "ONAY_TARIHI")
    private LocalDateTime onayTarihi;

    @Column(name = "TALEP_TARIHI")
    private LocalDateTime talepTarihi;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TALEP_EDEN_ORTPERSONEL_ID")
    private OrtPersonel talepEdenOrtPersonel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ONAYLAYAN_ORTPERSONEL_ID")
    private OrtPersonel onaylayanOrtPersonel;

    @Column(name = "DURUM")
    @Enumerated(EnumType.STRING)
    private EnumOrtMalzemeOnayDurumu durum;

    @Nationalized
    @Lob
    @Column(name = "RED_SEBEBI")
    private String redSebebi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtBasvuruMalzemeTalep other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}