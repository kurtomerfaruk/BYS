package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity;

import jakarta.persistence.*;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.saglikhizmetleri.EnumShDanismanlikHizmeti;
import tr.bel.gaziantep.bysweb.core.enums.saglikhizmetleri.EnumShObeziteHizmet;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SHKISI")
@NamedQuery(name = "ShKisi.existByGnlKisiByShKisi",query = "SELECT s FROM ShKisi s WHERE s.aktif=true AND s.gnlKisi=:gnlKisi AND s.id!=:id")
@NamedQuery(name = "ShKisi.existByGnlKisi",query = "SELECT s FROM ShKisi s WHERE s.aktif=true AND s.gnlKisi.id=:gnlKisiId")
public class ShKisi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 2544719350517774480L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "GNLKISI_ID")
    private GnlKisi gnlKisi;

    @Column(name = "GELIS_TARIHI")
    private LocalDateTime gelisTarihi;

    @Column(name = "DANISMANLIK_HIZMETI")
    @Enumerated(EnumType.STRING)
    private EnumShDanismanlikHizmeti danismanlikHizmeti;

    @Column(name = "VERILEN_HIZMET")
    @Enumerated(EnumType.STRING)
    private EnumShObeziteHizmet verilenHizmet;

    @OneToMany(mappedBy = "shKisi")
    private List<ShKisiKanTahlilSonuc> shKisiKanTahlilSonucList = new ArrayList<>();

    @OneToMany(mappedBy = "shKisi")
    private List<ShKisiKontrol> shKisiKontrolList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ShKisi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}