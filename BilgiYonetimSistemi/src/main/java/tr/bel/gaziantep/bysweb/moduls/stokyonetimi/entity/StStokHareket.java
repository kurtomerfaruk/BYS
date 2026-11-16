package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 08:47
 */
@Getter
@Setter
@Entity
@Table(name = "STSTOK_HAREKET")
public class StStokHareket extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -8042780914012191519L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "HAREKET_NO", length = 50)
    private String hareketNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSTOK_ID")
    private StStok stStok;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_ID")
    private StDepo stDepo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_LOKASYON_ID")
    private StDepoLokasyon stDepoLokasyon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HEDEF_DEPO_ID")
    private StDepo hedefDepo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HEDEF_LOKASYON_ID")
    private StDepoLokasyon hedefLokasyon;

    @Enumerated(EnumType.STRING)
    @Column(name = "HAREKET_TURU")
    private EnumStHareketTuru hareketTuru;

    @Enumerated(EnumType.STRING)
    @Column(name = "DURUM")
    private EnumGirisCikis durum;

    @Column(name = "MIKTAR", precision = 18, scale = 4)
    private BigDecimal miktar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STBIRIM_ID")
    private StBirim stBirim;

    @Column(name = "BIRIM_MALIYET", precision = 18, scale = 4)
    private BigDecimal birimMaliyet;

    @Column(name = "TOPLAM_MALIYET", precision = 18, scale = 4)
    private BigDecimal toplamMaliyet;

    @Column(name = "ISLEM_TARIHI")
    private LocalDateTime islemTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYKULLANICI_ID")
    private SyKullanici syKullanici;

    @Size(max = 50)
    @Nationalized
    @Column(name = "BELGE_TURU", length = 50)
    private String belgeTuru;

    @Column(name = "BELGE_ID")
    private Integer belgeId;

    @Size(max = 50)
    @Nationalized
    @Column(name = "BELGE_NO", length = 50)
    private String belgeNo;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Size(max = 100)
    @Nationalized
    @Column(name = "KAYNAK_ISLEM_REFERANSI", length = 100)
    private String kaynakIslemReferansi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STTRANSFER_ID")
    private StTransfer stTransfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STSAYIM_ID")
    private StSayim stSayim;

    @ColumnDefault("0")
    @Column(name = "IPTAL_EDILDI", nullable = false)
    private boolean iptalEdildi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STTEDARIKCI_ID")
    private StTedarikci stTedarikci;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StStokHareket other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}