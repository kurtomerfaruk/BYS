package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumEvetHayir;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumVarYok;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.*;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 22.09.2026 14:18
 */
@Getter
@Setter
@Entity
@Table(name = "PKHASTA")
@NamedQuery(name = "PkHasta.findByKisiTcKimlikNo", query = "SELECT e FROM PkHasta e WHERE e.aktif=true AND e.gnlKisi.tcKimlikNo = :tcKimlikNo")
public class PkHasta extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -7641877366459729678L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "GNLKISI_ID")
    private GnlKisi gnlKisi;

    @Column(name = "BASVURU_TARIHI")
    private LocalDateTime basvuruTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name = "AILE_GOC_ILE_GELDI")
    private EnumEvetHayir aileGocIleGeldi;

    @Nationalized
    @Lob
    @Column(name = "AILE_GOC_ILE_GELDI_ACIKLAMA")
    private String aileGocIleGeldiAciklama;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ANNE_KIZLIK_SOYADI", length = 50)
    private String anneKizlikSoyadi;

    @Enumerated(EnumType.STRING)
    @Column(name = "SINIFTA_KALMA")
    private EnumVarYok siniftaKalma;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISIPLIN_SUCU")
    private EnumVarYok disiplinSucu;

    @Enumerated(EnumType.STRING)
    @Column(name = "OKULDA_MADDE_KULLANIMI")
    private EnumVarYok okuldaMaddeKullanimi;

    @Enumerated(EnumType.STRING)
    @Column(name = "OKULA_DEVAM_ETMEME_NEDENI")
    private EnumPkOkulaDevamEtmemeNedeni okulaDevamEtmemeNedeni;

    @Nationalized
    @Lob
    @Column(name = "OKULA_DEVAM_ETMEME_NEDENI_ACIKLAMA")
    private String okulaDevamEtmemeNedeniAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "MADDE_KULLANIMININ_OKULU_BIRAKMASINDA_ETKISI_VAR_MI")
    private EnumVarYok maddeKullanimininOkuluBirakmasindaEtkisiVarMi;

    @Enumerated(EnumType.STRING)
    @Column(name = "IS_YASAMI")
    private EnumVarYok isYasami;

    @Size(max = 50)
    @Nationalized
    @Column(name = "EN_UZUN_CALISMA_SURESI", length = 50)
    private String enUzunCalismaSuresi;

    @Enumerated(EnumType.STRING)
    @Column(name = "MESLEKI_YETERLILIK_VAR_MI")
    private EnumVarYok meslekiYeterlilikVarMi;

    @Nationalized
    @Lob
    @Column(name = "MESLEKI_YETERLILIK_VAR_MI_ACIKLAMA")
    private String meslekiYeterlilikVarMiAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "NEREDE_VE_KIMLERLE_YASIYOR")
    private EnumPkNeredeKimlerleYasiyor neredeVeKimlerleYasiyor;

    @Nationalized
    @Lob
    @Column(name = "NEREDE_VE_KIMLERLE_YASIYOR_ACIKLAMA")
    private String neredeVeKimlerleYasiyorAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "SOKAK_YASAMI_VAR_MI")
    private EnumVarYok sokakYasamiVarMi;

    @Nationalized
    @Lob
    @Column(name = "SOKAK_YASAMI_VAR_MI_ACIKLAMA")
    private String sokakYasamiVarMiAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "BASVURU_SEKLI")
    private EnumPkBasvuruSekli basvuruSekli;

    @Nationalized
    @Lob
    @Column(name = "BASVURU_SEKLI_ACIKLAMA")
    private String basvuruSekliAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "SIGARA_KULLANIMI")
    private EnumVarYok sigaraKullanimi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EN_SIK_KULLANILAN_PKMADDE_ID_1")
    private PkMadde enSikKullanilanMadde1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EN_SIK_KULLANILAN_PKMADDE_ID_2")
    private PkMadde enSikKullanilanMadde2;

    @Column(name = "MADDEYE_BASLAMA_YASI")
    private Integer maddeyeBaslamaYasi;

    @Enumerated(EnumType.STRING)
    @Column(name = "MADDEYE_BASLAMA_SEBEBI")
    private EnumPkMaddeyeBaslamaSebebi maddeyeBaslamaSebebi;

    @Nationalized
    @Lob
    @Column(name = "MADDEYE_BASLAMA_SEBEBI_ACIKLAMA")
    private String maddeyeBaslamaSebebiAciklama;

    @Nationalized
    @Lob
    @Column(name = "MADDE_KULLANIM_MIKTARI_VE_YONTEMI")
    private String maddeKullanimMiktariVeYontemi;

    @Nationalized
    @Lob
    @Column(name = "YOKSUNLUK_ENTOKSIKASYON_REMISYON")
    private String yoksunlukEntoksikasyonRemisyon;

    @Enumerated(EnumType.STRING)
    @Column(name = "AILE_DURUM_BILGISI")
    private EnumPkAileDurum aileDurumBilgisi;

    @Nationalized
    @Lob
    @Column(name = "AILE_DURUM_BILGISI_ACIKLAMA")
    private String aileDurumBilgisiAciklama;

    @Size(max = 50)
    @Nationalized
    @Column(name = "AILEDE_MADDE_KULLANIMI_OLAN_BIREYLER", length = 50)
    private String ailedeMaddeKullanimiOlanBireyler;

    @Size(max = 50)
    @Nationalized
    @Column(name = "AILEDE_ALKOL_KULLANIMI_OLAN_BIREY", length = 50)
    private String ailedeAlkolKullanimiOlanBirey;

    @Size(max = 50)
    @Nationalized
    @Column(name = "AILEDE_INTIHAR_OYKUSU", length = 50)
    private String ailedeIntiharOykusu;

    @Nationalized
    @Lob
    @Column(name = "AILEDE_INTIHAR_OYKUSU_ACIKLAMA")
    private String ailedeIntiharOykusuAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "INTIHAR_GIRISIMI")
    private EnumPkIntiharGirisimi intiharGirisimi;

    @Nationalized
    @Lob
    @Column(name = "INTIHAR_GIRISIMI_ACIKLAMA")
    private String intiharGirisimiAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "HERHANGI_BIR_SUC_ISLEDI_MI")
    private EnumPkSucIsleme herhangiBirSucIslediMi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "HERHANGI_BIR_SUC_ISLEDI_MI_ACIKLAMA", length = 50)
    private String herhangiBirSucIslediMiAciklama;

    @Size(max = 50)
    @Nationalized
    @Column(name = "DEVAM_EDEN_MAHKEMESI_VAR_MI", length = 50)
    private String devamEdenMahkemesiVarMi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "CEZAEVI_GECMISI_VAR_MI", length = 50)
    private String cezaeviGecmisiVarMi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "CEZAEVI_GECMISI_SURESI", length = 50)
    private String cezaeviGecmisiSuresi;

    @Enumerated(EnumType.STRING)
    @Column(name = "AILEDE_SUC_OYKUSU")
    private EnumVarYok ailedeSucOykusu;

    @Enumerated(EnumType.STRING)
    @Column(name = "AILEDE_SUC_OYKUSU_ACIKLAMA")
    private EnumPkAileBirey ailedeSucOykusuAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "FIZIKSEL_BIR_HASTALIGI_VAR_MI")
    private EnumVarYok fizikselBirHastaligiVarMi;

    @Nationalized
    @Lob
    @Column(name = "FIZIKSEL_BIR_HASTALIGI_VAR_MI_ACIKLAMA")
    private String fizikselBirHastaligiVarMiAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "BEDENINDE_ANOMALI_VAR_MI")
    private EnumVarYok bedenindeAnomaliVarMi;

    @Nationalized
    @Lob
    @Column(name = "BEDENINDE_ANOMALI_VAR_MI_ACIKLAMA")
    private String bedenindeAnomaliVarMiAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "BEDENINDE_KESI_IZI_VAR_MI")
    private EnumVarYok bedenindeKesiIziVarMi;

    @Enumerated(EnumType.STRING)
    @Column(name = "BEDENINDE_KESI_IZI_VAR_MI_ACIKLAMA")
    private EnumPkBeden bedenindeKesiIziVarMiAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "BEDENINDE_KESI_IZI_SIDDETI")
    private EnumPkKesiSiddeti bedenindeKesiIziSiddeti;

   @Enumerated(EnumType.STRING)
    @Column(name = "BEDENINDE_DOVME_VAR_MI")
    private EnumVarYok bedenindeDovmeVarMi;

    @Nationalized
    @Lob
    @Column(name = "PSIKIYATRIK_MUAYENE")
    private String psikiyatrikMuayene;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PSIKIYATRIK_MUAYENE_YAPAN_PKPERSONEL_ID")
    private PkPersonel psikiyatrikMuayeneYapanPersonel;

    @OneToMany(mappedBy = "pkHasta", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    private List<PkHastaMaddeKullanimi> pkHastaMaddeKullanimiList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkHasta other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}