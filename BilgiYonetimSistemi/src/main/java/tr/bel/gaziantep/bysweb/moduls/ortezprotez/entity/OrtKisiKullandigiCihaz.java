package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 24.11.2026 14:19
 */
@Getter
@Setter
@Entity
@Table(name = "ORTKISI_KULLANDIGI_CIHAZ")
public class OrtKisiKullandigiCihaz extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -7713537132111340890L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTKISI_ID")
    private OrtKisi ortKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTCIHAZ_ID")
    private OrtCihaz ortCihaz;

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
        if (!(object instanceof OrtKisiKullandigiCihaz other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}