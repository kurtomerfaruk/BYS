package tr.bel.gaziantep.bysweb.moduls.tabanlik.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabOnayDurumu;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabUcretTuru;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Ücretli/ücretsiz işlem kaydı. Bir talebe ait birden fazla ödeme/onay kaydı geçmişe dönük tutulabilir.
 *
 * @author OmerFarukKurt
 * @version 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TABODEME")
public class TabOdeme extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2761049385019382751L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABTALEP_ID")
    private TabTalep tabTalep;

    @Enumerated(EnumType.STRING)
    @Column(name = "UCRET_TURU")
    private EnumTabUcretTuru ucretTuru;

    @Column(name = "TUTAR", precision = 18, scale = 2)
    private BigDecimal tutar;

    @Column(name = "ODEME_TARIHI")
    private LocalDateTime odemeTarihi;

    @Nationalized
    @Column(name = "ODEME_YONTEMI", length = 50)
    private String odemeYontemi;

    @Nationalized
    @Column(name = "DEKONT_NO", length = 50)
    private String dekontNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "ONAY_DURUMU")
    private EnumTabOnayDurumu onayDurumu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ONAYLAYAN_SYKULLANICI_ID")
    private SyKullanici onaylayanSyKullanici;

    @Column(name = "ONAY_TARIHI")
    private LocalDateTime onayTarihi;

    @Nationalized
    @Lob
    @Column(name = "UCRETSIZ_GEREKCE")
    private String ucretsizGerekce;

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
        if (!(object instanceof TabOdeme other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
