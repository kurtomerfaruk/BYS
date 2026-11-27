package tr.bel.gaziantep.bysweb.moduls.tabanlik.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumEvetHayir;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 14:57
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TABURETIM_KARAR")
public class TabUretimKarar extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -6690076264733580047L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABTALEP_ID")
    private TabTalep tabTalep;

    @Enumerated(EnumType.STRING)
    @Column(name = "KARAR")
    private EnumEvetHayir karar;

    @Column(name = "KARAR_TARIHI")
    private LocalDateTime kararTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KARARI_VEREN_SYKULLANICI_ID")
    private SyKullanici karariVeren;

    @Nationalized
    @Lob
    @Column(name = "KARAR_GEREKCESI")
    private String kararGerekcesi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TabUretimKarar other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}