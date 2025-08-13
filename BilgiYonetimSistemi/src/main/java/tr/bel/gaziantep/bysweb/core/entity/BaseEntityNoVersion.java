package tr.bel.gaziantep.bysweb.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 08:24
 */
@Getter
@Setter
@MappedSuperclass
@SuperBuilder
public abstract class BaseEntityNoVersion implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -150317753224568544L;

    @JoinColumn(name = "EKLEYEN", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private SyKullanici ekleyen;

    @Column(name = "EKLEME_TARIHI")
    private LocalDateTime eklemeTarihi;

    @JoinColumn(name = "GUNCELLEYEN", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private SyKullanici guncelleyen;

    @Column(name = "GUNCELLEME_TARIHI")
    private LocalDateTime guncellemeTarihi;

    @Column(name = "AKTIF")
    private boolean aktif;


    public BaseEntityNoVersion() {
    }

    @PrePersist
    public void prePersist() {
        if (ekleyen == null) {
            ekleyen = Util.getSyKullanici();
        }

        eklemeTarihi = LocalDateTime.now();
        aktif = true;
    }

    @PreUpdate
    public void preUpdate() {
        if (ekleyen == null) {
            ekleyen = Util.getSyKullanici();
        }
        if (guncelleyen == null) {
            guncelleyen = Util.getSyKullanici();
        }

        guncellemeTarihi = LocalDateTime.now();
    }
}