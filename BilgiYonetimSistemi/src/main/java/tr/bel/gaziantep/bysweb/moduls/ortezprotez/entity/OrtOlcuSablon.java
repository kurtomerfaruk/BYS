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
 * @since 26.09.2025 09:15
 */

@Getter
@Setter
@Entity
@Table(name = "ORTOLCU_SABLON")
public class OrtOlcuSablon extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 334537588609250650L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "TANIM", length = 50)
    private String tanim;

    @Size(max = 150)
    @Nationalized
    @Column(name = "RESIM_YOLU", length = 150)
    private String resimYolu;

    @Column(name = "RESIM_GENISLIK")
    private Integer resimGenislik;

    @Column(name = "RESIM_YUKSEKLIK")
    private Integer resimYukseklik;


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtOlcuSablon other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}