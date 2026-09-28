package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumEvetHayir;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumVarYok;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkAkraba;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 28.09.2026 08:25
 */
@Getter
@Setter
@Entity
@Table(name = "PKHASTA_ADLI_SICIL")
public class PkHastaAdliSicil extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 3773841336488655383L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "SABIKA_KAYDI")
    private EnumVarYok sabikaKaydi;

    @Nationalized
    @Lob
    @Column(name = "SABIKA_KAYDI_ACIKLAMA")
    private String sabikaKaydiAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEVAM_EDEN_MAHKEME")
    private EnumVarYok devamEdenMahkeme;

    @Enumerated(EnumType.STRING)
    @Column(name = "KARAKOLLUK_OLDU_MU")
    private EnumEvetHayir karakollukOlduMu;

    @Nationalized
    @Lob
    @Column(name = "KARAKOLLUK_OLDU_MU_ACIKLAMA")
    private String karakollukOlduMuAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "NEZARETTE_KALDI_MI")
    private EnumEvetHayir nezaretteKaldiMi;

    @Enumerated(EnumType.STRING)
    @Column(name = "AILEDE_MADDE_KULLANIMI")
    private EnumVarYok ailedeMaddeKullanimi;

    @Enumerated(EnumType.STRING)
    @Column(name = "AILEDE_MADDE_KULLANIMI_ACIKLAMA")
    private EnumPkAkraba ailedeMaddeKullanimiAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "AILEDE_INTIHAR_EDEN")
    private EnumVarYok ailedeIntiharEden;

    @Enumerated(EnumType.STRING)
    @Column(name = "AILEDE_INTIHAR_EDEN_ACIKLAMA")
    private EnumPkAkraba ailedeIntiharEdenAciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "SOKAK_YASAMI")
    private EnumVarYok sokakYasami;

    @Nationalized
    @Lob
    @Column(name = "AILENIN_DEMOGRAFIK_BILGILERI")
    private String aileninDemografikBilgileri;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PkHastaAdliSicil other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}