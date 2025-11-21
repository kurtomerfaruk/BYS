package tr.bel.gaziantep.bysweb.moduls.moralevi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumEvetHayir;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYakinlikDerecesi;
import tr.bel.gaziantep.bysweb.core.enums.moralevi.EnumMeAnketHizmetSuresi;
import tr.bel.gaziantep.bysweb.core.enums.moralevi.EnumMeAnketKatilimDurumu;
import tr.bel.gaziantep.bysweb.core.enums.moralevi.EnumMeAnketPeriyot;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.time.LocalDate;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 20.11.2025 15:07
 */
@Getter
@Setter
@Entity
@Table(name = "MEANKET")
public class MeAnket extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -6048886552698966535L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEKISI_ID")
    private MeKisi meKisi;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "ANKET_YAPILAN_GNLKISI_ID")
    private GnlKisi anketYapilanGnlKisi;

    @Column(name = "YAKINLIK")
    @Enumerated(EnumType.STRING)
    private EnumGnlYakinlikDerecesi yakinlik;

    @Column(name = "HIZMETLERDEN_MEMNUN")
    @Enumerated(EnumType.STRING)
    private EnumEvetHayir hizmetlerdenMemnun;

    @Nationalized
    @Lob
    @Column(name = "HIZMETLERDEN_MEMNUN_ACIKLAMA")
    private String hizmetlerdenMemnunAciklama;

    @Column(name = "OLUMLU_ILERLEME")
    @Enumerated(EnumType.STRING)
    private EnumEvetHayir olumluIlerleme;

    @Nationalized
    @Lob
    @Column(name = "OLUMLU_ILERLEME_ACIKLAMA")
    private String olumluIlerlemeAciklama;

    @Column(name = "SERVISTEN_MEMNUN")
    @Enumerated(EnumType.STRING)
    private EnumEvetHayir servistenMemnun;

    @Nationalized
    @Lob
    @Column(name = "SERVISTEN_MEMNUN_ACIKLAMA")
    private String servistenMemnunAciklama;

    @Column(name = "PERSONEL_DAVRANISLARINDAN_MEMNUN")
    @Enumerated(EnumType.STRING)
    private EnumEvetHayir personelDavranislarindanMemnun;

    @Nationalized
    @Lob
    @Column(name = "PERSONEL_DAVRANISLARINDAN_MEMNUN_ACIKLAMA")
    private String personelDavranislarindanMemnunAciklama;

    @Column(name = "ONERILER", length = 50)
    @Enumerated(EnumType.STRING)
    private EnumEvetHayir oneriler;

    @Nationalized
    @Lob
    @Column(name = "ONERILER_ACIKLAMA")
    private String onerilerAciklama;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GORUSME_YAPAN_MEPERSONEL_ID")
    private MePersonel gorusmeYapanMePersonel;

    @Column(name = "TARIH")
    private LocalDate tarih;

    @Column(name = "ANKET_PERIYODU")
    @Enumerated(EnumType.STRING)
    private EnumMeAnketPeriyot anketPeriyodu;

    @Column(name = "HIZMET_SURESI")
    @Enumerated(EnumType.STRING)
    private EnumMeAnketHizmetSuresi hizmetSuresi;

    @Column(name = "HAFTALIK_KATILIM_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumMeAnketKatilimDurumu haftalikKatilimDurumu;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MeAnket other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}