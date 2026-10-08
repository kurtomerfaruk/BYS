package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 2.10.2026 11:40
 */
@Getter
@Setter
@Entity
@Table(name = "PKOPIYAT_YOKSUNLUK")
public class PkOpiyatYoksunluk extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -5973096496047648974L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKHASTA_ID")
    private PkHasta pkHasta;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @Column(name = "TOPLAM_PUAN")
    private Integer toplamPuan;

    @OneToMany(mappedBy = "pkOpiyatYoksunluk", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<PkOpiyatYoksunlukDetay> pkOpiyatYoksunlukDetayList = new ArrayList<>();

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