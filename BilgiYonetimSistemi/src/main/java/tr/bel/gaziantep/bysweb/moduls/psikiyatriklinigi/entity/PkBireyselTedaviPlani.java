package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 25.09.2026 09:44
 */
@Getter
@Setter
@Entity
@Table(name = "PKBIREYSEL_TEDAVI_PLANI")
public class PkBireyselTedaviPlani extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 2287048878166698791L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKHASTA_ID")
    private PkHasta pkHasta;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @Nationalized
    @Lob
    @Column(name = "TEDAVI_SURECI")
    private String tedaviSureci;

    @Nationalized
    @Lob
    @Column(name = "GECMIS_TEDAVILERI")
    private String gecmisTedavileri;

    @Nationalized
    @Lob
    @Column(name = "EGITIM")
    private String egitim;

    @Nationalized
    @Lob
    @Column(name = "CALISMA")
    private String calisma;

    @Nationalized
    @Lob
    @Column(name = "SOKAK_YASAMI")
    private String sokakYasami;

    @Nationalized
    @Lob
    @Column(name = "YASAL_DURUM")
    private String yasalDurum;

    @Nationalized
    @Lob
    @Column(name = "TIBBI_OYKU")
    private String tibbiOyku;

    @Nationalized
    @Lob
    @Column(name = "MADDE_KULLANIMI")
    private String maddeKullanimi;

    @Nationalized
    @Lob
    @Column(name = "PSIKOLOJIK_TEST_SONUCLARI")
    private String psikolojikTestSonuclari;

    @Nationalized
    @Lob
    @Column(name = "DSM5_MADDE_KULLANIM_BOZUKLUGU_TANISI")
    private String dsm5MaddeKullanimBozukluguTanisi;

    @Nationalized
    @Lob
    @Column(name = "AMAC_HEDEF")
    private String amacHedef;

    @Nationalized
    @Lob
    @Column(name = "SORUN")
    private String sorun;

    @Nationalized
    @Lob
    @Column(name = "MUDAHALELER")
    private String mudahaleler;

    @Nationalized
    @Lob
    @Column(name = "AILE_DEMOGRAFIK_VE_SAGLIK_DURUMU")
    private String aileDemografikVeSaglikDurumu;

    @Nationalized
    @Lob
    @Column(name = "AILE_EKONOMIK_VE_KONUT_DURUMU")
    private String aileEkonomikVeKonutDurumu;

    @Nationalized
    @Lob
    @Column(name = "TOPLUMSAL_CEVRENIN_DURUMU")
    private String toplumsalCevreninDurumu;

    @Nationalized
    @Lob
    @Column(name = "HASTANIN_SAGLIK_DURUMU_VE_MADDE_KULLANIMI")
    private String hastaninSaglikDurumuVeMaddeKullanimi;

    @Nationalized
    @Lob
    @Column(name = "AILE_SORUN")
    private String aileSorun;

    @Nationalized
    @Lob
    @Column(name = "AILE_MUDAHALE")
    private String aileMudahale;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkBireyselTedaviPlani other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}