package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStTransferDurum;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 15:58
 */
@Getter
@Setter
@Entity
@Table(name = "STTRANSFER")
public class StTransfer extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 8960685907953385153L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "TRANSFER_NO", length = 50)
    private String transferNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KAYNAK_DEPO_ID")
    private StDepo kaynakDepo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HEDEF_DEPO_ID")
    private StDepo hedefDepo;

    @Enumerated(EnumType.STRING)
    @Column(name = "DURUM")
    private EnumStTransferDurum durum;

    @Column(name = "TALEP_TARIHI")
    private LocalDateTime talepTarihi;

    @Column(name = "ONAY_TARIHI")
    private LocalDateTime onayTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ONAYLAYAN_SYKULLANICI_ID")
    private SyKullanici onaylayanSyKullanici;

    @Column(name = "SEVK_TARIHI")
    private LocalDateTime sevkTarihi;

    @Column(name = "TESLIM_TARIHI")
    private LocalDateTime teslimTarihi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TESLIM_ALAN_SYKULLANICI_ID")
    private SyKullanici teslimAlanSyKullanici;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "stTransfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StTransferKalem> kalemler = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StTransfer other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}