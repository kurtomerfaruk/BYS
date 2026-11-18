package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStIslemLogTuru;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 13:25
 */
@Getter
@Setter
@Entity
@Table(name = "STISLEM_LOG")
public class StIslemLog extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 324306307853349362L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ISLEM_TURU")
    private EnumStIslemLogTuru islemTuru;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYKULLANICI_ID")
    private SyKullanici syKullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_ID")
    private StDepo stDepo;

    @Size(max = 50)
    @Nationalized
    @Column(name = "BELGE_TURU", length = 50)
    private String belgeTuru;

    @Size(max = 50)
    @Nationalized
    @Column(name = "BELGE_NO", length = 50)
    private String belgeNo;

    @Size(max = 50)
    @Nationalized
    @Column(name = "IP_ADRESI", length = 50)
    private String ipAdresi;

    @Size(max = 500)
    @Nationalized
    @Column(name = "DETAY", length = 500)
    private String detay;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StIslemLog other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}