package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "SHKISI_KAN_TAHLIL_SONUC")
@NamedQuery(name = "ShKisiKanTahlilSonuc.findByGnlKisi", query = "SELECT s FROM ShKisiKanTahlilSonuc s " +
        "WHERE s.aktif=true AND s.shKisi.gnlKisi=:gnlKisi ORDER BY s.id DESC")
public class ShKisiKanTahlilSonuc extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -5899903853413872950L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHKISI_ID")
    private ShKisi shKisi;

    @Column(name = "TARIH")
    private LocalDateTime tarih;

    @Column(name = "GLIKOZ_ACLIK")
    private Integer glikozAclik;

    @Column(name = "INSULIN", precision = 18, scale = 2)
    private BigDecimal insulin;

    @Column(name = "TOTAL_KOLESTEROL")
    private Integer totalKolesterol;

    @Column(name = "LDL")
    private Integer ldl;

    @Column(name = "TRIGLISERID")
    private Integer trigliserid;

    @Column(name = "DEMIR")
    private Integer demir;

    @Column(name = "B12_VITAMINI")
    private Integer b12Vitamini;

    @Column(name = "TSH", precision = 18, scale = 2)
    private BigDecimal tsh;

    @Column(name = "T3", precision = 18, scale = 2)
    private BigDecimal t3;

    @Column(name = "T4", precision = 18, scale = 2)
    private BigDecimal t4;

    @Column(name = "D_VITAMINI", precision = 18, scale = 2)
    private BigDecimal dVitamini;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ShKisiKanTahlilSonuc other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}