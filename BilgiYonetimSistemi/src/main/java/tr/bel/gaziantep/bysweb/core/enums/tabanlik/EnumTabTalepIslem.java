package tr.bel.gaziantep.bysweb.core.enums.tabanlik;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:25
 */
@Getter
public enum EnumTabTalepIslem implements BaseEnum {

    RANDEVU_VER("Ölçüm Randevusu Ver", EnumTabTalepDurumu.YENI,
            EnumTabTalepDurumu.RANDEVU_BEKLIYOR, "tabtalep:guncelle", "pi pi-calendar-plus");

    private final String label;
    private final EnumTabTalepDurumu sourceStatus;
    private final EnumTabTalepDurumu targetStatus;
    private final String permission;
    private final String icon;

    EnumTabTalepIslem(String label, EnumTabTalepDurumu sourceStatus, EnumTabTalepDurumu targetStatus,
                      String permission, String icon) {
        this.label = label;
        this.sourceStatus = sourceStatus;
        this.targetStatus = targetStatus;
        this.permission = permission;
        this.icon = icon;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

    public Set<EnumTabTalepDurumu> getSourceStatuses() {
        return EnumSet.of(sourceStatus);
    }

    public static Set<EnumTabTalepIslem> getActions(EnumTabTalepDurumu durum) {
        if (durum == null) {
            return EnumSet.noneOf(EnumTabTalepIslem.class);
        }
        EnumSet<EnumTabTalepIslem> result = EnumSet.noneOf(EnumTabTalepIslem.class);
        for (EnumTabTalepIslem action : values()) {
            if (action.getSourceStatuses().contains(durum)) {
                result.add(action);
            }
        }
        return result;
    }
}
