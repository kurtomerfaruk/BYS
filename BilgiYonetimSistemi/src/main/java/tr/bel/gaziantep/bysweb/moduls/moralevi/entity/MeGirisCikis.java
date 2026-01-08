package tr.bel.gaziantep.bysweb.moduls.moralevi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.10.2025 11:07
 */
@Getter
@Setter
@Entity
@Table(name = "MEGIRIS_CIKIS")
@NamedQuery(name = "MeGirisCikis.findByEntrance", query = "SELECT m FROM MeGirisCikis m WHERE m.cikisTarihi IS NULL ORDER BY m.meKisi.eyKisi.gnlKisi.ad,m.meKisi" +
        ".eyKisi.gnlKisi.soyad")
public class MeGirisCikis extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 8271885543327926492L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEKISI_ID")
    private MeKisi meKisi;

    @Column(name = "GIRIS_TARIHI")
    private LocalDateTime girisTarihi;

    @Column(name = "CIKIS_TARIHI")
    private LocalDateTime cikisTarihi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "TANSIYON", length = 50)
    private String tansiyon;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SEKER", length = 50)
    private String seker;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ATES", length = 50)
    private String ates;

    @Size(max = 50)
    @Nationalized
    @Column(name = "NABIZ", length = 50)
    private String nabiz;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MeGirisCikis other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}