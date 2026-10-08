package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 29.09.2026 10:46
 */
@Getter
@Setter
@Entity
@Table(name = "PKAILE_EGITIMI")
public class PkAileEgitimi extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -6878895859118145110L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKAILE_ID")
    private PkAile pkAile;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKEGITIM_KONU_ID")
    private PkEgitimKonu pkEgitimKonu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKPERSONEL_ID")
    private PkPersonel pkPersonel;

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
        if (!(object instanceof PkAileEgitimi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}