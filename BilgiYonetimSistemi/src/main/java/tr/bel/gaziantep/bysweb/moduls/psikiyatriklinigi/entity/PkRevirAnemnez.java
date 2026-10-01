package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumVarYok;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 1.10.2026 09:03
 */
@Getter
@Setter
@Entity
@Table(name = "PKREVIR_ANEMNEZ")
public class PkRevirAnemnez extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -8475188297274400972L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKHASTA_TEDAVI_SEKLI_ID")
    private PkHastaTedaviSekli pkHastaTedaviSekli;

    @Column(name = "YATIS_TARIHI")
    private LocalDateTime yatisTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GORUSME_YAPAN_PKPERSONEL_ID")
    private PkPersonel gorusmeYapanPkPersonel;

    @Size(max = 50)
    @Nationalized
    @Column(name = "PROTOKOL_NO", length = 50)
    private String protokolNo;

    @Column(name = "BOY")
    private Integer boy;

    @Column(name = "KILO")
    private Integer kilo;

    @Nationalized
    @Lob
    @Column(name = "ALERJI")
    private String alerji;

    @Enumerated(EnumType.STRING)
    @Column(name = "AILESEL_HASTALIK")
    private EnumVarYok aileselHastalik;

    @Nationalized
    @Lob
    @Column(name = "AILESEL_HASTALIK_ACIKLAMA")
    private String aileselHastalikAciklama;

    @Nationalized
    @Lob
    @Column(name = "ON_TIBBI_TANI")
    private String onTibbiTani;

    @Nationalized
    @Lob
    @Column(name = "TESLIM_ALINAN_TIBBI_MALZEMELER")
    private String teslimAlinanTibbiMalzemeler;

    @Nationalized
    @Lob
    @Column(name = "CALISMA_VE_EGLENCE")
    private String calismaVeEglence;

   @Enumerated(EnumType.STRING)
    @Column(name = "SON_ALTI_AYDA_KILO_KAYBI_VAR_MI")
    private EnumVarYok sonAltiAydaKiloKaybiVarMi;

    @Nationalized
    @Lob
    @Column(name = "SON_ALTI_AYDA_KILO_KAYBI_VAR_MI_ACIKLAMA")
    private String sonAltiAydaKiloKaybiVarMiAciklama;

    @OneToMany(mappedBy = "pkRevirAnemnez",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<PkRevirAnemnezDetay> pkRevirAnemnezDetayList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkRevirAnemnez other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}