package tr.bel.gaziantep.bysweb.moduls.tabanlik.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabTalepDurumu;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 11:44
 */
@Getter
@Setter
@Entity
@Table(name = "TABTALEP")
public class TabTalep extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 7067415520237021918L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 30)
    @Nationalized
    @Column(name = "TALEP_NO", length = 30)
    private String talepNo;

    @Column(name = "TALEP_TARIHI")
    private LocalDateTime talepTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABKISI_ID")
    private TabKisi tabKisi;

    @Enumerated(EnumType.STRING)
    @Column(name = "DURUM")
    private EnumTabTalepDurumu durum;

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
        if (!(object instanceof TabTalep other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}