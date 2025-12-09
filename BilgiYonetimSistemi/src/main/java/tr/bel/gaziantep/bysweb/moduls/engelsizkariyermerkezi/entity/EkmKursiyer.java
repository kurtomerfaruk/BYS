package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDevamDurumu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 8.12.2025 08:33
 */
@Getter
@Setter
@Entity
@Table(name = "EKMKURSIYER")
public class EkmKursiyer extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -8078800836997715889L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "KAYIT_TARIHI")
    private LocalDateTime kayitTarihi;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "EYKISI_ID")
    private EyKisi eyKisi;

    @Column(name = "DEVAM_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumGnlDevamDurumu devamDurumu;

    @OneToMany(mappedBy = "ekmKursiyer",fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<EkmGirisCikis> ekmGirisCikisList = new ArrayList<>();

    @OneToMany(mappedBy = "ekmKursiyer",fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<EkmKursiyerKurs> ekmKursiyerKursList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EkmKursiyer other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}