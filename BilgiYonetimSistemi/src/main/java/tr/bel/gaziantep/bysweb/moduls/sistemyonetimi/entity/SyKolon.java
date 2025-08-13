package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreEslesmeModu;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreTuru;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyVeriTipi;

import java.io.Serial;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SYKOLON")
@NamedQuery(name = "SyKolon.findByTabloAdi", query = "SELECT k FROM SyKolon k WHERE k.aktif=true AND k.syTablo.tabloAdi=:tabloAdi")
public class SyKolon extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 4514629672699225170L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYTABLO_ID")
    private SyTablo syTablo;

    @Size(max = 150)
    @Nationalized
    @Column(name = "BASLIK", length = 150)
    private String baslik;

    @Size(max = 150)
    @Nationalized
    @Column(name = "COLUMN_PROPERTY", length = 150)
    private String columnProperty;

    @Size(max = 150)
    @Nationalized
    @Column(name = "FILTER_PROPERTY", length = 150)
    private String filterProperty;

    @Size(max = 150)
    @Nationalized
    @Column(name = "SORT_PROPERTY", length = 150)
    private String sortProperty;

    @Column(name = "FILTRE_ANAHTARI")
    @Enumerated(EnumType.STRING)
    private EnumSyFiltreAnahtari filtreAnahtari;

    @Column(name = "FILTRE_ESLESME_MODU")
    @Enumerated(EnumType.STRING)
    private EnumSyFiltreEslesmeModu filtreEslesmeModu;

    @Column(name = "FILTRE_TURU")
    @Enumerated(EnumType.STRING)
    private EnumSyFiltreTuru filtreTuru;

    @Column(name = "TIP")
    @Enumerated(EnumType.STRING)
    private EnumSyVeriTipi tip;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SyKolon other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}