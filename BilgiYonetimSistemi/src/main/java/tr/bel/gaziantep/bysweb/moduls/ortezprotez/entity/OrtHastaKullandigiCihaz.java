package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "ORTHASTA_KULLANDIGI_CIHAZ")
public class OrtHastaKullandigiCihaz extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 4180277909930374965L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTHASTA_ID")
    private OrtHasta ortHasta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTCIHAZ_ID")
    private OrtCihaz ortCihaz;

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
        if (!(object instanceof OrtHastaKullandigiCihaz other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}