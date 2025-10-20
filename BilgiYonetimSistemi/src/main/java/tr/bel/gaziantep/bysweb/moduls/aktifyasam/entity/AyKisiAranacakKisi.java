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
@Table(name = "AYKISI_ARANACAK_KISI")
public class AyKisiAranacakKisi extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 8199582546837691128L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AYKISI_ID")
    private AyKisi ayKisi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "AD", length = 50)
    private String ad;

    @Size(max = 50)
    @Nationalized
    @Column(name = "SOYAD", length = 50)
    private String soyad;

    @Size(max = 50)
    @Nationalized
    @Column(name = "YAKINLIK_DERECESI", length = 50)
    private String yakinlikDerecesi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "TELEFON", length = 50)
    private String telefon;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AyKisiAranacakKisi other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}