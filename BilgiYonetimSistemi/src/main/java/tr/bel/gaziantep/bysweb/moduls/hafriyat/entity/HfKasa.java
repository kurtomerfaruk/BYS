package tr.bel.gaziantep.bysweb.moduls.hafriyat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.hafriyat.EnumHfTahsilatTuru;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlBanka;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.07.2025 11:19
 */
@Getter
@Setter
@Entity
@Table(name = "HFKASA")
public class HfKasa extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 8818824399437558721L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HFFIRMA_ID")
    private HfFirma hfFirma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLBANKA_ID")
    private GnlBanka gnlBanka;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HFHAFRIYAT_IS_ID")
    private HfHafriyatIs hfHafriyatIs;

    @Column(name = "TAHSILAT_TURU")
    @Enumerated(EnumType.STRING)
    private EnumHfTahsilatTuru tahsilatTuru;

    @Column(name = "TUTAR", precision = 18, scale = 2)
    private BigDecimal tutar;

    @Column(name = "TAHSIL_TARIHI")
    private LocalDate tahsilTarihi;

    @Column(name = "VADE_TARIHI")
    private LocalDate vadeTarihi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SERI_NO", length = 50)
    private String seriNo;

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
        if (!(object instanceof HfKasa other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}