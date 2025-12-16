package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.12.2025 13:33
 */
@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_SABLON_KOLON")
@NamedQuery(name = "GnlRaporSablonKolon.findBySablon",query = "SELECT s FROM GnlRaporSablonKolon s WHERE s.aktif=true " +
        "AND s.gnlRaporKullaniciRaporSablon.id =:sablonId " +
        "ORDER BY s.siralama")
public class GnlRaporSablonKolon extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 3508196767593678810L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_KULLANICI_RAPOR_SABLON_ID")
    private GnlRaporKullaniciRaporSablon gnlRaporKullaniciRaporSablon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_KOLON_ID")
    private GnlRaporKolon gnlRaporKolon;

    @ColumnDefault("0")
    @Column(name = "SIRALAMA")
    private Integer siralama;

    @Column(name = "GENISLIK")
    private Integer genislik;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlRaporSablonKolon other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}