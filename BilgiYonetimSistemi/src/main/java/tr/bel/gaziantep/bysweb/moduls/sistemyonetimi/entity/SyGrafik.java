package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGrafikTuru;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.8.0
 * @since 27.04.2026 09:34
 */
@Getter
@Setter
@Entity
@Table(name = "SYGRAFIK")
@NamedQuery(name = "SyGrafik.findByModul", query = "SELECT g FROM SyGrafik g WHERE g.aktif=true AND g.gosterilecekModul=:modul")

@SqlResultSetMapping(name = "GrafikData", classes = {
        @ConstructorResult(targetClass = com.kurtomerfaruk.amchartfaces.model.ChartData.class,
                columns = {
                        @ColumnResult(name = "label"),
                        @ColumnResult(name = "value")
                })
})
@SqlResultSetMapping(name = "GrafikData2", classes = {
        @ConstructorResult(targetClass = com.kurtomerfaruk.amchartfaces.model.ChartDataThreeColumn.class,
                columns = {
                        @ColumnResult(name = "label1"),
                        @ColumnResult(name = "label2"),
                        @ColumnResult(name = "value1"),
                })
})
public class SyGrafik extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6499275062056725374L;

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
    @Column(name = "SORGU")
    private String sorgu;

    @Enumerated(EnumType.STRING)
    @Column(name = "GOSTERILECEK_MODUL")
    private EnumModul gosterilecekModul;

    @Enumerated(EnumType.STRING)
    @Column(name = "GRAFIK_TURU")
    private EnumGrafikTuru grafikTuru;

    @ColumnDefault("0")
    @Column(name = "DISA_AKTAR", nullable = false)
    private boolean disaAktar;

    @Column(name = "YONLENDIRME")
    private Short yonlendirme;

    @ColumnDefault("0")
    @Column(name = "KAYDIRMA_CUBUGU")
        private Boolean kaydirmaCubugu;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SyGrafik other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

}