package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity;

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
 * @since 7.07.2025 08:54
 */
@Getter
@Setter
@Entity
@Table(name = "EDBVERILECEK_HIZMET_RESIM")
public class EdbVerilecekHizmetResim extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -26677418601771252L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EDBVERILECEK_HIZMET_ID")
    private EdbVerilecekHizmet edbVerilecekHizmet;

    @Size(max = 500)
    @Nationalized
    @Column(name = "RESIM_ADI", length = 500)
    private String resimAdi;

    @Size(max = 500)
    @Nationalized
    @Column(name = "RESIM_YOLU", length = 500)
    private String resimYolu;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EdbVerilecekHizmetResim other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}