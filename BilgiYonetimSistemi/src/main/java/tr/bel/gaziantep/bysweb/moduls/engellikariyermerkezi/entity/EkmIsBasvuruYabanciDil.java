package tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumPuanlama;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlYabanciDil;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "EKMIS_BASVURU_YABANCI_DIL")
public class EkmIsBasvuruYabanciDil extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -6225319102201237659L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EKMIS_BASVURU_ID")
    private EkmIsBasvuru ekmIsBasvuru;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLYABANCI_DIL_ID")
    private GnlYabanciDil gnlYabanciDil;

    @Enumerated(EnumType.STRING)
    @Column(name = "OKUMA")
    private EnumPuanlama okuma;

    @Enumerated(EnumType.STRING)
    @Column(name = "YAZMA")
    private EnumPuanlama yazma;

    @Enumerated(EnumType.STRING)
    @Column(name = "KONUSMA")
    private EnumPuanlama konusma;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EkmIsBasvuruYabanciDil other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}