package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBelgeTuru;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 25.11.2026 14:10
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ORTTALEP_BELGE")
public class OrtTalepBelge extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -8019822502314461801L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTTALEP_ID")
    private OrtTalep ortTalep;

    @Enumerated(EnumType.STRING)
    @Column(name = "BELGE_TURU")
    private EnumOrtBelgeTuru belgeTuru;

    @Size(max = 250)
    @Nationalized
    @Column(name = "DOSYA_ADI", length = 250)
    private String dosyaAdi;

    @Size(max = 500)
    @Nationalized
    @Column(name = "DOSYA_YOLU", length = 500)
    private String dosyaYolu;

    @Size(max = 100)
    @Nationalized
    @Column(name = "MIME_TYPE", length = 100)
    private String mimeType;

    @Column(name = "DOSYA_BOYUTU")
    private Long dosyaBoyutu;

    @Column(name = "YUKLENME_TARIHI")
    private LocalDateTime yuklenmeTarihi;

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
        if (!(object instanceof OrtTalepBelge other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}