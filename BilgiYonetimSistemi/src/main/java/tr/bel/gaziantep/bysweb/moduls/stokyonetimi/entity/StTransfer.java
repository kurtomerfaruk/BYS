package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStTransferDurum;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 10:08
 */
@Getter
@Setter
@Entity
@Table(name = "STTRANSFER")
public class StTransfer extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -3172124658394668263L;

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

    @Column(name = "TALEP_TARIHI")
    private LocalDateTime talepTarihi;

    @Column(name = "SEVK_TARIHI")
    private LocalDateTime sevkTarihi;

    @Column(name = "TESLIM_TARIHI")
    private LocalDateTime teslimTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name = "DURUM")
    private EnumStTransferDurum durum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TALEP_EDEN_SYKULLANICI_ID")
    private SyKullanici talepEdenSyKullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ONAYLAYAN_SYKULLANICI_ID")
    private SyKullanici onaylayanSyKullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEVK_EDEN_SYKULLANICI_ID")
    private SyKullanici sevkEdenSyKullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TESLIM_ALAN_GNLPERSONEL_ID")
    private GnlPersonel teslimAlanGnlPersonel;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "stTransfer", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<StTransferKalem> stTransferKalemList = new ArrayList<>();

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