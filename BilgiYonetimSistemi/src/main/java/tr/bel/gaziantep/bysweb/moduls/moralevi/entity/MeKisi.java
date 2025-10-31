package tr.bel.gaziantep.bysweb.moduls.moralevi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYakinlikDerecesi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.07.2025 14:09
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MEKISI")
@NamedQuery(name = "MeKisi.findByGnlKisi", query = "SELECT m FROM MeKisi m WHERE m.aktif=true AND m.eyKisi.gnlKisi =:gnlKisi")
@NamedQuery(name = "MeKisi.findByGirisYapmayanlar", query = "SELECT m FROM MeKisi m WHERE m.id NOT IN (SELECT c.meKisi.id FROM MeGirisCikis c WHERE c.cikisTarihi" +
        " IS NULL) AND m.aktif=true AND m.eyKisi.gnlKisi.durum=tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.SAG")
public class MeKisi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2516657658087328999L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "KAYIT_TARIHI")
    private LocalDateTime kayitTarihi;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "EYKISI_ID")
    private EyKisi eyKisi;

    @Size(max = 11)
    @Nationalized
    @Column(name = "REFAKATCI_TC_KIMLIK_NO", length = 11)
    private String refakatciTcKimlikNo;

    @Column(name = "REFAKATCI_DOGUM_TARIHI")
    private LocalDate refakatciDogumTarihi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "REFAKATCI_AD", length = 50)
    private String refakatciAd;

    @Size(max = 50)
    @Nationalized
    @Column(name = "REFAKATCI_SOYAD", length = 50)
    private String refakatciSoyad;

    @Nationalized
    @Lob
    @Column(name = "REFAKATCI_ADRES")
    private String refakatciAdres;

    @Size(max = 50)
    @Nationalized
    @Column(name = "REFAKATCI_TELEFON", length = 50)
    private String refakatciTelefon;

    @Column(name = "YAKINLIK")
    @Enumerated(EnumType.STRING)
    private EnumGnlYakinlikDerecesi yakinlik;

    @Nationalized
    @Lob
    @Column(name = "DIGER_ACIKLAMA")
    private String digerAciklama;

    @Nationalized
    @Lob
    @Column(name = "ANAMNEZ_RAPORU")
    private String anamnezRaporu;

    @Nationalized
    @Lob
    @Column(name = "IZLEME_YAPANIN_GORUSU")
    private String izlemeYapaninGorusu;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MeKisi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}