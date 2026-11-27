package tr.bel.gaziantep.bysweb.moduls.tabanlik.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabBelgeTuru;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:25
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TABBELGE")
public class TabBelge extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -1271713346415241985L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TABTALEP_ID")
    private TabTalep tabTalep;

    @Enumerated(EnumType.STRING)
    @Column(name = "BELGE_TURU")
    private EnumTabBelgeTuru belgeTuru;

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
    @Column(name = "MIME_TURU", length = 100)
    private String mimeTuru;

    @Column(name = "DOSYA_BOYUTU")
    private Long dosyaBoyutu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "YUKLEYEN_SYKULLANICI_ID")
    private SyKullanici yukleyen;

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
        if (!(object instanceof TabBelge other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}