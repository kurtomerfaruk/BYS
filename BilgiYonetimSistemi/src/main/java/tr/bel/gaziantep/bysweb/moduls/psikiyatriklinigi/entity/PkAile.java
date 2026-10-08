package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYakinlikDerecesi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 28.09.2026 08:25
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PKAILE")
public class PkAile extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -7424234879216811053L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "BASVURU_TARIHI")
    private LocalDateTime basvuruTarihi;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "GNLKISI_ID")
    private GnlKisi gnlKisi;

    @Enumerated(EnumType.STRING)
    @Column(name = "YAKINLIK_DERECESI")
    private EnumGnlYakinlikDerecesi yakinlikDerecesi;

    @Nationalized
    @Lob
    @Column(name = "YAKINLIK_DERECESI_ACIKLAMA")
    private String yakinlikDerecesiAciklama;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "PKHASTA_COCUKLUK_DONEMI_ID")
    private PkHastaCocuklukDonemi pkHastaCocuklukDonemi;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "PKHASTA_AILE_ICI_ILISKI_ID")
    private PkHastaAileIciIliski pkHastaAileIciIliski;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "PKHASTA_ADLI_SICIL_ID")
    private PkHastaAdliSicil pkHastaAdliSicil;

    @OneToMany(mappedBy = "pkAile", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    private List<PkHasta> pkHastaList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkAile other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}