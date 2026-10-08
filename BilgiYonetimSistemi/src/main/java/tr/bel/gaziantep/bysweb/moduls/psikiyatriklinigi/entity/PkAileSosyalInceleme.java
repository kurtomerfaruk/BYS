package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 28.09.2026 13:56
 */
@Getter
@Setter
@Entity
@Table(name = "PKAILE_SOSYAL_INCELEME")
public class PkAileSosyalInceleme extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -2240175428446643098L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKAILE_ID")
    private PkAile pkAile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKHASTA_ID")
    private PkHasta pkHasta;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SOSYAL_GUVENCE", length = 50)
    private String sosyalGuvence;

    @Column(name = "AYLIK_GELIRI", precision = 8, scale = 2)
    private BigDecimal aylikGeliri;

    @Nationalized
    @Lob
    @Column(name = "GENEL_TANITIM")
    private String genelTanitim;

    @Nationalized
    @Lob
    @Column(name = "KISILIK_OZELLIKLERI")
    private String kisilikOzellikleri;

    @Nationalized
    @Lob
    @Column(name = "IS_DURUMU")
    private String isDurumu;

    @Nationalized
    @Lob
    @Column(name = "EKONOMIK_DURUM")
    private String ekonomikDurum;

    @Nationalized
    @Lob
    @Column(name = "OGRENIM_DURUMU")
    private String ogrenimDurumu;

    @Nationalized
    @Lob
    @Column(name = "SAGLIK_DURUMU")
    private String saglikDurumu;

    @Nationalized
    @Lob
    @Column(name = "SABIKA_DURUMU")
    private String sabikaDurumu;

    @Nationalized
    @Lob
    @Column(name = "AILE_VE_SOSYAL_YASAM")
    private String aileVeSosyalYasam;

    @Nationalized
    @Lob
    @Column(name = "KONUT_DURUMU")
    private String konutDurumu;

    @Nationalized
    @Lob
    @Column(name = "DEGERLENDIRME_VE_SONUC")
    private String degerlendirmeVeSonuc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEGERLENDIRME_YAPAN_PKPERSONEL_ID")
    private PkPersonel degerlendirmeYapanPkPersonel;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkAileSosyalInceleme other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}