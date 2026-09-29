package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkEgitimTur;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 29.09.2026 10:46
 */
@Getter
@Setter
@Entity
@Table(name = "PKEGITIM_KONU")
@NamedQuery(name = "PkEgitimKonu.findByTur",query = "SELECT k FROM PkEgitimKonu k WHERE k.aktif=true AND k.tur=:tur ORDER BY k.tanim ASC")
public class PkEgitimKonu extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1664724442085646116L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 150)
    @NotNull
    @Nationalized
    @Column(name = "TANIM", nullable = false, length = 150)
    private String tanim;

    @Enumerated(EnumType.STRING)
    @Column(name = "TUR", length = 50)
    private EnumPkEgitimTur tur;

    @OneToMany(mappedBy = "pkEgitimKonu")
    private List<PkAileEgitimi> pkAileEgitimiList = new ArrayList<>();

    @OneToMany(mappedBy = "pkEgitimKonu")
    private List<PkAileTerapi> pkAileTerapiList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkEgitimKonu other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}