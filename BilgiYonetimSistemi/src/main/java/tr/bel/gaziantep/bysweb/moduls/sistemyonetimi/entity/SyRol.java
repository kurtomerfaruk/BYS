package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 13:50
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SYROL")
public class SyRol extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -6060469154653227847L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)

    @Column(name = "ROL_ADI", length = 50)
    private String rolAdi;

    @Size(max = 250)

    @Column(name = "ACIKLAMA", length = 250)
    private String aciklama;

    @OneToMany(mappedBy = "syRol", cascade = CascadeType.MERGE)
    @Builder.Default
    private List<SyRolYetki> syRolYetkis = new ArrayList<>();

    @OneToMany(mappedBy = "syRol", cascade = CascadeType.MERGE)
    @Builder.Default
    private List<SyKullaniciRol> syKullaniciRolList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SyRol other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}