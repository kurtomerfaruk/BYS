package tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.10.2025 08:29
 */
@Getter
@Setter
@Entity
@Table(name = "ORTPROJE")
public class OrtProje extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2731612657363174341L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "PROJE_ADI", length = 50)
    private String projeAdi;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Column(name = "BASLANGIC_TARIHI")
    private LocalDate baslangicTarihi;

    @Column(name = "BITIS_TARIHI")
    private LocalDate bitisTarihi;

    @OneToMany(mappedBy = "ortProje")
    private Set<OrtBasvuru> ortbasvurus = new LinkedHashSet<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrtProje other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}