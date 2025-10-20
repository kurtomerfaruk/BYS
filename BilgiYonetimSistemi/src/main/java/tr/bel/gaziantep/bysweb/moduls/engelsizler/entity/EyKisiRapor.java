package tr.bel.gaziantep.bysweb.moduls.engelsizler.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDate;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 28.06.2025 08:32
 */
@Getter
@Setter
@Entity
@Table(name = "EYKISI_RAPOR")
public class EyKisiRapor extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -5931959043227881605L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYKISI_ID")
    private EyKisi eyKisi;


    @Column(name = "ENGELLI_SAGLIK_RAPORU_VARMI")
    private boolean engelliSaglikRaporuVarmi;

    @Size(max = 50)

    @Column(name = "RAPOR_NO", length = 50)
    private String raporNo;

    @Column(name = "RAPOR_TARIHI")
    private LocalDate raporTarihi;

    @Size(max = 150)
    @Column(name = "HASTAHANE", length = 150)
    private String hastahane;

    @Column(name = "TOPLAM_VUCUT_KAYIP_ORANI")
    private Integer toplamVucutKayipOrani;

    @Column(name = "AGIR_OZURLU")
    private boolean agirOzurlu;


    @Lob
    @Nationalized
    @Column(name = "ACIKLAMA")
    private String aciklama;


    @Column(name = "SUREKLI")
    private boolean surekli;

    @Column(name = "RAPOR_BITIS_TARIHI")
    private LocalDate raporBitisTarihi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EyKisiRapor other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}