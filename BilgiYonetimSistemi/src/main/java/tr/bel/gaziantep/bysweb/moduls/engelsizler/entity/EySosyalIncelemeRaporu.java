package tr.bel.gaziantep.bysweb.moduls.engelsizler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyDegerlendirmeDurumu;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "EYSOSYAL_INCELEME_RAPORU")
@NamedQuery(name = "EySosyalIncelemeRaporu.findByGnlKisi", query = "SELECT e FROM EySosyalIncelemeRaporu e WHERE e.aktif=true " +
        "AND e.eyKisi.gnlKisi=:gnlKisi ORDER BY e.raporTarihi DESC")
public class EySosyalIncelemeRaporu extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -4994464663294025516L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "RAPOR_TARIHI")
    private LocalDateTime raporTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RAPORU_DUZENLEYEN_GNLPERSONEL_ID")
    private GnlPersonel raporuDuzenleyenGnlPersonel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYINCELEME_NEDENI_ID")
    private EyIncelemeNedeni eyIncelemeNedeni;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYKISI_ID")
    private EyKisi eyKisi;

    @Lob
    @Nationalized
    @Column(name = "AILE_BILGILERI")
    private String aileBilgileri;

    @Lob
    @Nationalized
    @Column(name = "ENGELIN_OLUSUMU_BILGISI")
    private String engelinOlusumuBilgisi;

    @Lob
    @Nationalized
    @Column(name = "EKONOMIK_DURUMU")
    private String ekonomikDurumu;

    @Lob
    @Nationalized
    @Column(name = "KONUT_DURUMU")
    private String konutDurumu;

    @Lob
    @Nationalized
    @Column(name = "KISILIK_OZELLIKLERI")
    private String kisilikOzellikleri;

    @Lob
    @Nationalized
    @Column(name = "KONUTUN_FIZIKI_CEVRESI")
    private String konutunFizikiCevresi;

    @Lob
    @Nationalized
    @Column(name = "TALEP")
    private String talep;

    @Lob
    @Nationalized
    @Column(name = "GENEL_DEGERLENDIRME_ACIKLAMA")
    private String genelDegerlendirmeAciklama;

    @Column(name = "DEGERLENDIRME_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumEyDegerlendirmeDurumu degerlendirmeDurumu;

    @Lob
    @Nationalized
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EySosyalIncelemeRaporu other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}