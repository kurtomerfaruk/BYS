package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "GNLRAPOR_SABLON_KOLON")
public class GnlRaporSablonKolon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_KULLANICI_RAPOR_SABLON_ID")
    private GnlRaporKullaniciRaporSablon gnlRaporKullaniciRaporSablon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GNLRAPOR_KOLON_ID")
    private GnlRaporKolon gnlRaporKolon;

    @ColumnDefault("0")
    @Column(name = "SIRALAMA")
    private Integer siralama;

    @Column(name = "GENISLIK")
    private Integer genislik;

}