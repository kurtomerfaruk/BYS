package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 29.09.2026 13:43
 */
@Getter
@Setter
@Entity
@Table(name = "PKNARANONA_KATILAN_AILE")
public class PkNaranonaKatilanAile extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 5608405055502359167L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKAILE_ID")
    private PkAile pkAile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKNARANON_ID")
    private PkNaranon pkNaranon;

    @ColumnDefault("0")
    @Column(name = "SECILI", nullable = false)
    private boolean secili;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkNaranonaKatilanAile other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}