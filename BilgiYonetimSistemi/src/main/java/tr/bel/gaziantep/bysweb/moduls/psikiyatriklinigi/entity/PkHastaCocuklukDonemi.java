package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumEvetHayir;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumVarYok;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 28.09.2026 08:25
 */
@Getter
@Setter
@Entity
@Table(name = "PKHASTA_COCUKLUK_DONEMI")
public class PkHastaCocuklukDonemi extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 6961649684484025674L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Lob
    @Column(name = "KONUSMASI_VE_YURUMESI")
    private String konusmasiVeYurumesi;

    @Nationalized
    @Lob
    @Column(name = "ISTAH_VE_UYKU")
    private String istahVeUyku;

    @Enumerated(EnumType.STRING)
    @Column(name = "ATESLI_HAVALE_MENENJIT")
    private EnumVarYok atesliHavaleMenenjit;

    @Enumerated(EnumType.STRING)
    @Column(name = "YUKSEKTEN_DUSME")
    private EnumVarYok yuksektenDusme;

    @Enumerated(EnumType.STRING)
    @Column(name = "ISTENILEN_COCUK")
    private EnumEvetHayir istenilenCocuk;

    @Nationalized
    @Lob
    @Column(name = "BUYUK_KAZA_YARALANMA_KIRIK")
    private String buyukKazaYaralanmaKirik;

    @Nationalized
    @Lob
    @Column(name = "EGITIM")
    private String egitim;

    @Nationalized
    @Lob
    @Column(name = "KISILIK_OZELLIKLERI")
    private String kisilikOzellikleri;

    @Enumerated(EnumType.STRING)
    @Column(name = "UYUM_SORUNU_VAR_MI")
    private EnumEvetHayir uyumSorunuVarMi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "INTIHAR_GIRISIMI_VEYA_DUSUNCESI_OLDU_MU", length = 50)
    private String intiharGirisimiVeyaDusuncesiOlduMu;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkHastaCocuklukDonemi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}