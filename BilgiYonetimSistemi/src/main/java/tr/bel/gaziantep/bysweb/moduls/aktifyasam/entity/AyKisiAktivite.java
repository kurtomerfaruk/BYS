package tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AYKISI_AKTIVITE")
public class AyKisiAktivite extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 8685927564506902853L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AYKISI_ID")
    private AyKisi ayKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AYAKTIVITE_ID")
    private AyAktivite ayAktivite;

    @ColumnDefault("0")
    @Column(name = "SECILI")
    private boolean secili;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AyKisiAktivite other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}