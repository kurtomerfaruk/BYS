package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlAnketDurum;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlAnketTuru;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "GNLANKET")
@NamedQuery(name = "GnlAnket.findByToken", query = "SELECT a FROM GnlAnket a WHERE a.aktif=true AND a.token =:token")
public class GnlAnket extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2108404626152757578L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "BASLIK", nullable = false, length = 50)
    private String baslik;

    @Nationalized
    @Lob
    @Column(name = "ACIKLAMA")
    private String aciklama;

    @Column(name = "DURUM")
    @Enumerated(EnumType.STRING)
    private EnumGnlAnketDurum durum;

    @Column(name = "ANKET_TURU")
    @Enumerated(EnumType.STRING)
    private EnumGnlAnketTuru anketTuru;

    @Column(name = "BASLAMA_TARIHI")
    private LocalDateTime baslamaTarihi;

    @Column(name = "BITIS_TARIHI")
    private LocalDateTime bitisTarihi;

    @Size(max = 150)
    @Nationalized
    @Column(name = "TOKEN", length = 150, unique = true)
    private String token;

    @OneToMany(mappedBy = "gnlAnket", cascade = CascadeType.ALL)
    private List<GnlAnketSoru> gnlAnketSoruList = new ArrayList<>();

    @OneToMany(mappedBy = "gnlAnket")
    private List<GnlAnketCevap> gnlAnketCevapList = new ArrayList<>();

    @Transient
    @Getter(AccessLevel.NONE)
    private int katilimSayisi;

    @Transient
    @Getter(AccessLevel.NONE)
    private String link;

    public int getKatilimSayisi() {
        return gnlAnketCevapList.size();
    }

    public String getLink() {
        if (StringUtil.isBlank(token)) {
            return "";
        }
        int serverPort = Util.getRequest().getServerPort();
        String scheme = Util.getRequest().getScheme();
        String port = "";

        if (!("http".equals(scheme) && serverPort == 80) &&
                !("https".equals(scheme) && serverPort == 443)) {
            port = ":" + serverPort;
        }
        return Util.getRequest().getScheme() + "://"
                + Util.getRequest().getServerName()
                + port
                + Util.getRequest().getContextPath()
                + "/anket?token="
                + token;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GnlAnket other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}