package tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKurs;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "EKMGIRIS_CIKIS")
public class EkmGirisCikis extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYKISI_ID")
    private EyKisi eyKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLKURS_ID")
    private GnlKurs gnlKurs;

    @Column(name = "GIRIS_TARIHI")
    private LocalDateTime girisTarihi;

    @Column(name = "CIKIS_TARIHI")
    private LocalDateTime cikisTarihi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EkmGirisCikis other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}