package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlFaydalandigiHak;

import java.io.Serial;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GNLKISI_FAYDALANDIGI_HAKLAR")
public class GnlKisiFaydalandigiHaklar extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2610732419726978642L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "GNLKISI_ID", nullable = false)
    private GnlKisi gnlKisi;

    @Column(name = "TANIM")
    @Enumerated(EnumType.STRING)
    private EnumGnlFaydalandigiHak tanim;


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
        if (!(object instanceof GnlKisiFaydalandigiHaklar other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}