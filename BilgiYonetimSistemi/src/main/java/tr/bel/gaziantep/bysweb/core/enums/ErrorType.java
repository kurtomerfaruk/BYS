package tr.bel.gaziantep.bysweb.core.enums;

import tr.bel.gaziantep.bysweb.core.exception.BaseErrorType;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 1.10.2025 09:06
 */
public enum ErrorType implements BaseErrorType {

    NESNE_SECILEMEDI("Nesne Seçilmedi"),
    KISI_BILGILERI_OKUNAMADI("Kişi Bilgileri Okunamadı...");

    private String messageText;

    @Override
    public String getDisplayValue() {
        return this.messageText;
    }

    ErrorType(String messageText) {
        this.messageText = messageText;
    }
}
