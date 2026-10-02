package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 01.10.2026 16:27
 */
@Getter
@Setter
@Entity
@Table(name = "PKOPIYAT_YOKSUNLUK")
public class PkOpiyatYoksunluk extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2354908617758173419L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKHASTA_ID")
    private PkHasta pkHasta;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @Size(max = 100)
    @Nationalized
    @Column(name = "DINLENME_HALINDEKI_NABIZ", length = 100)
    private String dinlenmeHalindekiNabiz;

    @Size(max = 100)
    @Nationalized
    @Column(name = "TERLEME", length = 100)
    private String terleme;

    @Size(max = 100)
    @Nationalized
    @Column(name = "YERINDE_DURAMAMA", length = 100)
    private String yerindeDuramama;

    @Size(max = 100)
    @Nationalized
    @Column(name = "GOZ_BEBEKLERININ_BOYUTU", length = 100)
    private String gozBebeklerininBoyutu;

    @Size(max = 100)
    @Nationalized
    @Column(name = "KEMIK_EKLEM_AGRISI", length = 100)
    private String kemikEklemAgrisi;

    @Size(max = 100)
    @Nationalized
    @Column(name = "BURUN_AKINTISI_GOZ_YASARMASI", length = 100)
    private String burunAkintisiGozYasarmasi;

    @Size(max = 100)
    @Nationalized
    @Column(name = "SINDIRIM_SISTEMI_RAHATSIZLARI", length = 100)
    private String sindirimSistemiRahatsizlari;

    @Size(max = 100)
    @Nationalized
    @Column(name = "TITREME", length = 100)
    private String titreme;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ESNEME", length = 100)
    private String esneme;

    @Size(max = 100)
    @Nationalized
    @Column(name = "BUNALTI_SINIRLILIK", length = 100)
    private String bunaltiSinirlilik;

    @Size(max = 100)
    @Nationalized
    @Column(name = "KAZ_DERISI_GORUNUMU", length = 100)
    private String kazDerisiGorunumu;

    @Column(name = "TOPLAM_PUAN")
    private Integer toplamPuan;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkOpiyatYoksunluk other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}