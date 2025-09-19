package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlAnketSoruTuru;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "GNLANKET_SORU")
public class GnlAnketSoru extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 286974867474562706L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "GNLANKET_ID", nullable = false)
    private GnlAnket gnlAnket;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "TANIM", nullable = false)
    private String tanim;

    @Column(name = "SORU_TURU")
    @Enumerated(EnumType.STRING)
    private EnumGnlAnketSoruTuru soruTuru;

    @ColumnDefault("0")
    @Column(name = "ZORUNLU")
    private boolean zorunlu;

    @ColumnDefault("0")
    @Column(name = "DIGER")
    private boolean diger;

    @OneToMany(mappedBy = "gnlAnketSoru", cascade = CascadeType.ALL)
    private List<GnlAnketSoruSecenek> gnlAnketSoruSecenekList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlAnketSoru other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}