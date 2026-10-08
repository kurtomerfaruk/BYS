package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 29.09.2026 13:43
 */
@Getter
@Setter
@Entity
@Table(name = "PKNARANON")
public class PkNaranon extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 5349464500531938930L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @Nationalized
    @Lob
    @Column(name = "TOPLANTI_ICERIGI")
    private String toplantiIcerigi;

    @OneToMany(mappedBy = "pkNaranon",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    private List<PkNaranonaKatilanAile> pkNaranonaKatilanAileList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkNaranon other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}