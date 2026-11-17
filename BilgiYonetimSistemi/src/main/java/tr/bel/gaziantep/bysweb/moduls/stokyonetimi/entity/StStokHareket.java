package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 11:51
 */
@Getter
@Setter
@Entity
@Table(name = "STSTOK_HAREKET")
public class StStokHareket extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -4277676988496603636L;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "HAREKET_TURU")
    private EnumStHareketTuru hareketTuru;

    @Enumerated(EnumType.STRING)
    @Column(name = "YON")
    private EnumGirisCikis yon;

    @Column(name = "MIKTAR", precision = 18, scale = 4)
    private BigDecimal miktar;

    @Column(name = "BIRIM_MALIYET", precision = 18, scale = 4)
    private BigDecimal birimMaliyet;

    @Column(name = "TOPLAM_MALIYET", precision = 18, scale = 4)
    private BigDecimal toplamMaliyet;

    @Size(max = 50)
    @Nationalized
    @Column(name = "LOT", length = 50)
    private String lot;

    @Column(name = "SKT")
    private LocalDate skt;

    @Column(name = "ISLEM_TARIHI")
    private LocalDateTime islemTarihi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "BELGE_TURU", length = 50)
    private String belgeTuru;

    @Size(max = 50)
    @Nationalized
    @Column(name = "BELGE_NO", length = 50)
    private String belgeNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TERS_HAREKET_ID")
    private StStokHareket tersHareket;

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
        if (!(object instanceof StStokHareket other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}