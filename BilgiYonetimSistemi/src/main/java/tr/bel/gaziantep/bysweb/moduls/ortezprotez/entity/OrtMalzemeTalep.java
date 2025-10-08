package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtMalzemeOnayDurumu;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.10.2025 13:53
 */
@Getter
@Setter
@Entity
@Table(name = "ORTMALZEME_TALEP")
public class OrtMalzemeTalep extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -4182897377510927161L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTBASVURU_ID")
    private OrtBasvuru ortBasvuru;

    @Column(name = "TALEP_TARIHI")
    private LocalDateTime talepTarihi;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TALEP_EDEN_ORTPERSONEL_ID")
    private OrtPersonel talepEdenOrtPersonel;

    @Column(name = "ONAY_TARIHI")
    private LocalDateTime onayTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ONAYLAYAN_ORTPERSONEL_ID")
    private OrtPersonel onaylayanOrtPersonel;

    @Column(name = "RED_TARIHI")
    private LocalDateTime redTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REDEDEN_ORTPERSONEL_ID")
    private OrtPersonel rededenOrtPersonel;

    @Nationalized
    @Lob
    @Column(name = "RED_SEBEBI")
    private String redSebebi;

    @ColumnDefault("0")
    @Column(name = "TESLIM_EDILDI")
    private boolean teslimEdildi;

    @Column(name = "TESLIM_TARIHI")
    private LocalDateTime teslimTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TESLIM_ALAN_ORTPERSONEL_ID")
    private OrtPersonel teslimAlanPersonel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TESLIM_EDEN_ORTPERSONEL_ID")
    private OrtPersonel teslimEdenPersonel;

    @Column(name = "DURUM")
    @Enumerated(EnumType.STRING)
    private EnumOrtMalzemeOnayDurumu durum;

    @OneToMany(mappedBy = "ortMalzemeTalep", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<OrtMalzemeTalepStok> ortMalzemeTalepStokList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtMalzemeTalep other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}