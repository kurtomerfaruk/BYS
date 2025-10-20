package tr.bel.gaziantep.bysweb.moduls.genel.entity;

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
 * @since 31.07.2025 10:08
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GNLPERSONEL")
public class GnlPersonel extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -5698037286482605462L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GNLKISI_ID")
    private GnlKisi gnlKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLUNVAN_ID")
    private GnlUnvan gnlUnvan;

    @Size(max = 5)
    @Column(name = "DAHILI", length = 5)
    private String dahili;

    @OneToMany(mappedBy = "gnlPersonel", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @Builder.Default
    private List<GnlPersonelBirim> gnlPersonelBirimList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlPersonel other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}