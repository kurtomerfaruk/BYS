package tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKurs;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "AYGIRIS_CIKIS")
@NamedQuery(name = "AyGirisCikis.findGirisYapanlar", query = "SELECT g.ayKisi FROM AyGirisCikis g WHERE g.cikisTarihi IS NULL ORDER BY g.ayKisi.gnlKisi.ad," +
        "g.ayKisi.gnlKisi.soyad")
public class AyGirisCikis extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 8320326071911868361L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AYKISI_ID")
    private AyKisi ayKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLKURS_ID")
    private GnlKurs gnlKurs;

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

    @Transient
    @Getter(AccessLevel.NONE)
    private boolean exit;

    public boolean isExit() {
        return cikisTarihi != null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AyGirisCikis other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}