package tr.bel.gaziantep.bysweb.moduls.hafriyat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.hafriyat.EnumHfFirmaTuru;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIl;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "HFFIRMA")
public class HfFirma extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 5588206092570133677L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KOD", length = 50)
    private String kod;

    @Size(max = 250)
    @Nationalized
    @Column(name = "FIRMA_UNVANI", length = 250)
    private String firmaUnvani;

    @Size(max = 250)
    @Nationalized
    @Column(name = "YETKILI", length = 250)
    private String yetkili;

    @Size(max = 50)
    @Nationalized
    @Column(name = "TELEFON", length = 50)
    private String telefon;

    @Size(max = 50)
    @Nationalized
    @Column(name = "FAKS", length = 50)
    private String faks;

    @Nationalized
    @Lob
    @Column(name = "ADRES")
    private String adres;

    @Size(max = 50)
    @Nationalized
    @Column(name = "VERGI_DAIRESI", length = 50)
    private String vergiDairesi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "VERGI_NO", length = 50)
    private String vergiNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLIL_ID")
    private GnlIl gnlIl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLILCE_ID")
    private GnlIlce gnlIlce;

    @Column(name = "FIRMA_TURU")
    @Enumerated(EnumType.STRING)
    private EnumHfFirmaTuru firmaTuru;

    @ColumnDefault("0")
    @Column(name = "EVRAK_AYLIK_MI")
    private boolean evrakAylikMi;

    @Column(name = "EVRAK_GUN")
    private Integer evrakGun;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HfFirma other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}