package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_KOLON")
public class GnlRaporKolon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_MODUL_ID")
    private GnlRaporModul gnlRaporModul;

    @Size(max = 100)
    @Nationalized
    @Column(name = "KOLON_ADI", length = 100)
    private String kolonAdi;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ALAN_ADI", length = 100)
    private String alanAdi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "VERI_TIPI", length = 50)
    private String veriTipi;

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

}