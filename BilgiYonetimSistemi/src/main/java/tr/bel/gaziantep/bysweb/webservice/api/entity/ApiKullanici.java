package tr.bel.gaziantep.bysweb.webservice.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.5.0
 * @since 18.02.2026 09:52
 */
@Getter
@Setter
@Entity
@Table(name = "API_KULLANICI")
@NamedQuery(name = "ApiKullanici.findByAppKeyByAppSecret",query = "SELECT k FROM ApiKullanici k WHERE k.aktif=true AND k.appKey=:appKey " +
        "AND k.appSecret=:appSecret")
@NamedQuery(name = "ApiKullanici.findByApiKullaniciIdByIP",query = "SELECT k FROM ApiKullanici k WHERE k.aktif=true AND k.id=:id " +
        "AND k.ip=:ip")
@NamedQuery(name = "ApiKullanici.findByIp",query = "SELECT k FROM ApiKullanici k WHERE k.aktif=true AND k.ip=:ip")

public class ApiKullanici extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 5047669326431485501L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "GNLKISI_ID", nullable = false)
    private GnlKisi gnlKisi;

    @Size(max = 200)
    @NotNull
    @Nationalized
    @Column(name = "APP_KEY", nullable = false, length = 200)
    private String appKey;

    @Size(max = 500)
    @NotNull
    @Nationalized
    @Column(name = "APP_SECRET", nullable = false, length = 500)
    private String appSecret;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "IP", nullable = false, length = 50)
    private String ip;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ApiKullanici other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}