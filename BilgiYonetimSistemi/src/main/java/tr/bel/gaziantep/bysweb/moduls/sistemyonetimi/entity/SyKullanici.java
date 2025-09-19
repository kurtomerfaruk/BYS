package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntityNoVersion;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyKullaniciTuru;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SYKULLANICI")
@NamedQuery(name = "SyKullanici.findByKullaniciAdiByParola", query = "SELECT s FROM SyKullanici s WHERE s.kullaniciAdi=:kullaniciAdi AND s.parola=:parola")
@NamedQuery(name = "SyKullanici.findByKullaniciAdi", query = "SELECT s FROM SyKullanici s WHERE s.kullaniciAdi=:kullaniciAdi")
@NamedQuery(name = "SyKullanici.findByKilitli", query = "SELECT s FROM SyKullanici s WHERE s.aktif=true AND s.kilitli=false")
public class SyKullanici extends BaseEntityNoVersion {
    @Serial
    private static final long serialVersionUID = -9216898786963405162L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @Basic(optional = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GNLPERSONEL_ID")
    private GnlPersonel gnlPersonel;

    @Size(max = 50)
    @Column(name = "KULLANICI_ADI", length = 50)
    private String kullaniciAdi;

    @Size(max = 100)
    @Column(name = "PAROLA", length = 100)
    private String parola;

    @Column(name = "KULLANICI_TURU")
    @Enumerated(EnumType.STRING)
    private EnumSyKullaniciTuru kullaniciTuru;

    @Size(max = 50)
    @Column(name = "SESSION_ID", length = 50)
    private String sessionId;

    @Size(max = 50)

    @Column(name = "SON_GIRILEN_IP", length = 50)
    private String sonGirilenIp;

    @Column(name = "SON_GIRIS_ZAMANI")
    private LocalDateTime sonGirisZamani;

    @Size(max = 50)
    @Column(name = "TEMA", length = 50)
    private String tema;

    @ColumnDefault("0")
    @Column(name = "PAROLA_DEGISTIRILSIN")
    private boolean parolaDegistirilsin;

    @Column(name = "KILITLI")
    private boolean kilitli;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "syKullanici", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SyKullaniciGiris> syKullaniciGirisList = new ArrayList<>();

    @OneToMany(mappedBy = "syKullanici",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<SyKullaniciRol> syKullaniciRols = new LinkedHashSet<>();

    @OneToMany(mappedBy = "syKullanici")
    @Builder.Default
    private List<SyDuyuruKullanici> syDuyuruKullaniciList = new ArrayList<>();

    public SyKullanici(Integer id){
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SyKullanici other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}