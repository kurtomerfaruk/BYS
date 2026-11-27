package tr.bel.gaziantep.bysweb.moduls.tabanlik.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabRandevuDurumu;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabRandevuTuru;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:25
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TABRANDEVU")
public class TabRandevu extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 6519845616065128730L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABTALEP_ID")
    private TabTalep tabTalep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABKISI_ID")
    private TabKisi tabKisi;

    @Enumerated(EnumType.STRING)
    @Column(name = "RANDEVU_TURU")
    private EnumTabRandevuTuru randevuTuru;

    @Column(name = "RANDEVU_TARIHI")
    private LocalDateTime randevuTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name = "DURUM")
    private EnumTabRandevuDurumu durum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OLUSTURAN_SYKULLANICI_ID")
    private SyKullanici olusturan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GERCEKLESTIREN_SYKULLANICI_ID")
    private SyKullanici gerceklestiren;

    @Size(max = 500)
    @Nationalized
    @Column(name = "IPTAL_NEDENI", length = 500)
    private String iptalNedeni;

    @Size(max = 500)
    @Nationalized
    @Column(name = "ERTELEME_NEDENI", length = 500)
    private String ertelemeNedeni;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @ColumnDefault("0")
    @Column(name = "BILDIRIM_GONDERILDI", nullable = false)
    private boolean bildirimGonderildi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TabRandevu other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}