package tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 14:01
 */
@Getter
@Setter
@Entity
@Table(name = "AYETKINLIK_RESIM")
public class AyEtkinlikResim extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 7185974636252448294L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AYETKINLIK_ID")
    private AyEtkinlik ayEtkinlik;

    @Size(max = 150)
    @Nationalized
    @Column(name = "RESIM_ADI", length = 150)
    private String resimAdi;

    @Size(max = 150)
    @Nationalized
    @Column(name = "RESIM_YOLU", length = 150)
    private String resimYolu;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AyEtkinlikResim other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}