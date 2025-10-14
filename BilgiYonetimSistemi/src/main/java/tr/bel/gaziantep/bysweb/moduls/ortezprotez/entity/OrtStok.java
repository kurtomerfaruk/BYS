package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtStokBirim;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.10.2025 13:57
 */

@Getter
@Setter
@Entity
@Table(name = "ORTSTOK")
public class OrtStok extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 3053078028881473005L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Nationalized
    @Column(name = "MARKA", length = 100)
    private String marka;

    @Size(max = 50)
    @Nationalized
    @Column(name = "STOK_KODU", length = 50)
    private String stokKodu;

    @Size(max = 150)
    @Nationalized
    @Column(name = "STOK_ADI", length = 150)
    private String stokAdi;

    @Column(name = "MIKTAR", precision = 18, scale = 2)
    private BigDecimal miktar;

    @Column(name = "ALIS_FIYATI", precision = 18, scale = 2)
    private BigDecimal alisFiyati;

    @Column(name = "SATIS_FIYATI", precision = 18, scale = 2)
    private BigDecimal satisFiyati;

    @Column(name = "BIRIM")
    @Enumerated(EnumType.STRING)
    private EnumOrtStokBirim birim;

    @OneToMany(mappedBy = "ortStok")
    private List<OrtStokFiyatHareket> ortStokFiyatHareketList = new ArrayList<>();

    @OneToMany(mappedBy = "ortStok")
    private List<OrtStokHareket> ortStokHareketList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtStok other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}