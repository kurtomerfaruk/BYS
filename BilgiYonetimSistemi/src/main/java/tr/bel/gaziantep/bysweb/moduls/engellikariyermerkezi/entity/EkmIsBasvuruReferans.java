package tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.08.2025 15:24
 */
@Getter
@Setter
@Entity
@Table(name = "EKMIS_BASVURU_REFERANS")
public class EkmIsBasvuruReferans extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EKMIS_BASVURU_ID")
    private EkmIsBasvuru ekmIsBasvuru;

    @Size(max = 50)
    @Nationalized
    @Column(name = "AD", length = 50)
    private String ad;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SOYAD", length = 50)
    private String soyad;

    @Size(max = 200)
    @Nationalized
    @Column(name = "CALISTIGI_KURUM", length = 200)
    private String calistigiKurum;

    @Size(max = 50)
    @Nationalized
    @Column(name = "GOREVI", length = 50)
    private String gorevi;

    @Size(max = 20)
    @Nationalized
    @Column(name = "TELEFON", length = 20)
    private String telefon;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EkmIsBasvuruReferans other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}