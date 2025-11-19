package tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity;

import jakarta.persistence.*;
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
 * @since 2.07.2025 14:01
 */
@Getter
@Setter
@Entity
@Table(name = "AYETKINLIK")
public class AyEtkinlik extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -4700150991822000663L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AYBIRIM_ID")
    private AyBirim ayBirim;

    @Column(name = "TARIH")
    private LocalDate tarih;

    @Size(max = 500)
    @Nationalized
    @Column(name = "KONU", length = 500)
    private String konu;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "ayEtkinlik", fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    private List<AyEtkinlikResim> ayEtkinlikResimList = new ArrayList<>();

    @OneToMany(mappedBy = "ayEtkinlik", fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    private List<AyEtkinlikKisi> ayEtkinlikKisiList =new ArrayList<>();

    @Transient
    @Getter(AccessLevel.NONE)
    private int kisiCount;

    public int getKisiCount() {
       return ayEtkinlikKisiList.size();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AyEtkinlik other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}