package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlHastane;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 24.09.2025 09:02
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORTHASTA")
@NamedQuery(name = "OrtHasta.findByKisiTcKimlikNo", query = "SELECT e FROM OrtHasta e WHERE e.aktif=true AND e.gnlKisi.tcKimlikNo = :tcKimlikNo")
public class OrtHasta extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7609692408098149764L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "GNLKISI_ID")
    private GnlKisi gnlKisi;

    @ColumnDefault("0")
    @Column(name = "ENGELLI", nullable = false)
    private boolean engelli;

//    @Column(name = "ENGEL_OLUSMA_DURUMU")
//    @Enumerated(EnumType.STRING)
//    private EnumOrtEngelOlusum engelOlusmaDurumu;

    @ColumnDefault("0")
    @Column(name = "PROTEZ", nullable = false)
    private boolean protez;

//    @Size(max = 150)
//    @Nationalized
//    @Column(name = "PROTEZ_SEVIYESI", length = 150)
//    private String protezSeviyesi;

    @ColumnDefault("0")
    @Column(name = "ORTEZ", nullable = false)
    private boolean ortez;

    @Size(max = 150)
    @Nationalized
    @Column(name = "ORTEZ_TIPI", length = 150)
    private String ortezTipi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AMELIYAT_OLDUGU_GNLHASTANE_ID")
    private GnlHastane ameliyatOlduguHastane;

    @Column(name = "AMELIYAT_TARIHI")
    private LocalDate ameliyatTarihi;

    @Size(max = 150)
    @Nationalized
    @Column(name = "DEPREME_NEREDE_YAKALANDI", length = 150)
    private String depremeNeredeYakalandi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "BIRINCI_DERECE_VEFAT_DURUMU", length = 50)
    private String birinciDereceVefatDurumu;

    @Nationalized
    @Lob
    @Column(name = "HASTANIN_OYKUSU")
    private String hastaninOykusu;

    @Nationalized
    @Lob
    @Column(name = "HASTANIN_TALEBI")
    private String hastaninTalebi;

    @OneToMany(mappedBy = "ortHasta")
    @Builder.Default
    private List<OrtRandevu> ortrandevus = new ArrayList<>();

    @OneToMany(mappedBy = "ortHasta")
    @SQLRestriction("AKTIF=true")
    @Builder.Default
    private List<OrtFizikTedavi> ortFizikTedaviList = new ArrayList<>();

    @OneToMany(mappedBy = "ortHasta", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    @Builder.Default
    private List<OrtHastaKullandigiCihaz> ortHastaKullandigiCihazList = new ArrayList<>();

    @OneToMany(mappedBy = "ortHasta", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    @Builder.Default
    private List<OrtHastaProtezAmputeSeviye> ortHastaProtezAmputeSeviyeList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtHasta other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}