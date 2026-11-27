package tr.bel.gaziantep.bysweb.moduls.tabanlik.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:25
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TABOLCUM")
public class TabOlcum extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -3209488889321560541L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABTALEP_ID")
    private TabTalep tabTalep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABRANDEVU_ID")
    private TabRandevu tabRandevu;

    @Column(name = "OLCUM_TARIHI")
    private LocalDateTime olcumTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OLCUM_YAPAN_SYKULLANICI_ID")
    private SyKullanici olcumYapan;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "tabOlcum", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @SQLRestriction("AKTIF=true")
    private List<TabOlcumDetay> detayList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TabOlcum other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}