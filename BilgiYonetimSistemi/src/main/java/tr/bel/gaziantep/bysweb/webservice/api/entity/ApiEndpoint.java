package tr.bel.gaziantep.bysweb.webservice.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
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
@Table(name = "API_ENDPOINT")
public class ApiEndpoint extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 838244058590364086L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 500)
    @Nationalized
    @Column(name = "PATH", length = 500)
    private String path;

    @Size(max = 10)
    @Nationalized
    @Column(name = "HTTP_METHOD", length = 10)
    private String httpMethod;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ApiEndpoint other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}