package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtFizikTedaviDurum;

import java.io.Serial;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 8.10.2025 15:29
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORTFIZIK_TEDAVI")
public class OrtFizikTedavi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -5623081892304348911L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTHASTA_ID")
    private OrtHasta ortHasta;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEDAVI_UYGULAYAN_ORTPERSONEL_ID")
    private OrtPersonel tedaviUygulayanOrtPersonel;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Column(name = "DURUM")
    @Enumerated(EnumType.STRING)
    private EnumOrtFizikTedaviDurum durum;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtFizikTedavi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}