package tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "EDBHIZMET_ANKET")
public class EdbHizmetAnket extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 2660834413022880258L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EDBVERILECEK_HIZMET_ID")
    private EdbVerilecekHizmet edbVerilecekHizmet;

    @Column(name = "TARIH")
    private LocalDate tarih;

    @Nationalized
    @Lob
    @Column(name = "GENEL_GORUS")
    private String genelGorus;

    @Column(name = "HIZMETTEN_MEMNUN")
    private Integer hizmettenMemnun;

    @Column(name = "PERSONELDEN_MEMNUN")
    private Integer personeldenMemnun;

    @ColumnDefault("0")
    @Column(name = "HIZMET_ZAMANINDA_VERILDI_MI", nullable = false)
    private boolean hizmetZamanindaVerildiMi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GORUSMEYI_YAPAN_EDBPERSONEL_ID")
    private EdbPersonel gorusmeyiYapanEdbPersonel;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EdbHizmetAnket other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}