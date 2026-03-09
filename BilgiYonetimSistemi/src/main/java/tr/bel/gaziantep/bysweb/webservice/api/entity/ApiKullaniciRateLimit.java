package tr.bel.gaziantep.bysweb.webservice.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.5.0
 * @since 18.02.2026 09:52
 */
@Getter
@Setter
@Entity
@Table(name = "API_KULLANICI_RATE_LIMIT")
@NamedQuery(name = "ApiKullaniciRateLimit.findByUserIdByMethodByPath",query = "SELECT r FROM ApiKullaniciRateLimit r WHERE r.aktif=true AND r.apiKullanici.id=:apiUserId " +
        "AND r.apiEndpoint.httpMethod=:method AND r.apiEndpoint.path=:path")
public class ApiKullaniciRateLimit extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6503151431440967257L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "API_KULLANICI_ID")
    private ApiKullanici apiKullanici;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "API_ENDPOINT_ID")
    private ApiEndpoint apiEndpoint;

    @Column(name = "RATE_LIMIT_PER_MINUTE")
    private Integer rateLimitPerMinute;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ApiKullaniciRateLimit other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}