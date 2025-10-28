package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLRestriction;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbDurum;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:54
 */
@Getter
@Setter
@Entity
@Table(name = "EDBVERILECEK_HIZMET")
@NamedQuery(name = "EdbVerilecekHizmet.findByHizmetPlanlamaId", query = "SELECT e FROM EdbVerilecekHizmet e WHERE e.aktif=true AND " +
        "e.edbHizmetPlanlama =:edbHizmetPlanlama")
@NamedQuery(name = "EdbVerilecekHizmet.findByDurum", query = "SELECT e FROM EdbVerilecekHizmet e WHERE e.aktif=true AND " +
        "e.durum=:durum")
@NamedQuery(name = "EdbVerilecekHizmet.findByDurumMaxDate", query = "SELECT v FROM EdbVerilecekHizmet v " +
        "WHERE v.aktif=true AND v.durum = :durum AND " +
        "v.hizmetTarihi= (SELECT MAX(v1.hizmetTarihi) FROM EdbVerilecekHizmet v1 WHERE v1.aktif=true AND v1.durum = :durum AND v1.hizmetTarihi=v.hizmetTarihi) " +
        "AND NOT EXISTS (SELECT v2 FROM EdbVerilecekHizmet v2 WHERE v2.aktif=true AND v2.durum = :durum AND v2.hizmetTarihi=v.hizmetTarihi AND v2.hizmetTarihi>v.hizmetTarihi)")
public class EdbVerilecekHizmet extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 7570386044101187138L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EDBHIZMET_PLANLAMA_ID")
    private EdbHizmetPlanlama edbHizmetPlanlama;

    @Column(name = "DURUM")
    @Enumerated(EnumType.STRING)
    private EnumEdbDurum durum;

    @Column(name = "TARIH")
    private LocalDate tarih;

    @Column(name = "HIZMET_TARIHI")
    private LocalDate hizmetTarihi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "HIZMETTEN_MEMNUN", length = 50)
    private String hizmettenMemnun;

    @Nationalized
    @Lob
    @Column(name = "SIKAYET")
    private String sikayet;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @OneToMany(mappedBy = "edbVerilecekHizmet", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true AND SECILI=true")
    private List<EdbVerilecekHizmetPersonel> edbVerilecekHizmetPersonelList = new ArrayList<>();

    @OneToMany(mappedBy = "edbVerilecekHizmet", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @SQLRestriction("AKTIF=true")
    private List<EdbVerilecekHizmetResim> edbVerilecekHizmetResimList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EdbVerilecekHizmet other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}