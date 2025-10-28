package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.engelsizkariyermerkezi.EnumEkmEngelOlusmaDurumu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.08.2025 15:24
 */
@Getter
@Setter
@Entity
@Table(name = "EKMIS_BASVURU")
@NamedQuery(name = "EkmIsBasvuru.findByGnlKisi", query = "SELECT a FROM EkmIsBasvuru a WHERE a.aktif=true AND " +
        "a.eyKisi.gnlKisi=:gnlKisi ORDER BY a.basvuruTarihi DESC")
@NamedQuery(name = "EkmIsBasvuru.getEyKisiList", query = "SELECT e.eyKisi FROM EkmIsBasvuru e WHERE e.aktif=true " +
        "ORDER BY e.eyKisi.gnlKisi.ad,e.eyKisi.gnlKisi.soyad")
public class EkmIsBasvuru extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 3647869523098209089L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "BASVURU_TARIHI")
    private LocalDateTime basvuruTarihi;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "EYKISI_ID")
    private EyKisi eyKisi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EKMIS_POZISYON_ID")
    private EkmIsPozisyon ekmIsPozisyon;

    @Column(name = "ENGEL_OLUSMA_DURUMU")
    @Enumerated(EnumType.STRING)
    private EnumEkmEngelOlusmaDurumu engelOlusmaDurumu;

    @ColumnDefault("0")
    @Column(name = "SIGARA_KULLANIYOR")
    private boolean sigaraKullaniyor;

    @ColumnDefault("0")
    @Column(name = "SAGLIK_PROBLEMI_VAR_MI")
    private boolean saglikProblemiVarMi;

    @Nationalized
    @Lob
    @Column(name = "SAGLIK_ACIKLAMA")
    private String saglikAciklama;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KISI_YAKINI_ADI", length = 50)
    private String kisiYakiniAdi;

    @Size(max = 50)
    @Nationalized
    @Column(name = "KISI_YAKINI_SOYADI", length = 50)
    private String kisiYakiniSoyadi;

    @Size(max = 20)
    @Nationalized
    @Column(name = "KISI_YAKINI_TELEFON", length = 20)
    private String kisiYakiniTelefon;

    @Nationalized
    @Lob
    @Column(name = "UZMAN_GORUSU")
    private String uzmanGorusu;

    @OneToMany(mappedBy = "ekmIsBasvuru", fetch = FetchType.LAZY)
    private List<EkmIsBasvuruEgitim> ekmIsBasvuruEgitimList = new ArrayList<>();

    @OneToMany(mappedBy = "ekmIsBasvuru", fetch = FetchType.LAZY)
    private List<EkmIsBasvuruKullandigiProgram> ekmIsBasvuruKullandigiProgramList = new ArrayList<>();

    @OneToMany(mappedBy = "ekmIsBasvuru", fetch = FetchType.LAZY)
    private List<EkmIsBasvuruKursSeminer> ekmIsBasvuruKursSeminerList = new ArrayList<>();

    @OneToMany(mappedBy = "ekmIsBasvuru", fetch = FetchType.LAZY)
    private List<EkmIsBasvuruReferans> ekmIsBasvuruReferansList = new ArrayList<>();

    @OneToMany(mappedBy = "ekmIsBasvuru", fetch = FetchType.LAZY)
    private List<EkmIsBasvuruTecrube> ekmIsBasvuruTecrubeList = new ArrayList<>();

    @OneToMany(mappedBy = "ekmIsBasvuru", fetch = FetchType.LAZY)
    private List<EkmIsBasvuruYabanciDil> ekmIsBasvuruYabanciDilList = new ArrayList<>();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EkmIsBasvuru other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}