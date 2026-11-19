package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStAlisBelgeTuru;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 16:10
 */
@Getter
@Setter
@Entity
@Table(name = "STALIS")
public class StAlis extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 8155211451536280519L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 20)
    @Nationalized
    @Enumerated(EnumType.STRING)
    @Column(name = "BELGE_TURU", length = 20)
    private EnumStAlisBelgeTuru belgeTuru;

    @Size(max = 50)
    @Nationalized
    @Column(name = "BELGE_NO", length = 50)
    private String belgeNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STTEDARIKCI_ID")
    private StTedarikci stTedarikci;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STDEPO_ID")
    private StDepo stDepo;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "stAlis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StAlisKalem> kalemler = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StAlis other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}