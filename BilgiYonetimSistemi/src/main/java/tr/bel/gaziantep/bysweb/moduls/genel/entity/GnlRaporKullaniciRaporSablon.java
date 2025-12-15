package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_KULLANICI_RAPOR_SABLON")
public class GnlRaporKullaniciRaporSablon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_MODUL_ID")
    private GnlRaporModul gnlRaporModul;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYKULLANICI_ID")
    private SyKullanici syKullanici;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SABLON_ADI", length = 50)
    private String sablonAdi;

    @ColumnDefault("0")
    @Column(name = "GENEL")
    private boolean genel;

}