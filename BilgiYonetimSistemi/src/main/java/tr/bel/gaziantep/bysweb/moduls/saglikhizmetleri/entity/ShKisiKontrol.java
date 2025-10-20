package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity;

import jakarta.persistence.*;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.07.2025 13:12
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SHKISI_KONTROL")
@NamedQuery(name = "ShKisiKontrol.findByGnlKisi", query = "SELECT s FROM ShKisiKontrol s WHERE s.aktif=true " +
        "AND s.shKisi.gnlKisi=:gnlKisi ORDER BY s.tarih DESC")
public class ShKisiKontrol extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2142134099026046341L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHKISI_ID")
    private ShKisi shKisi;

    @Column(name = "TARIH")
    private LocalDate tarih;

    @Column(name = "BOY", precision = 18, scale = 2)
    private BigDecimal boy;

    @Column(name = "KILO", precision = 18, scale = 2)
    private BigDecimal kilo;

    @Column(name = "BEL_CEVRESI", precision = 18, scale = 2)
    private BigDecimal belCevresi;

    @Column(name = "BKI", precision = 18, scale = 2)
    private BigDecimal bki;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ShKisiKontrol other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}