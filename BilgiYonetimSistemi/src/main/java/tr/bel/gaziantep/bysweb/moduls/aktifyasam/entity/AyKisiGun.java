package tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGun;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.12.2025 16:07
 */
@Getter
@Setter
@Entity
@Table(name = "AYKISI_GUN")
@NamedQuery(name = "AyKisiGun.findByGun", query = "SELECT a.ayKisi FROM AyKisiGun a WHERE a.aktif=true " +
        "AND a.ayKisi.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG AND a.gun =:gun " +
        "AND a.ayKisi.devamDurumu =tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDevamDurumu.DEVAM_EDIYOR")

public class AyKisiGun extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 8931608780267974957L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AYKISI_ID")
    private AyKisi ayKisi;

    @Enumerated(EnumType.STRING)
    @Column(name = "GUN")
    private EnumGnlGun gun;

    @ColumnDefault("0")
    @Column(name = "SECILI", nullable = false)
    private boolean secili;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AyKisiGun other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}