package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.time.LocalDate;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.11.2025 16:13
 */
@Getter
@Setter
@Entity
@Table(name = "EDBMEMNUNIYET_ANKETI")
public class EdbMemnuniyetAnketi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6914103909078537681L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "TARIH")
    private LocalDate tarih;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLKISI_ID")
    private GnlKisi gnlKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANKET_YAPAN_EDBPERSONEL_ID")
    private EdbPersonel anketYapanEdbPersonel;

    @Column(name = "EV_TEMIZLIGI")
    private Integer evTemizligi;

    @Nationalized
    @Lob
    @Column(name = "EV_TEMIZLIGI_ACIKLAMA")
    private String evTemizligiAciklama;

    @Column(name = "KISISEL_BAKIM")
    private Integer kisiselBakim;

    @Nationalized
    @Lob
    @Column(name = "KISISEL_BAKIM_ACIKLAMA")
    private String kisiselBakimAciklama;

    @Column(name = "BOYA_BADANA")
    private Integer boyaBadana;

    @Nationalized
    @Lob
    @Column(name = "BOYA_BADANA_ACIKLAMA")
    private String boyaBadanaAciklama;

    @Column(name = "TADILAT_BAKIM")
    private Integer tadilatBakim;

    @Nationalized
    @Lob
    @Column(name = "TADILAT_BAKIM_ACIKLAMA")
    private String tadilatBakimAciklama;

    @Column(name = "PERSONEL_TUTUM_DAVRANIS")
    private Integer personelTutumDavranis;

    @Nationalized
    @Lob
    @Column(name = "PERSONEL_TUTUM_DAVRANIS_ACIKLAMA")
    private String personelTutumDavranisAciklama;

    @Column(name = "PERSONEL_USLUP")
    private Integer personelUslup;

    @Nationalized
    @Lob
    @Column(name = "PERSONEL_USLUP_ACIKLAMA")
    private String personelUslupAciklama;

    @Column(name = "GUVENDE_HISSETME")
    private Integer guvendeHissetme;

    @Nationalized
    @Lob
    @Column(name = "GUVENDE_HISSETME_ACIKLAMA")
    private String guvendeHissetmeAciklama;

    @Column(name = "MALZEME_YETERLILIGI")
    private Integer malzemeYeterliligi;

    @Nationalized
    @Lob
    @Column(name = "MALZEME_YETERLILIGI_ACIKLAMA")
    private String malzemeYeterliligiAciklama;

    @Column(name = "ILETISIM_KURAN_PERSONEL")
    private Integer iletisimKuranPersonel;

    @Nationalized
    @Lob
    @Column(name = "ILETISIM_KURAN_PERSONEL_ACIKLAMA")
    private String iletisimKuranPersonelAciklama;

    @Nationalized
    @Lob
    @Column(name = "DILEK_ONERI_SIKAYET")
    private String dilekOneriSikayet;


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EdbMemnuniyetAnketi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}