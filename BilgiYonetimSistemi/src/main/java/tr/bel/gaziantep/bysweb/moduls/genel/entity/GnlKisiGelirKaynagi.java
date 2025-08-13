package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGelirKaynagi;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "GNLKISI_GELIR_KAYNAGI")
public class GnlKisiGelirKaynagi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2680232115801929109L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLKISI_ID")
    private GnlKisi gnlKisi;

    @Column(name = "TANIM")
    @Enumerated(EnumType.STRING)
    private EnumGnlGelirKaynagi tanim;


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
        if (!(object instanceof GnlKisiGelirKaynagi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}