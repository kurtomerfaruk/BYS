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
@Table(name = "GNLRAPOR_MODUL_ENTITY_BAGLANTI")
public class GnlRaporModulEntityBaglanti extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_MODUL_ID")
    private GnlRaporModul gnlRaporModul;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ENTITY_ADI", length = 100)
    private String entityAdi;

    @Size(max = 200)
    @Nationalized
    @Column(name = "ENTITY_CLASS", length = 200)
    private String entityClass;

    @Size(max = 500)
    @Nationalized
    @Column(name = "JOIN_KOSULU", length = 500)
    private String joinKosulu;

    @Size(max = 50)
    @Nationalized
    @Column(name = "JOIN_TIPI", length = 50)
    private String joinTipi;

    @ColumnDefault("0")
    @Column(name = "SIRALAMA")
    private Integer siralama;

}