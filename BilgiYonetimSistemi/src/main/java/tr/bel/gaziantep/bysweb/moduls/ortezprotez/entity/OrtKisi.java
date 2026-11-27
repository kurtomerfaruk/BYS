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
 * @version 1.30.0
 * @since 24.11.2026 11:39
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORTKISI")
@NamedQuery(name = "OrtKisi.findByGnlKisi", query = "SELECT o FROM OrtKisi o WHERE o.aktif=true AND o.gnlKisi = :gnlKisi")
@NamedQuery(name = "OrtKisi.findByTcKimlikNo", query = "SELECT o FROM OrtKisi o WHERE o.aktif=true AND o.gnlKisi.tcKimlikNo = :tcKimlikNo")
public class OrtKisi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 9069164197971468148L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "GNLKISI_ID")
    private GnlKisi gnlKisi;

    @ColumnDefault("0")
    @Column(name = "ENGELLI")
    private boolean engelli;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ENGEL_OLUSMA_DURUMU", length = 50)
    private String engelOlusmaDurumu;

    @ColumnDefault("0")
    @Column(name = "PROTEZ", nullable = false)
    private boolean protez;

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

    @Size(max = 250)
    @Nationalized
    @Column(name = "BIRINCI_DERECE_VEFAT_DURUMU", length = 250)
    private String birinciDereceVefatDurumu;

    @Nationalized
    @Lob
    @Column(name = "HASTANIN_OYKUSU")
    private String hastaninOykusu;

    @Nationalized
    @Lob
    @Column(name = "HASTANIN_TALEBI")
    private String hastaninTalebi;

    @OneToMany(mappedBy = "ortKisi", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    @Builder.Default
    private List<OrtKisiKullandigiCihaz> ortKisiKullandigiCihazList = new ArrayList<>();

    @OneToMany(mappedBy = "ortKisi", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    @Builder.Default
    private List<OrtKisiProtezAmputeSeviye> ortKisiProtezAmputeSeviyeList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtKisi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}