package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumPuanlama;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkAkraba;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkEbeveynIliski;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 28.09.2026 08:25
 */
@Getter
@Setter
@Entity
@Table(name = "PKHASTA_AILE_ICI_ILISKI")
public class PkHastaAileIciIliski extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2698149354469872757L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "EBEVEYNLER_ARASI_ILISKI")
    private EnumPkEbeveynIliski ebeveynlerArasiIliski;

    @Nationalized
    @Lob
    @Column(name = "EBEVEYNLER_ARASI_ILISKI_ACIKLAMA")
    private String ebeveynlerArasiIliskiAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "ANNENIN_HASTAYLA_ILISKISI")
    private EnumPuanlama anneninHastaylaIliskisi;

    @Enumerated(EnumType.STRING)
    @Column(name = "BABANIN_HASTAYLA_ILISKISI")
    private EnumPuanlama babaninHastaylaIliskisi;

    @Enumerated(EnumType.STRING)
    @Column(name = "HASTANIN_KARDESLERIYLE_ILISKISI")
    private EnumPuanlama hastaninKardesleriyleIliskisi;

    @Enumerated(EnumType.STRING)
    @Column(name = "AILEDE_EN_COK_KIMINLE_ANLASIR")
    private EnumPkAkraba ailedeEnCokKiminleAnlasir;

    @Nationalized
    @Lob
    @Column(name = "AILEDE_EN_COK_KIMINLE_ANLASIR_ACIKLAMA")
    private String ailedeEnCokKiminleAnlasirAciklama;

    @Nationalized
    @Lob
    @Column(name = "HASTA_BU_DURUMA_NASIL_GELDI")
    private String hastaBuDurumaNasilGeldi;

    @Nationalized
    @Lob
    @Column(name = "MADDE_OYKUSU")
    private String maddeOykusu;

    @Nationalized
    @Lob
    @Column(name = "ZARAR")
    private String zarar;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkHastaAileIciIliski other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}