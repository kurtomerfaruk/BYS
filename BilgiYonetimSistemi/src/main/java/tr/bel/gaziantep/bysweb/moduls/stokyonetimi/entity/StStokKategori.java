package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 11:26
 */
@Getter
@Setter
@Entity
@Table(name = "STSTOK_KATEGORI")
public class StStokKategori extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1004731398354889776L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KATEGORI_KODU", length = 50)
    private String kategoriKodu;

    @Size(max = 150)
    @Nationalized
    @Column(name = "KATEGORI_ADI", length = 150)
    private String kategoriAdi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UST_KATEGORI_ID")
    private StStokKategori ustKategori;

    @OneToMany(mappedBy = "ustKategori")
    private List<StStokKategori> ststokKategoris = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StStokKategori other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}