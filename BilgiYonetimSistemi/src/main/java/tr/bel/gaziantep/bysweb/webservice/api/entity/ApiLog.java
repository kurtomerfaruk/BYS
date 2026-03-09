package tr.bel.gaziantep.bysweb.webservice.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.5.0
 * @since 18.02.2026 10:05
 */
@Getter
@Setter
@Entity
@Table(name = "API_LOG")
public class ApiLog {
    @Serial
    private static final long serialVersionUID = 698259171210384254L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 200)
    @Nationalized
    @Column(name = "APP_KEY", length = 200)
    private String appKey;

    @Size(max = 500)
    @Nationalized
    @Column(name = "ENDPOINT", length = 500)
    private String endpoint;

    @Size(max = 10)
    @Nationalized
    @Column(name = "HTTP_METHOD", length = 10)
    private String httpMethod;

    @Nationalized
    @Lob
    @Column(name = "REQUEST_BODY")
    private String requestBody;

    @Column(name = "RESPONSE_STATUS")
    private Integer responseStatus;

    @Nationalized
    @Lob
    @Column(name = "REQUEST_URI")
    private String requestUri;

    @Size(max = 50)
    @Nationalized
    @Column(name = "CLIENT_IP", length = 50)
    private String clientIp;

    @Column(name = "DURATION")
    private long duration;

    @JoinColumn(name = "EKLEYEN", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ApiKullanici ekleyen;

    @Column(name = "EKLEME_TARIHI")
    private LocalDateTime eklemeTarihi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ApiLog other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}