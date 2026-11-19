package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 09:21
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "STDEPO_YETKI")
@NamedQuery(name = "StDepoYetki.findByKullaniciByDepo", query = "SELECT d FROM StDepoYetki d WHERE d.aktif=true AND d.syKullanici=:syKullanici " +
        "AND d.stDepo=:stDepo")
public class StDepoYetki extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2478096516068572318L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYKULLANICI_ID")
    private SyKullanici syKullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_ID")
    private StDepo stDepo;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StDepoYetki other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}