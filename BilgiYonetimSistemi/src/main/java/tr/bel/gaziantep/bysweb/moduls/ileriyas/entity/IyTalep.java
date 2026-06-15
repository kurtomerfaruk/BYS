package tr.bel.gaziantep.bysweb.moduls.ileriyas.entity;

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

@Getter
@Setter
@Entity
@Table(name = "IYTALEP")
@NamedQuery(name = "IyTalep.findByDurum", query = "SELECT e FROM IyTalep e WHERE e.aktif=true AND e.durum =:durum AND " +
        "(e.iyKisi.gnlKisi.durum IN (" +
        "                               tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.OLU," +
        "                               tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.OLUM," +
        "                               tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum.OLUMUN_TESPITI) )")
public class IyTalep extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -1676101956747620367L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IYKISI_ID")
    private IyKisi iyKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IYTALEP_KONU_ID")
    private IyTalepKonu iyTalepKonu;

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

    @Nationalized
    @Lob
    @Column(name = "DURUM_ACIKLAMA")
    private String durumAciklama;

    @Nationalized
    @Lob
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
        if (!(object instanceof IyTalep other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}