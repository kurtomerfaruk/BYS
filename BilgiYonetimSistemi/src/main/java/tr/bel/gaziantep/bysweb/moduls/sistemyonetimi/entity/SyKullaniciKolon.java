package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity;

import jakarta.persistence.*;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntityNoVersion;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreEslesmeModu;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 13:50
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SYKULLANICI_KOLON")
@NamedQuery(name = "SyKullaniciKolon.findByKullaniciByTablo", query = "SELECT s FROM SyKullaniciKolon s WHERE s.syKullanici =:syKullanici AND " +
        "s.syKolon.syTablo.tabloAdi=:tablo ORDER BY s.siraNo ASC ")
public class SyKullaniciKolon extends BaseEntityNoVersion {
    @Serial
    private static final long serialVersionUID = -4000330557138543494L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYKULLANICI_ID")
    private SyKullanici syKullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYKOLON_ID")
    private SyKolon syKolon;

    @Column(name = "GENISLIK")
    private Integer genislik;

    @Column(name = "GORUNURLUK")
    private boolean gorunurluk;

    @Column(name = "SIRA_NO")
    private Integer siraNo;

    @Column(name = "FILTRE_ESLESME_MODU")
    @Enumerated(EnumType.STRING)
    private EnumSyFiltreEslesmeModu filtreEslesmeModu;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SyKullaniciKolon other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}