package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtEngelOlusum;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 24.09.2025 09:02
 */

@Getter
@Setter
@Entity
@Table(name = "ORTHASTA")
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

    @Column(name = "DEGERLENDIRME_TARIHI")
    private LocalDateTime degerlendirmeTarihi;

    @Column(name = "ENGEL_OLUSMA_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumOrtEngelOlusum engelOlusmaDurumu;

    @ColumnDefault("0")
    @Column(name = "PROTEZ", nullable = false)
    private boolean protez;

    @Size(max = 150)
    @Nationalized
    @Column(name = "PROTEZ_SEVIYESI", length = 150)
    private String protezSeviyesi;

    @ColumnDefault("0")
    @Column(name = "ORTEZ", nullable = false)
    private boolean ortez;

    @Size(max = 150)
    @Nationalized
    @Column(name = "ORTEZ_TIPI", length = 150)
    private String ortezTipi;

    @Size(max = 150)
    @Nationalized
    @Column(name = "KULLANDIGI_CIHAZ_ORTEZ", length = 150)
    private String kullandigiCihazOrtez;

    @Size(max = 150)
    @Nationalized
    @Column(name = "KULLANDIGI_CIHAZ_PROTEZ", length = 150)
    private String kullandigiCihazProtez;

    @Size(max = 150)
    @Nationalized
    @Column(name = "AMELIYAT_OLDUGU_HASTANE", length = 150)
    private String ameliyatOlduguHastane;

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
    @Column(name = "HASTANIN_OYKUSU_TALEBI")
    private String hastaninOykusuTalebi;

    @Nationalized
    @Lob
    @Column(name = "KARAR_GORUS")
    private String kararGorus;

    @Column(name = "BASVURU_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumOrtBasvuruDurumu basvuruDurumu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MUAYENE_YAPAN_ORTPERSONEL_ID")
    private OrtPersonel muayeneYapanOrtPersonel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RAPORU_ONAYLAYAN_ORTPERSONEL_ID")
    private OrtPersonel raporuOnaylayanOrtPersonel;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "OrtHasta")
    private List<OrtRandevu> ortrandevus = new ArrayList<>();

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