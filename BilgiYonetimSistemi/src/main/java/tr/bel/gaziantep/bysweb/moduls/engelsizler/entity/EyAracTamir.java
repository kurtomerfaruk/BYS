package tr.bel.gaziantep.bysweb.moduls.engelsizler.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyAracBilgisi;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyArizaTuru;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "EYARAC_TAMIR")
public class EyAracTamir extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 3174809549249637970L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EYKISI_ID")
    private EyKisi eyKisi;

    @Column(name = "KAYIT_TARIHI")
    private LocalDateTime kayitTarihi;

    @Column(name = "ARAC_BILGISI")
    @Enumerated(EnumType.STRING)
    private EnumEyAracBilgisi aracBilgisi;

    @Size(max = 50)
    @Column(name = "MARKA", length = 50)
    private String marka;

    @Size(max = 50)
    @Column(name = "MODEL", length = 50)
    private String model;

    @Size(max = 50)
    @Column(name = "ARIZA_TURU", length = 50)
    private String arizaTuru;

    @Column(name = "GARANTI")
    private boolean garanti;

    @Column(name = "UYGUNLUK")
    private boolean uygunluk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TAMIR_ONCESI_TESLIM_ALAN_GNLPERSONEL_ID")
    private GnlPersonel tamirOncesiTeslimAlanGnlPersonel;

    @Size(max = 50)
    @Column(name = "TAMIR_ONCESI_TESLIM_EDEN", length = 50)
    private String tamirOncesiTeslimEden;

    @Size(max = 250)
    @Column(name = "ARAC_PROBLEMI", length = 250)
    private String aracProblemi;

    @Nationalized
    @Column(name = "NOTLAR")
    private String notlar;

    @Column(name = "TESLIM_TARIHI")
    private LocalDateTime teslimTarihi;

    @Size(max = 50)
    @Column(name = "TAMIR_SONRASI_TESLIM_ALAN", length = 50)
    private String tamirSonrasiTeslimAlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TAMIR_SONRASI_TESLIM_EDEN_GNLPERSONEL_ID")
    private GnlPersonel tamirSonrasiTeslimEdenGnlPersonel;

    @Lob
    @Nationalized
    @Column(name = "YAPILAN_ISLEM")
    private String yapilanIslem;

    @Column(name = "DURUM")
    @Enumerated(EnumType.STRING)
    private EnumGnlTalepDurumu durum;

    @Transient
    @Getter(AccessLevel.NONE)
    private List<EnumEyArizaTuru> arizaTurus;

    @Transient
    @Getter(AccessLevel.NONE)
    private String arizaTuruStr;

    public List<EnumEyArizaTuru> getArizaTurus() {
        arizaTuru = StringUtil.removeBracket(arizaTuru);

        if (!StringUtil.isBlank(arizaTuru)) {
            arizaTurus = new ArrayList<>();
            arizaTurus = Arrays.stream(arizaTuru.split(","))
                    .map(EnumEyArizaTuru::fromValue)
                    .collect(Collectors.toList());
        }
        return arizaTurus;
    }

    public String getArizaTuruStr() {
        if (StringUtil.isBlank(arizaTuru)) {
            return ""; // Eğer arizaTuru boşsa hemen döner
        }

        arizaTuru = StringUtil.removeBracket(arizaTuru);
        return Arrays.stream(arizaTuru.split(","))
                .map(EnumEyArizaTuru::fromValue)
                .map(EnumEyArizaTuru::getDisplayValue)
                .collect(Collectors.joining(","));
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EyAracTamir other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}