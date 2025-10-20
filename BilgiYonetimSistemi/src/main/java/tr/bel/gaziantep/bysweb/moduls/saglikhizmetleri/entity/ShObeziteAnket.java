package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumEvetHayir;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumVarYok;
import tr.bel.gaziantep.bysweb.core.enums.saglikhizmetleri.EnumShDanismanlikHizmeti;
import tr.bel.gaziantep.bysweb.core.enums.saglikhizmetleri.EnumShKronikRahatsizlik;
import tr.bel.gaziantep.bysweb.core.enums.saglikhizmetleri.EnumShObeziteHizmet;
import tr.bel.gaziantep.bysweb.core.enums.saglikhizmetleri.EnumShSigaraAlkolKullanimi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.07.2025 13:12
 */
@Getter
@Setter
@Entity
@Table(name = "SHOBEZITE_ANKET")
public class ShObeziteAnket extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -1082037806710973327L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ANKET_TARIHI")
    private LocalDateTime anketTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHKISI_ID")
    private ShKisi shKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANKETI_YAPAN_GNLPERSONEL_ID")
    private GnlPersonel anketiYapanGnlPersonel;

    @Column(name = "BOY", precision = 18, scale = 2)
    private BigDecimal boy;

    @Column(name = "KILO", precision = 18, scale = 2)
    private BigDecimal kilo;

    @Column(name = "BKI", precision = 18, scale = 2)
    private BigDecimal bki;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KATILIM_SONUCU", length = 50)
    private String katilimSonucu;

    @Column(name = "DANISMANLIK_HIZMETI_VEREN")
    @Enumerated(EnumType.STRING)
    private EnumShDanismanlikHizmeti danismanlikHizmetiVeren;

    @Column(name = "VERILEN_HIZMET")
    @Enumerated(EnumType.STRING)
    private EnumShObeziteHizmet verilenHizmet;

    @Column(name = "SIGARA_ALKOL_KULLANIM_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumShSigaraAlkolKullanimi sigaraAlkolKullanimDurumu;

    @Column(name = "COCUKLUK_CAGINDA_SISMANLIK_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumEvetHayir cocuklukCagindaSismanlikDurumu;

    @Column(name = "DUZENLI_SU_TUKETIMI_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumEvetHayir duzenliSuTuketimiDurumu;

    @Column(name = "KRONIK_RAHATSIZLIK_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumShKronikRahatsizlik kronikRahatsizlikDurumu;

    @Column(name = "DUZENLI_BESLENME_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumEvetHayir duzenliBeslenmeDurumu;

    @Column(name = "BESIN_ALERJI_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumVarYok besinAlerjiDurumu;

    @Column(name = "ANEMI_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumVarYok anemiDurumu;

    @Column(name = "AILE_OBEZITE_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumVarYok aileObeziteDurumu;

    @Column(name = "GIDA_TAKVIYESI_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumEvetHayir gidaTakviyesiDurumu;

    @Column(name = "SIDDETLI_FIZIKSEL_AKTIVITE_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumEvetHayir siddetliFizikselAktiviteDurumu;

    @Column(name = "ORTA_DERECELI_AKTIVITE_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumEvetHayir ortaDereceliAktiviteDurumu;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ShObeziteAnket other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}