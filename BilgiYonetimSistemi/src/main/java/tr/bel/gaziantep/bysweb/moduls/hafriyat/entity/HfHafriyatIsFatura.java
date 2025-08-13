package tr.bel.gaziantep.bysweb.moduls.hafriyat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "HFHAFRIYAT_IS_FATURA")
public class HfHafriyatIsFatura extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 4297626722003836641L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HFHAFRIYAT_IS_ID")
    private HfHafriyatIs hfHafriyatIs;

    @Size(max = 50)
    @Nationalized
    @Column(name = "FATURA_NO", length = 50)
    private String faturaNo;

    @Column(name = "FATURA_TARIHI")
    private LocalDate faturaTarihi;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HfHafriyatIsFatura other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}