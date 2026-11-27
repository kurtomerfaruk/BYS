package tr.bel.gaziantep.bysweb.moduls.tabanlik.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabUretimAsamasi;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Tabanlık üretim kaydı. Üretim tamamlandığında mamul için URETIM_GIRISI stok hareketi oluşturulur.
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
@Table(name = "TABURETIM")
public class TabUretim extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2761039485019382751L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABTALEP_ID")
    private TabTalep tabTalep;

    @Enumerated(EnumType.STRING)
    @Column(name = "ASAMA")
    private EnumTabUretimAsamasi asama;

    @Column(name = "BASLANGIC_TARIHI")
    private LocalDateTime baslangicTarihi;

    @Column(name = "TAMAMLAMA_TARIHI")
    private LocalDateTime tamamlanmaTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SORUMLU_SYKULLANICI_ID")
    private SyKullanici sorumluSyKullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSTOK_ID")
    private StStok stStok;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_ID")
    private StDepo stDepo;

    @Column(name = "MIKTAR", precision = 18, scale = 4)
    private BigDecimal miktar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "URETIM_GIRIS_HAREKET_ID")
    private StStokHareket uretimGirisHareket;

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
        if (!(object instanceof TabUretim other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
