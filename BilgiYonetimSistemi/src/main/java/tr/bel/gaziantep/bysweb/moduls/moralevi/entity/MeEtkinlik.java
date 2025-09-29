package tr.bel.gaziantep.bysweb.moduls.moralevi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.07.2025 15:01
 */

@Getter
@Setter
@Entity
@Table(name = "MEETKINLIK")
public class MeEtkinlik extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -5114199133089413310L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "TARIH", nullable = false)
    private LocalDate tarih;

    @Size(max = 300)
    @Nationalized
    @Column(name = "KONU", length = 300)
    private String konu;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "meEtkinlik", fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    private List<MeEtkinlikKisi> meEtkinlikKisiList = new ArrayList<>();

    @OneToMany(mappedBy = "meEtkinlik", fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    private List<MeEtkinlikResim> meEtkinlikResimList = new ArrayList<>();

    @Transient
    @Getter(AccessLevel.NONE)
    private int kisiCount;

    public int getKisiCount() {
        return meEtkinlikKisiList.size();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MeEtkinlik other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}