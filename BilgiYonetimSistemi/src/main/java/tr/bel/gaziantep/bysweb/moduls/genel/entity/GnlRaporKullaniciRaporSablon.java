package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.12.2025 13:33
 */
@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_KULLANICI_RAPOR_SABLON")
@NamedQuery(name = "GnlRaporKullaniciRaporSablon.findBySyKullaniciOrPublic",query = "SELECT k FROM GnlRaporKullaniciRaporSablon k WHERE k.aktif=true AND " +
        "(k.syKullanici = :syKullanici OR k.genel=true) " +
        "ORDER BY k.sablonAdi")
public class GnlRaporKullaniciRaporSablon extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -5974143069103944755L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_ID")
    private GnlRapor gnlRapor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYKULLANICI_ID")
    private SyKullanici syKullanici;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SABLON_ADI", length = 50)
    private String sablonAdi;

    @ColumnDefault("0")
    @Column(name = "GENEL")
    private boolean genel;

    @OneToMany(mappedBy = "gnlRaporKullaniciRaporSablon", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @SQLRestriction("AKTIF=true")
    private List<GnlRaporSablonKolon> gnlRaporSablonKolonList = new ArrayList<>();

    @OneToMany(mappedBy = "gnlRaporKullaniciRaporSablon", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @SQLRestriction("AKTIF=true")
    private List<GnlRaporSablonParametre> gnlRaporSablonParametreList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlRaporKullaniciRaporSablon other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}