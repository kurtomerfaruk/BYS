package tr.bel.gaziantep.bysweb.core.enums.bys;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 13:22
 */
@Getter
public enum EnumRaporTuru implements BaseEnum {

    PDF("PDF"),
    XLS("XLS"),
    PPT("PPT"),
    DOCX("DOCX"),
    CSV("CSV");

    private final String modul;

    EnumRaporTuru(String modul) {
        this.modul = modul;
    }

    @Override
    public String getDisplayValue() {
        return modul;
    }


}

