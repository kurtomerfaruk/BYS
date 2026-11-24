package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepTipi;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 15:52
 */
@Getter
@Setter
@Entity
@Table(name = "ORTTALEP")
public class OrtTalep extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -8924939957507784277L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 30)
    @Nationalized
    @Column(name = "TALEP_NO", length = 30)
    private String talepNo;

    @Column(name = "TALEP_TARIHI")
    private LocalDateTime talepTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTKISI_ID")
    private OrtKisi ortKisi;

    @Enumerated(EnumType.STRING)
    @Column(name = "TALEP_TIPI")
    private EnumOrtTalepTipi talepTipi;

    @Enumerated(EnumType.STRING)
    @Column(name = "DURUM")
    private EnumOrtTalepDurumu durum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEKNIKER_ORTPERSONEL_ID")
    private OrtPersonel teknikerOrtPersonel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIZYOTERAPIST_ORTPERSONEL_ID")
    private OrtPersonel fizyoterapistOrtPersonel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTPROJE_ID")
    private OrtProje ortProje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTSUT_KODU_ID")
    private OrtSutKodu ortSutKodu;

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
        if (!(object instanceof OrtTalep other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}