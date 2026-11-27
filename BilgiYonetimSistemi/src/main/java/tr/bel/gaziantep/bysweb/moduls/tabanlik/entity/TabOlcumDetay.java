package tr.bel.gaziantep.bysweb.moduls.tabanlik.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabAyakTarafi;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:25
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TABOLCUM_DETAY")
public class TabOlcumDetay extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 702876615316790266L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABOLCUM_ID")
    private TabOlcum tabOlcum;

    @Enumerated(EnumType.STRING)
    @Column(name = "AYAK_TARAFI")
    private EnumTabAyakTarafi ayakTarafi;

    @Column(name = "AYAK_NUMARASI", precision = 10, scale = 2)
    private BigDecimal ayakNumarasi;

    @Column(name = "UZUNLUK", precision = 10, scale = 3)
    private BigDecimal uzunluk;

    @Column(name = "GENISLIK", precision = 10, scale = 3)
    private BigDecimal genislik;

    @Column(name = "TOPUK_GENISLIGI", precision = 10, scale = 3)
    private BigDecimal topukGenisligi;

    @Column(name = "KAVIS_YUKSEKLIGI", precision = 10, scale = 3)
    private BigDecimal kavisYuksekligi;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TabOlcumDetay other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}