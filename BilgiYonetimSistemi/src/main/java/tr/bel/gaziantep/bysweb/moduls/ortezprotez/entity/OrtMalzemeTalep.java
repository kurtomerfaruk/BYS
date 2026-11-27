package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtMalzemeDurumu;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 25.11.2026 16:03
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORTMALZEME_TALEP")
public class OrtMalzemeTalep extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -4875414233536107273L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTTALEP_ID")
    private OrtTalep ortTalep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TALEP_EDEN_ORTPERSONEL_ID")
    private OrtPersonel talepEdenOrtPersonel;

    @Column(name = "TALEP_TARIHI")
    private LocalDateTime talepTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name = "DURUM")
    private EnumOrtMalzemeDurumu durum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ONAYLAYAN_SYKULLANICI_ID")
    private SyKullanici onaylayan;

    @Column(name = "ONAY_TARIHI")
    private LocalDateTime onayTarihi;

    @Nationalized
    @Lob
    @Column(name = "RED_GEREKCESI")
    private String redGerekcesi;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "ortMalzemeTalep", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @SQLRestriction("AKTIF = true")
    @Builder.Default
    private List<OrtMalzemeTalepDetay> detayList = new ArrayList<>();

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