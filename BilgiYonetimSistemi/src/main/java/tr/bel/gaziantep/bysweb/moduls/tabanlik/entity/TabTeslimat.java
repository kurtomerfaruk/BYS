package tr.bel.gaziantep.bysweb.moduls.tabanlik.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * Tabanlık teslimat kaydı. Stok çıkış hareketi (SATIS / URUN_CIKISI) referansı tutulur.
 *
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 16:15
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TABTESLIMAT")
public class TabTeslimat extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2761039481739502851L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABTALEP_ID")
    private TabTalep tabTalep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABRANDEVU_ID")
    private TabRandevu tabRandevu;

    @Column(name = "TESLIMAT_TARIHI")
    private LocalDateTime teslimatTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TESLIM_EDEN_SYKULLANICI_ID")
    private SyKullanici teslimEdenSyKullanici;

    @Nationalized
    @Lob
    @Column(name = "URUN_BILGILERI")
    private String urunBilgileri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSTOK_HAREKET_ID")
    private StStokHareket stStokHareket;

    @Enumerated(EnumType.STRING)
    @Column(name = "STOK_HAREKET_TURU")
    private EnumStHareketTuru stokHareketTuru;

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
        if (!(object instanceof TabTeslimat other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
