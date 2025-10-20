package tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.evdeyasamadestek.EnumEdbBasvuruDurumu;
import tr.bel.gaziantep.bysweb.core.enums.evdeyasamadestek.EnumEdbHizmetTuru;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepTipi;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepTuru;

import java.io.Serial;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:54
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EDBTALEP")
public class EdbTalep extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -8761627206036549499L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @Enumerated(EnumType.STRING)
    @Column(name = "TALEP_TURU")
    private EnumGnlTalepTuru talepTuru;

    @Enumerated(EnumType.STRING)
    @Column(name = "TALEP_TIPI")
    private EnumGnlTalepTipi talepTipi;

    @Enumerated(EnumType.STRING)
    @Column(name = "TALEP_EDILEN_HIZMETLER")
    private EnumEdbHizmetTuru talepEdilenHizmetler;

    @Nationalized
    @Lob
    @Column(name = "DURUM_ACIKLAMA")
    private String durumAciklama;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Nationalized
    @Lob
    @Column(name = "SOSYAL_INCELEME_KARARI")
    private String sosyalIncelemeKarari;

    @Column(name = "INCELEME_TARIHI")
    private LocalDateTime incelemeTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name = "TALEP_DURUMU")
    private EnumEdbBasvuruDurumu talepDurumu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EDBBASVURU_ID")
    private EdbBasvuru edbBasvuru;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INCELEMEYI_YAPAN_EDBPERSONEL_ID")
    private EdbPersonel incelemeyiYapanEdbPersonel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EDBHIZMET_ID")
    private EdbHizmet edbHizmet;

    @ColumnDefault("0")
    @Column(name = "HIZMETE_EKLENSIN_MI", nullable = false)
    private boolean hizmeteEklensinMi;

    @ColumnDefault("0")
    @Column(name = "PLANLAMA_YAPILDI", nullable = false)
    private boolean planlamaYapildi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EdbTalep other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}