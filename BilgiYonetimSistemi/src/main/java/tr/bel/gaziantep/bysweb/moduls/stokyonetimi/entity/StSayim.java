package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStSayimDurum;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 10:37
 */
@Getter
@Setter
@Entity
@Table(name = "STSAYIM")
public class StSayim extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -4904808644178599643L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SAYIM_NO", length = 50)
    private String sayimNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_ID")
    private StDepo stDepo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_LOKASYON_ID")
    private StDepoLokasyon stDepoLokasyon;

    @Column(name = "BASLANGIC_TARIHI")
    private LocalDateTime baslangicTarihi;

    @Column(name = "BITIS_TARIHI")
    private LocalDateTime bitisTarihi;

    @Size(max = 50)
    @Nationalized
    @Enumerated(EnumType.STRING)
    @Column(name = "DURUM", length = 50)
    private EnumStSayimDurum durum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SAYAN_KULLANICI_ID")
    private SyKullanici sayanKullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ONAYLAYAN_KULLANICI_ID")
    private SyKullanici onaylayanKullanici;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "stSayim", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StSayimKalem> stSayimKalemList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StSayim other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}