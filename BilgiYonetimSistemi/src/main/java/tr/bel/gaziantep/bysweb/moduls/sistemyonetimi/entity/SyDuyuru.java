package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyDuyuruTur;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.08.2025 08:57
 */
@Getter
@Setter
@Entity
@Table(name = "SYDUYURU")
@NamedQuery(name = "SyDuyuru.findAllPublic", query = "SELECT s FROM SyDuyuru s WHERE s.aktif=true AND s.onaylandi=true AND " +
        "s.duyuruTur=tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyDuyuruTur.GENEL ORDER BY s.yayinlanmaTarihi DESC")
public class SyDuyuru extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -4485800443470875865L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 150)
    @Nationalized
    @Column(name = "BASLIK", length = 150)
    private String baslik;

    @Nationalized
    @Lob
    @Column(name = "ICERIK")
    private String icerik;

    @Column(name = "YAYINLANMA_TARIHI")
    private LocalDateTime yayinlanmaTarihi;

    @Column(name = "DUYURU_TUR")
    @Enumerated(EnumType.STRING)
    private EnumSyDuyuruTur duyuruTur;

    @Column(name = "ONAYLANMA_TARIHI")
    private LocalDateTime onaylanmaTarihi;

    @ColumnDefault("0")
    @Column(name = "ONAYLANDI", nullable = false)
    private boolean onaylandi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ONAYLAYAN_SYKULLANICI_ID")
    private SyKullanici onaylayanSyKullanici;

    @OneToMany(mappedBy = "syDuyuru")
    private List<SyDuyuruKullanici> syDuyuruKullaniciList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SyDuyuru other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}