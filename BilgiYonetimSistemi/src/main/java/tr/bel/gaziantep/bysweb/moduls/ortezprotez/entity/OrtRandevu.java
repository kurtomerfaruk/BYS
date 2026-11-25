package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtRandevuDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtRandevuTuru;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 25.11.2026 08:30
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ORTRANDEVU")
@NamedQuery(name = "OrtRandevu.findByTalep", query = "SELECT r FROM OrtRandevu r WHERE r.aktif=true AND r.ortTalep = :ortTalep ORDER BY r.randevuTarihi DESC")
@NamedQuery(name = "OrtRandevu.findByStartDateByEndDate", query = "SELECT r FROM OrtRandevu r WHERE r.aktif=true AND (r.randevuTarihi>=:start AND r.randevuTarihi<=:end) ORDER BY r.randevuTarihi ASC")
public class OrtRandevu extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -5396589309833811824L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTTALEP_ID")
    private OrtTalep ortTalep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTKISI_ID")
    private OrtKisi ortKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTPERSONEL_ID")
    private OrtPersonel ortPersonel;

    @Enumerated(EnumType.STRING)
    @Column(name = "RANDEVU_TURU")
    private EnumOrtRandevuTuru randevuTuru;

    @Column(name = "RANDEVU_TARIHI")
    private LocalDateTime randevuTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name = "DURUM")
    private EnumOrtRandevuDurumu durum;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Nationalized
    @Lob
    @Column(name = "SONUC")
    private String sonuc;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtRandevu other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}