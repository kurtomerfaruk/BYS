package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "GNLANKET_CEVAP")
@NamedQuery(name = "GnlAnketCevap.findByGnlAnketIdBySyKullaniciId",query = "SELECT c FROM GnlAnketCevap c " +
        "WHERE c.aktif=true AND c.syKullanici.id=:syKullaniciId AND c.gnlAnket.id=:gnlAnketId")
@NamedQuery(name = "GnlAnketCevap.findByGnlAnketIdByTcKimlikNo",query = "SELECT c FROM GnlAnketCevap c " +
        "WHERE c.aktif=true AND c.syKullanici.gnlPersonel.gnlKisi.tcKimlikNo=:tcKimlikNo AND c.gnlAnket.id=:gnlAnketId")
@NamedQuery(name = "GnlAnketCevap.findByGnlAnketIdByToken",query = "SELECT c FROM GnlAnketCevap c " +
        "WHERE c.aktif=true AND c.token=:token AND c.gnlAnket.id=:gnlAnketId")
public class GnlAnketCevap extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6358478616267998887L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "GNLANKET_ID", nullable = false)
    private GnlAnket gnlAnket;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SYKULLANICI_ID")
    private SyKullanici syKullanici;

    @Size(max = 11)
    @Nationalized
    @Column(name = "TC_KIMLIK_NO", length = 11)
    private String tcKimlikNo;

    @Column(name = "DOGUM_TARIHI")
    private LocalDate dogumTarihi;

    @Size(max = 150)
    @Nationalized
    @Column(name = "TOKEN", length = 150)
    private String token;

    @OneToMany(mappedBy = "gnlAnketCevap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GnlAnketCevapDetay> gnlAnketCevapDetayList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlAnketCevap other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}