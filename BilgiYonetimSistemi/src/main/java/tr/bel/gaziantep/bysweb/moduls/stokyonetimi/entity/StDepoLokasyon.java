package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "STDEPO_LOKASYON")
public class StDepoLokasyon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KOD", length = 50)
    private String kod;

    @Size(max = 150)
    @Nationalized
    @Column(name = "AD", length = 150)
    private String ad;

    @Size(max = 30)
    @Nationalized
    @Column(name = "LOKASYON_TIPI", length = 30)
    private String lokasyonTipi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_ID")
    private StDepo stDepo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UST_LOKASYON_ID")
    private StDepoLokasyon ustLokasyon;

    @Column(name = "SIRA_NO")
    private Integer siraNo;

    @Size(max = 300)
    @Nationalized
    @Column(name = "ACIKLAMA", length = 300)
    private String aciklama;


}