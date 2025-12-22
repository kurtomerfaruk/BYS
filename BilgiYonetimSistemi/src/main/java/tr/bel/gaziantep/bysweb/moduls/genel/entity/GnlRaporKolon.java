package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyVeriTipi;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.12.2025 13:33
 */
@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_KOLON")
public class GnlRaporKolon extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 541961467185749990L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_ID")
    private GnlRapor gnlRapor;

    @Size(max = 100)
    @Nationalized
    @Column(name = "KOLON_ADI", length = 100)
    private String kolonAdi;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ALAN_ADI", length = 100)
    private String alanAdi;

    @Column(name = "VERI_TIPI")
    @Enumerated(EnumType.STRING)
    private EnumSyVeriTipi veriTipi;


    @Size(max = 100)
    @Nationalized
    @Column(name = "FORMAT", length = 100)
    private String format;

    @Size(max = 150)
    @Nationalized
    @Column(name = "GORUNUR_ADI", length = 150)
    private String gorunurAdi;

    @ColumnDefault("1")
    @Column(name = "SIRALANABILIR")
    private boolean siralanabilir;

    @ColumnDefault("1")
    @Column(name = "FILTRELENEBILIR")
    private boolean filtrelenebilir;

    @ColumnDefault("0")
    @Column(name = "VARSAYILAN")
    private boolean varsayilan;

    @ColumnDefault("0")
    @Column(name = "SIRALAMA")
    private Integer siralama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlRaporKolon other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}