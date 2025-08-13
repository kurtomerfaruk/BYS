package tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity;

import jakarta.persistence.*;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EDBVERILECEK_HIZMET_PERSONEL")
public class EdbVerilecekHizmetPersonel extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -3377889380914621331L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EDBPERSONEL_ID")
    private EdbPersonel edbPersonel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EDBVERILECEK_HIZMET_ID")
    private EdbVerilecekHizmet edbVerilecekHizmet;

    @Column(name = "SECILI")
    private boolean secili;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EdbVerilecekHizmetPersonel other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}