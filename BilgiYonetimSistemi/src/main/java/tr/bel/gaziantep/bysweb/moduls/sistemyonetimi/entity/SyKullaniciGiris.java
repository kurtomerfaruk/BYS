package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SYKULLANICI_GIRIS")
public class SyKullaniciGiris extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -5363197804338575177L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYKULLANICI_ID")
    private SyKullanici syKullanici;

    @Column(name = "GIRIS_ZAMANI")
    private LocalDateTime girisZamani;

    @Size(max = 20)

    @Column(name = "GIRIS_IP", length = 20)
    private String girisIp;

    @Size(max = 50)

    @Column(name = "SESSION_ID", length = 50)
    private String sessionId;

    @Size(max = 250)

    @Column(name = "ACIKLAMA", length = 250)
    private String aciklama;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SyKullaniciGiris other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}