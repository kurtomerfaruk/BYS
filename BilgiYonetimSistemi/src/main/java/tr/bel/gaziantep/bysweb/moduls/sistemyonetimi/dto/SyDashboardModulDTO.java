package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.dto;

import lombok.*;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Sistem paneli modül özet kartı için veri taşıyıcısı.
 * Yeni bir modül eklendiğinde manifest listesine yapılan tek satırlık eklemeyle
 * bu DTO üzerinden panele otomatik olarak dahil edilir.
 *
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.25.0
 * @since 27.10.2026 11:18
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyDashboardModulDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -5928964484415056057L;

    private EnumModul modul;
    private String ikon;
    private String renk;
    private long ziyaretSayisi;
    private long kullaniciSayisi;
    private LocalDateTime sonErisim;

    public String getModulAdi() {
        return modul != null ? modul.getDisplayValue() : "";
    }

    public boolean isKullanilmis() {
        return ziyaretSayisi > 0;
    }
}
