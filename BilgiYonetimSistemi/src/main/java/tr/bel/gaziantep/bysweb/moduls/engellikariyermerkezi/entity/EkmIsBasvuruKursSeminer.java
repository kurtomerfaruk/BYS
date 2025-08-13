package tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "EKMIS_BASVURU_KURS_SEMINER")
public class EkmIsBasvuruKursSeminer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EKMIS_BASVURU_ID")
    private EkmIsBasvuru ekmIsBasvuru;

    @Size(max = 200)
    @Nationalized
    @Column(name = "KONU", length = 200)
    private String konu;

    @Size(max = 200)
    @Nationalized
    @Column(name = "KURUM", length = 200)
    private String kurum;

    @Size(max = 200)
    @Nationalized
    @Column(name = "SURE", length = 200)
    private String sure;

    @Column(name = "TARIH")
    private LocalDate tarih;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EkmIsBasvuruKursSeminer other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}