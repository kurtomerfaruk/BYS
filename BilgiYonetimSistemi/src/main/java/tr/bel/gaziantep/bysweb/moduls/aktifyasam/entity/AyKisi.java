package tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.aktifyasam.EnumAyDevamDurumu;
import tr.bel.gaziantep.bysweb.core.enums.aktifyasam.EnumAyGrup;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGun;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 14:01
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AYKISI")
@NamedQuery(name = "AyKisi.findByLatLngIsNull", query = "SELECT a FROM AyKisi a WHERE a.aktif=true AND a.gnlKisi.latLng IS NULL  AND " +
        "a.gnlKisi.binaNo IS NOT NULL")
@NamedQuery(name = "AyKisi.findByGun",query = "SELECT a FROM AyKisi a WHERE a.aktif=true " +
        "AND a.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG AND a.gun =:gun " +
        "AND a.devamDurumu =tr.bel.gaziantep.bysweb.core.enums.aktifyasam.EnumAyDevamDurumu.DEVAM_EDIYOR")
public class AyKisi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 668865374558285865L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "KAYIT_TARIHI")
    private LocalDateTime kayitTarihi;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "GNLKISI_ID")
    private GnlKisi gnlKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AYBIRIM_ID")
    private AyBirim ayBirim;

    @Column(name = "GRUP")
    @Enumerated(EnumType.STRING)
    private EnumAyGrup grup;

    @Column(name = "GUN")
    @Enumerated(EnumType.STRING)
    private EnumGnlGun gun;

    @Column(name = "DEVAM_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumAyDevamDurumu devamDurumu;

    @Size(max = 150)
    @Nationalized
    @Column(name = "DURAK",length = 150)
    private String durak;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Nationalized
    @Lob
    @Column(name = "HOBILERI")
    private String hobileri;

    @Nationalized
    @Lob
    @Column(name = "YASLININ_BEKLENTILERI")
    private String yaslininBeklentileri;

    @ColumnDefault("0")
    @Column(name = "SOSYO_PSIKOLOJIK_DESTEK")
    private boolean sosyoPsikolojikDestek;

    @ColumnDefault("0")
    @Column(name = "SOSYAL_AKTIVITE_ISTEGI")
    private boolean sosyalAktiviteIstegi;

    @ColumnDefault("0")
    @Column(name = "AILEDEKI_HUKUMLU_DURUMU")
    private boolean ailedekiHukumluDurumu;

    @Nationalized
    @Lob
    @Column(name = "AILEDEKI_HUKUMLU_DURUMU_ACIKLAMA")
    private String ailedekiHukumluDurumuAciklama;

    @ColumnDefault("0")
    @Column(name = "AILEDEKI_ASKER_DURUMU")
    private boolean ailedekiAskerDurumu;

    @ColumnDefault("0")
    @Column(name = "AILEDEKI_GAZI_DURUMU")
    private boolean ailedekiGaziDurumu;

    @ColumnDefault("0")
    @Column(name = "AILEDEKI_SEHIT_DURUMU")
    private boolean ailedekiSehitDurumu;



    @OneToMany(mappedBy = "ayKisi", fetch = FetchType.LAZY)
    @SQLRestriction("AKTIF=true")
    @Builder.Default
    private List<AyKisiAranacakKisi> ayAranacakKisiList = new ArrayList<>();

    @OneToMany(mappedBy = "ayKisi", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    private List<AyKisiSaglikBilgi> ayKisiSaglikBilgileriList = new ArrayList<>();

    @OneToMany(mappedBy = "ayKisi", fetch = FetchType.LAZY)
    @Builder.Default
    private List<AyGirisCikis> ayGirisCikisList = new ArrayList<>();

    @OneToMany(mappedBy = "ayKisi", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    private List<AyKisiAktivite> ayKisiAktiviteList = new ArrayList<>();

    @OneToMany(mappedBy = "ayKisi", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    private List<AyKisiSanatsalBeceri> ayKisiSanatsalBeceriList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AyKisi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}