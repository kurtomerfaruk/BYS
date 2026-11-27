package tr.bel.gaziantep.bysweb.moduls.tabanlik.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabBelgeTuru;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabUcretTuru;

import java.io.Serial;

/**
 * Ücretli/ücretsiz süreçlerde hangi belgelerin zorunlu olduğunu tanımlayan yönetilebilir kural.
 *
 * @author OmerFarukKurt
 * @version 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TABBELGE_ZORUNLULUK")
public class TabBelgeZorunluluk extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -5127309481723650198L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "UCRET_TURU")
    private EnumTabUcretTuru ucretTuru;

    @Size(max = 60)
    @Nationalized
    @Column(name = "ISLEM", length = 60)
    private String islem;

    @Enumerated(EnumType.STRING)
    @Column(name = "BELGE_TURU")
    private EnumTabBelgeTuru belgeTuru;

    @ColumnDefault("1")
    @Column(name = "ZORUNLU", nullable = false)
    private boolean zorunlu;

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
        if (!(object instanceof TabBelgeZorunluluk other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
