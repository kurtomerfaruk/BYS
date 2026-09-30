package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkTalepTuru;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 29.09.2026 15:08
 */
@Getter
@Setter
@Entity
@Table(name = "PKTALEP")
public class PkTalep extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2865162296036588697L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKAILE_ID")
    private PkAile pkAile;

    @Enumerated(EnumType.STRING)
    @Column(name = "TALEP_TURU")
    private EnumPkTalepTuru talepTuru;

    @Enumerated(EnumType.STRING)
    @Column(name = "DURUM")
    private EnumGnlTalepDurumu durum;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkTalep other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}