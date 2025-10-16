package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity;

import jakarta.persistence.*;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.10.2025 11:22
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SYKULLANICI_YETKI")
public class SyKullaniciYetki extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 5232727565061314147L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYKULLANICI_ID")
    private SyKullanici syKullanici;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYYETKI_ID")
    private SyYetki syYetki;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SyKullaniciYetki other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}