package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.08.2025 08:57
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SYDUYURU_KULLANICI")
@NamedQuery(name = "SyDuyuruKullanici.findByKullanici", query = "SELECT s FROM SyDuyuruKullanici s WHERE s.aktif=true AND " +
        "s.syKullanici =:kullanici AND s.okundu=false ORDER BY s.syDuyuru.yayinlanmaTarihi DESC")
public class SyDuyuruKullanici extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -8944042799401604219L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYKULLANICI_ID")
    private SyKullanici syKullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYDUYURU_ID")
    private SyDuyuru syDuyuru;

    @ColumnDefault("0")
    @Column(name = "OKUNDU", nullable = false)
    private boolean okundu;

    @Column(name = "OKUNMA_TARIHI")
    private LocalDateTime okunmaTarihi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SyDuyuruKullanici other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}