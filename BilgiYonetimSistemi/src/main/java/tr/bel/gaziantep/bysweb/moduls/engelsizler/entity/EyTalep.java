package tr.bel.gaziantep.bysweb.moduls.engelsizler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepTipi;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepTuru;

import java.io.Serial;
import java.time.LocalDateTime;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 28.06.2025 08:32
 */
@Getter
@Setter
@Entity
@Table(name = "EYTALEP")
public class EyTalep extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 4136828300694352004L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYKISI_ID")
    private EyKisi eyKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYTALEP_KONU_ID")
    private EyTalepKonu eyTalepKonu;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @Column(name = "TALEP_TURU")
    @Enumerated(EnumType.STRING)
    private EnumGnlTalepTuru talepTuru;

    @Column(name = "TALEP_TIPI")
    @Enumerated(EnumType.STRING)
    private EnumGnlTalepTipi talepTipi;

    @Column(name = "DURUM")
    @Enumerated(EnumType.STRING)
    private EnumGnlTalepDurumu durum;


    @Lob
    @Nationalized
    @Column(name = "DURUM_ACIKLAMA")
    private String durumAciklama;


    @Lob
    @Nationalized
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EyTalep other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}