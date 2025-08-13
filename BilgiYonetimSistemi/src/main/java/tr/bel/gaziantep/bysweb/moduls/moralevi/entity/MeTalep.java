package tr.bel.gaziantep.bysweb.moduls.moralevi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepTipi;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepTuru;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalepKonu;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "METALEP")
public class MeTalep extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2627797844270977854L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEKISI_ID")
    private MeKisi meKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYTALEP_KONU_ID")
    private EyTalepKonu eyTalepKonu;

    @NotNull
    @Column(name = "TARIH", nullable = false)
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
        if (!(object instanceof MeTalep other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}