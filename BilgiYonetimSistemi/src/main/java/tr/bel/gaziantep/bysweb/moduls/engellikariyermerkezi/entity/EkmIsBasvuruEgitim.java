package tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlEgitimDurumu;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlEgitimBolum;

import java.io.Serial;
import java.time.LocalDate;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.08.2025 15:24
 */
@Getter
@Setter
@Entity
@Table(name = "EKMIS_BASVURU_EGITIM")
public class EkmIsBasvuruEgitim extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 6718910875760184254L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EKMIS_BASVURU_ID")
    private EkmIsBasvuru ekmIsBasvuru;

    @Enumerated(EnumType.STRING)
    @Column(name = "EGITIM_DURUMU")
    private EnumGnlEgitimDurumu egitimDurumu;

    @Size(max = 500)
    @Nationalized
    @Column(name = "OKUL_ADI", length = 500)
    private String okulAdi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLEGITIM_BOLUM_ID")
    private GnlEgitimBolum gnlEgitimBolum;

    @Column(name = "BASLANGIC_TARIHI")
    private LocalDate baslangicTarihi;

    @Column(name = "BITIS_TARIHI")
    private LocalDate bitisTarihi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "MEZUNIYET_DERECESI", length = 50)
    private String mezuniyetDerecesi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EkmIsBasvuruEgitim other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}