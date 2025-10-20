package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.09.2025 09:46
 */
@Getter
@Setter
@Entity
@Table(name = "GNLANKET_CEVAP_DETAY")
public class GnlAnketCevapDetay extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 4707587151132398069L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "GNLANKET_CEVAP_ID")
    private GnlAnketCevap gnlAnketCevap;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "GNLANKET_SORU_ID")
    private GnlAnketSoru gnlAnketSoru;

    @Nationalized
    @Lob
    @Column(name = "CEVAP")
    private String cevap;

    @Nationalized
    @Lob
    @Column(name = "DIGER_CEVAP")
    private String digerCevap;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlAnketCevapDetay other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}