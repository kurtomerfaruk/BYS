package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlRaporParametreTipi;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_PARAMETRE")
public class GnlRaporParametre extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -5217815469027841446L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_MODUL_ID")
    private GnlRaporModul gnlRaporModul;

    @Size(max = 50)
    @Nationalized
    @Column(name = "PARAMETRE_ADI", length = 50)
    private String parametreAdi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "GORUNUR_ADI", length = 50)
    private String gorunurAdi;

    @Column(name = "PARAMETRE_TIPI")
    @Enumerated(EnumType.STRING)
    private EnumGnlRaporParametreTipi parametreTipi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "VERI_TIPI", length = 50)
    private String veriTipi;

    @ColumnDefault("0")
    @Column(name = "ZORUNLU")
    private boolean zorunlu;

    @Size(max = 200)
    @Nationalized
    @Column(name = "VARSAYILAN_DEGER", length = 200)
    private String varsayilanDeger;

    @Size(max = 50)
    @Nationalized
    @Column(name = "VARSAYILAN_OPERATOR", length = 50)
    private String varsayilanOperator;

    @Size(max = 500)
    @Nationalized
    @Column(name = "SQL_KOSUL", length = 500)
    private String sqlKosul;

    @Size(max = 500)
    @Nationalized
    @Column(name = "LOOKUP_QUERY", length = 500)
    private String lookupQuery;

    @Size(max = 500)
    @Nationalized
    @Column(name = "LOOKUP_ENUM_CLASS", length = 500)
    private String lookupEnumClass;

    @ColumnDefault("0")
    @Column(name = "SIRALAMA")
    private Integer siralama;



}