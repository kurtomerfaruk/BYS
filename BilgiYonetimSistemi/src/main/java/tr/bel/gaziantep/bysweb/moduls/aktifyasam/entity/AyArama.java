package tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.aktifyasam.EnumAyGrup;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGun;

import java.io.Serial;
import java.time.LocalDate;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.11.2025 13:46
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AYARAMA")
@NamedQuery(name = "AyArama.findByGunByTarihByGrup",query = "SELECT a FROM AyArama a WHERE a.aktif=true AND a.tarih =:tarih AND a.gun=:gun AND a.grup =:grup")
public class AyArama extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7360048252207386816L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AYKISI_ID")
    private AyKisi ayKisi;

    @Column(name = "TARIH")
    private LocalDate tarih;

    @ColumnDefault("0")
    @Column(name = "GELECEK", nullable = false)
    private boolean gelecek;

    @Column(name = "GUN")
    @Enumerated(EnumType.STRING)
    private EnumGnlGun gun;

    @Column(name = "GRUP")
    @Enumerated(EnumType.STRING)
    private EnumAyGrup grup;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AyArama other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}