package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity;

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
@Table(name = "SYROL_YETKI")
public class SyRolYetki extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 3815801333374833829L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYROL_ID")
    private SyRol syRol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYYETKI_ID")
    private SyYetki syYetki;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SyRolYetki other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}