package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 26.11.2026 08:34
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ORTTESLIMAT")
public class OrtTeslimat extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -6463830715295350381L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTTALEP_ID")
    private OrtTalep ortTalep;

    @Column(name = "TESLIM_TARIHI")
    private LocalDateTime teslimTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TESLIM_EDEN_ORTPERSONEL_ID")
    private OrtPersonel teslimEden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TESLIM_ALAN_GNLKISI_ID")
    private GnlKisi teslimAlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KAYDEDEN_SYKULLANICI_ID")
    private SyKullanici kaydeden;

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
        if (!(object instanceof OrtTeslimat other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}