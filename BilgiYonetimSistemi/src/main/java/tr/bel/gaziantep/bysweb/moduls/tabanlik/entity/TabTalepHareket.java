package tr.bel.gaziantep.bysweb.moduls.tabanlik.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabTalepDurumu;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 11:44
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TABTALEP_HAREKET")
public class TabTalepHareket extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 3487019327937490352L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABTALEP_ID")
    private TabTalep tabTalep;

    @Size(max = 50)
    @Nationalized
    @Enumerated(EnumType.STRING)
    @Column(name = "ESKI_DURUM", length = 50)
    private EnumTabTalepDurumu eskiDurum;

    @Size(max = 50)
    @Nationalized
    @Enumerated(EnumType.STRING)
    @Column(name = "YENI_DURUM", length = 50)
    private EnumTabTalepDurumu yeniDurum;

    @Size(max = 60)
    @Nationalized
    @Column(name = "ISLEM", length = 60)
    private String islem;

    @Column(name = "ISLEM_TARIHI")
    private LocalDateTime islemTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ISLEMI_YAPAN_SYKULLANICI_ID")
    private SyKullanici islemiYapan;

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
        if (!(object instanceof TabTalepHareket other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}