package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.8.0
 * @since 21.05.2026 11:25
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SHRANDEVU")
@NamedQuery(name = "ShRandevu.findByStartDateByEndDate", query = "SELECT e FROM ShRandevu e WHERE e.aktif=true AND " +
        "(e.randevuTarihi>=:start AND e.randevuTarihi<=:end) ORDER BY e.randevuTarihi ASC")
public class ShRandevu extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -4616420372423785011L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHKISI_ID")
    private ShKisi shKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLPERSONEL_ID")
    private GnlPersonel gnlPersonel;

    @Column(name = "RANDEVU_TARIHI")
    private LocalDateTime randevuTarihi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KONU", length = 50)
    private String konu;

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
        if (!(object instanceof ShRandevu other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}