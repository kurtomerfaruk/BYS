package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.*;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlIzinTuru;

import java.io.Serial;
import java.time.LocalDate;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 30.09.2026 11:56
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PKIZIN")
public class PkIzin extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -943991615424031623L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PKPERSONEL_ID")
    private PkPersonel pkPersonel;

    @Enumerated(EnumType.STRING)
    @Column(name = "IZIN_TURU")
    private EnumGnlIzinTuru izinTuru;

    @Column(name = "IZIN_BASLANGIC")
    private LocalDate izinBaslangic;

    @Column(name = "IZIN_BITIS")
    private LocalDate izinBitis;

    @Column(name = "GUN_SAYI")
    private Integer gunSayi;


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkIzin other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}