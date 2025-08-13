package tr.bel.gaziantep.bysweb.moduls.engelsizler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyKullandigiCihaz;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "EYKISI_KULLANDIGI_CIHAZ")
public class EyKisiKullandigiCihaz extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -3437222226183108960L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYKISI_ID")
    private EyKisi eyKisi;

    @Column(name = "TANIM")
    @Enumerated(EnumType.STRING)
    private EnumEyKullandigiCihaz tanim;

    @Column(name = "SECILI")
    private boolean secili;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EyKisiKullandigiCihaz other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}