package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkModul;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 02.10.2026 09:08
 */
@Getter
@Setter
@Entity
@Table(name = "PKSORU_TUR")
public class PkSoruTur extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 6056235959919377991L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "MODUL")
    private EnumPkModul modul;

    @Size(max = 150)
    @NotNull
    @Nationalized
    @Column(name = "TANIM", nullable = false, length = 150)
    private String tanim;

    @Column(name = "SIRA_NO")
    private Integer siraNo;

    @ColumnDefault("0")
    @Column(name = "COKLU_SECIM", nullable = false)
    private boolean cokluSecim;

    @OneToMany(mappedBy = "pkSoruTur")
    private List<PkSoruTanim> pkSoruTanimList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkSoruTur other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}