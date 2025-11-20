package tr.bel.gaziantep.bysweb.moduls.moralevi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYakinlikDerecesi;
import tr.bel.gaziantep.bysweb.core.enums.moralevi.EnumMeTelefonKullanimDurumu;
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

    @Column(name = "TELEFON_KULLANIM_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumMeTelefonKullanimDurumu telefonKullanimDurumu;

    @Nationalized
    @Lob
    @Column(name = "SOSYAL_YASANTI_BILGILERI")
    private String sosyalYasantiBilgileri;

    @Nationalized
    @Lob
    @Column(name = "REFAKATCI_SAGLIK_DURUMU")
    private String refakatciSaglikDurumu;

    @Nationalized
    @Lob
    @Column(name = "REFAKATCI_EGITIM_VE_CALISMA_BILGILERI")
    private String refakatciEgitimVeCalismaBilgileri;

    @Nationalized
    @Lob
    @Column(name = "BAKIM_DESTEGINE_IHTIYAC_SEBEBI")
    private String bakimDestegineIhtiyacSebebi;

    @Nationalized
    @Lob
    @Column(name = "BAKIM_DESTEGINDEN_YARARLANMA_SURESI")
    private String bakimDestegindenYarlanmaSuresi;

    @Nationalized
    @Lob
    @Column(name = "YASADIGI_ORTAM_ACIKLAMA")
    private String yasadigiOrtamAciklama;

    @Column(name = "ILK_DEMANS_TANI_TARIHI")
    private LocalDate ilkDemansTaniTarihi;

    @Nationalized
    @Lob
    @Column(name = "TANIYI_ORTAYA_KOYAN_KURULUS")
    private String taniyiOrtayaKoyanKurulus;

    @Nationalized
    @Lob
    @Column(name = "GECIRMIS_OLDUGU_AMELIYATLAR")
    private String gecirmisOlduguAmeliyatlar;

    @Nationalized
    @Lob
    @Column(name = "HASTALIKLAR_KULLANDIGI_ILACLAR_ALERJI_DURUMU")
    private String hastaliklarKullandigiIlaclarAlerjiDurumu;

    @Nationalized
    @Lob
    @Column(name = "KULLANDIGI_YARDIMCI_ARAC_CIHAZ")
    private String kullandigiYardimciAracCihaz;

    @Column(name = "HASTANE_KONTROL_TARIHI")
    private LocalDate hastaneKontrolTarihi;

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