package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 10:57
 */
@Getter
public enum EnumGnlYakinlikDerecesi implements BaseEnum {

    ESI("Eşi"),
    KARDESI("Kardeşi"),
    ANNESI_BABASI("Annesi-Babası"),
    COCUGU("Çocuğu"),
    TORUNU("Torunu"),
    GELINI_DAMADI("Gelini-Damadı"),
    KOMSUSU("Komşusu"),
    DIGER("Diğer"),
    KENDISI("Kendisi"),
    OGLU("Oğlu"),
    KIZI("Kızı");

    private final String label;

    EnumGnlYakinlikDerecesi(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }

    public static EnumGnlYakinlikDerecesi fromValue(String value){
        for (EnumGnlYakinlikDerecesi yakinlikDerecesi : EnumGnlYakinlikDerecesi.values()) {
            if(yakinlikDerecesi.getDisplayValue().equals(value)){
                return yakinlikDerecesi;
            }
        }

        return null;
    }
}
