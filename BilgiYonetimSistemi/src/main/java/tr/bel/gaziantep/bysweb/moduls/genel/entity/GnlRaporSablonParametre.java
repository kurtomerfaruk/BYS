package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_SABLON_PARAMETRE")
public class GnlRaporSablonParametre extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_KULLANICI_RAPOR_SABLON_ID")
    private GnlRaporKullaniciRaporSablon gnlRaporKullaniciRaporSablon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_PARAMETRE_ID")
    private GnlRaporParametre gnlRaporParametre;

    @Size(max = 50)
    @Nationalized
    @Column(name = "DEGER", length = 50)
    private String deger;

    @Size(max = 50)
    @Nationalized
    @Column(name = "OPERATOR", length = 50)
    private String operator;

    @Size(max = 500)
    @Nationalized
    @Column(name = "IKINCI_DEGER", length = 500)
    private String ikinciDeger;

}