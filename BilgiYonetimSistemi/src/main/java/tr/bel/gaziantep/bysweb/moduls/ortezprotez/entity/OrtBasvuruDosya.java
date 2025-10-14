package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

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
 * @since 14.10.2025 08:55
 */
@Getter
@Setter
@Entity
@Table(name = "ORTBASVURU_DOSYA")
public class OrtBasvuruDosya extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 9026363742192938253L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORTBASVURU_ID")
    private OrtBasvuru ortBasvuru;

    @Size(max = 50)
    @Nationalized
    @Column(name = "DOSYA_ADI", length = 50)
    private String dosyaAdi;

    @Size(max = 250)
    @Nationalized
    @Column(name = "DOSYA_YOLU", length = 250)
    private String dosyaYolu;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtBasvuruDosya other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}