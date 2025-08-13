package tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKurs;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "AYKISI_KURS")
public class AyKisiKurs extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -9120605341169570611L;

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
        if (!(object instanceof AyKisiKurs other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}