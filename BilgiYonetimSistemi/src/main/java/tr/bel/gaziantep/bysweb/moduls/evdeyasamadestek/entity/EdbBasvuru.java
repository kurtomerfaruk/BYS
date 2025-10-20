package tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.evdeyasamadestek.EnumEdbBasvuruDurumu;
import tr.bel.gaziantep.bysweb.core.enums.evdeyasamadestek.EnumEdbKisiTuru;
import tr.bel.gaziantep.bysweb.core.enums.evdeyasamadestek.EnumEdbOzelDurum;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYakinlikDerecesi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EDBBASVURU")
public class EdbBasvuru extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1384077361319063427L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLKISI_ID")
    private GnlKisi gnlKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYKISI_ID")
    private EyKisi eyKisi;

    @Column(name = "BASVURU_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumEdbBasvuruDurumu basvuruDurumu;

    @Nationalized
    @Lob
    @Column(name = "AILE_DURUMU_ACIKLAMA")
    private String aileDurumuAciklama;

    @Column(name = "AILEDEKI_ENGELLI_SAYISI")
    private Short ailedekiEngelliSayisi;

    @ColumnDefault("1")
    @Column(name = "ARAC_VAR_MI", nullable = false)
    private boolean aracVarMi;

    @ColumnDefault("1")
    @Column(name = "BAHCELI", nullable = false)
    private boolean bahceli;

    @ColumnDefault("1")
    @Column(name = "BALKONLU", nullable = false)
    private boolean balkonlu;

    @Column(name = "BASVURAN_KENDISI", nullable = false)
    private boolean basvuranKendisi;

    @Column(name = "BASVURU_TARIHI")
    private LocalDateTime basvuruTarihi;

    @Size(max = 500)
    @Nationalized
    @Column(name = "DURUM", length = 500)
    private String durum;

    @Column(name = "ENGEL_DURUMU", nullable = false)
    private boolean engelDurumu;


    @Size(max = 50)
    @Nationalized
    @Column(name = "ESYALARIN_GENEL_DURUMU", length = 50)
    private String esyalarinGenelDurumu;

    @Column(name = "EVIN_ALANI")
    private Short evinAlani;

    @Size(max = 50)
    @Nationalized
    @Column(name = "EVIN_GENEL_DURUMU", length = 50)
    private String evinGenelDurumu;

    @Size(max = 50)
    @Nationalized
    @Column(name = "HANEDE_YASAYANLARIN_KIYAFET_DURUMU", length = 50)
    private String hanedeYasayanlarinKiyafetDurumu;

    @Size(max = 50)
    @Nationalized
    @Column(name = "HANEDE_YASAYANLARIN_PSIKOLOJIK_DURUMU", length = 50)
    private String hanedeYasayanlarinPsikolojikDurumu;

    @Column(name = "HIZMET_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumEdbBasvuruDurumu hizmetDurumu;

    @Nationalized
    @Lob
    @Column(name = "HIZMETE_UYGUNLUK_ACIKLAMA")
    private String hizmeteUygunlukAciklama;

    @Nationalized
    @Lob
    @Column(name = "INCELEME_YAPANIN_GORUSU")
    private String incelemeYapaninGorusu;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KISI_YAKINI_ADI", length = 50)
    private String kisiYakiniAdi;

    @Nationalized
    @Lob
    @Column(name = "KISI_YAKINI_ADRES")
    private String kisiYakiniAdres;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KISI_YAKINI_SOYADI", length = 50)
    private String kisiYakiniSoyadi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KISI_YAKINI_TC_KIMLIK_NO", length = 50)
    private String kisiYakiniTcKimlikNo;

    @Column(name = "KISI_YAKINI_DOGUM_TARIHI")
    private LocalDate kisiYakiniDogumTarihi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KISI_YAKINI_TELEFON", length = 50)
    private String kisiYakiniTelefon;

    @Column(name = "KISI_YAKINI_YAKINLIK_DERECESI")
    @Enumerated(EnumType.STRING)
    private EnumGnlYakinlikDerecesi kisiYakiniYakinlikDerecesi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KISI_YAKINI_GNLILCE_ID")
    private GnlIlce kisiYakiniGnlIlce;

    @Column(name = "ODA_SAYISI")
    private Short odaSayisi;

    @Column(name = "OZEL_DURUM")
    @Enumerated(EnumType.STRING)
    private EnumEdbOzelDurum ozelDurum;

    @Size(max = 500)
    @Nationalized
    @Column(name = "REFERANS", length = 500)
    private String referans;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BASVURUYU_ALAN_EDBPERSONEL_ID")
    private EdbPersonel basvuruyuAlanEdbPersonel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INCELEME_YAPAN_EDBPERSONEL_ID")
    private EdbPersonel incelemeYapanEdbPersonel;

    @Column(name = "KISI_TURU")
    @Enumerated(EnumType.STRING)
    private EnumEdbKisiTuru kisiTuru;

    @Column(name = "SIGARA", nullable = false)
    private boolean sigara;

    @Nationalized
    @Lob
    @Column(name = "SIGARA_ACIKLAMA")
    private String sigaraAciklama;

    @Column(name = "ALKOL", nullable = false)
    private boolean alkol;

    @Nationalized
    @Lob
    @Column(name = "ALKOL_ACIKLAMA")
    private String alkolAciklama;

    @Column(name = "MADDE")
    private boolean madde;

    @OneToMany(mappedBy = "edbBasvuru", fetch = FetchType.LAZY)
    @SQLRestriction("AKTIF=true")
    @Builder.Default
    private List<EdbTalep> edbTalepList = new ArrayList<>();

    @Nationalized
    @Lob
    @Column(name = "MADDE_ACIKLAMA")
    private String maddeAciklama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EdbBasvuru other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}