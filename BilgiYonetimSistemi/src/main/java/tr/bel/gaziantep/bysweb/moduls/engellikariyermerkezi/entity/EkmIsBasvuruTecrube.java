package tr.bel.gaziantep.bysweb.moduls.engellikariyermerkezi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "EKMIS_BASVURU_TECRUBE")
public class EkmIsBasvuruTecrube extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EKMIS_BASVURU_ID")
    private EkmIsBasvuru ekmIsBasvuru;

    @Size(max = 200)
    @Nationalized
    @Column(name = "FIRMA", length = 200)
    private String firma;

    @Size(max = 200)
    @Nationalized
    @Column(name = "GOREV", length = 200)
    private String gorev;

    @Column(name = "GIRIS_TARIHI")
    private LocalDate girisTarihi;

    @Column(name = "CIKIS_TARIHI")
    private LocalDate cikisTarihi;

    @Nationalized
    @Lob
    @Column(name = "AYRILMA_SEBEBI")
    private String ayrilmaSebebi;

    @Column(name = "UCRET", precision = 18, scale = 2)
    private BigDecimal ucret;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EkmIsBasvuruTecrube other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}