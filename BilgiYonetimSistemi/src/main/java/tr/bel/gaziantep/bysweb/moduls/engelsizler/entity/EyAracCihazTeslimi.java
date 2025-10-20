package tr.bel.gaziantep.bysweb.moduls.engelsizler.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyCihazDurum;

import java.io.Serial;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 28.06.2025 08:32
 */
@Getter
@Setter
@Entity
@Table(name = "EYARAC_CIHAZ_TESLIMI")
@NamedQuery(name = "EyAracCihazTeslimi.findByGnlKisi", query = "SELECT e FROM EyAracCihazTeslimi e WHERE e.aktif=true " +
        "AND e.eyKisi.gnlKisi=:gnlKisi ORDER BY e.verilisTarihi DESC")
public class EyAracCihazTeslimi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 5393561827846136033L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EYKISI_ID", nullable = false)
    private EyKisi eyKisi;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EYARAC_ID", nullable = false)
    private EyArac eyArac;

    @Lob
    @Nationalized
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Column(name = "VERILIS_TARIHI")
    private LocalDateTime verilisTarihi;

    @Column(name = "CIHAZ_DURUM")
    @Enumerated(EnumType.STRING)
    private EnumEyCihazDurum cihazDurum;

    @Nationalized
    @Column(name = "GERI_ALIM_ACIKLAMASI")
    private String geriAlimAciklamasi;

    @Column(name = "GERI_ALIM_TARIHI")
    private LocalDateTime geriAlimTarihi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EyAracCihazTeslimi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}