package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepDurumu;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 15:52
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ORTTALEP_DURUM_GECMISI")
public class OrtTalepDurumGecmisi extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 7722734566688452732L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTTALEP_ID")
    private OrtTalep ortTalep;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESKI_DURUM")
    private EnumOrtTalepDurumu eskiDurum;

    @Enumerated(EnumType.STRING)
    @Column(name = "YENI_DURUM")
    private EnumOrtTalepDurumu yeniDurum;

    @Column(name = "ISLEM_TARIHI")
    private LocalDateTime islemTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ISLEMI_YAPAN_SYKULLANICI_ID")
    private SyKullanici islemiYapanSyKullanici;

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
        if (!(object instanceof OrtTalepDurumGecmisi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}