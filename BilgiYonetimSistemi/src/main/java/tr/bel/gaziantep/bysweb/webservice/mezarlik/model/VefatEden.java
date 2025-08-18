package tr.bel.gaziantep.bysweb.webservice.mezarlik.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.08.2025 09:49
 */
@Data
public class VefatEden implements Serializable {
    @Serial
    private static final long serialVersionUID = -6400428412286816627L;

    @JsonProperty("TcKimlikNo")
    private String tcKimlikNo;
    @JsonProperty("AdSoyad")
    private String adSoyad;
    @JsonProperty("DogumTarihi")
    private LocalDateTime dogumTarihi;
    @JsonProperty("DogumTarihiStr")
    private String dogumTarihiStr;
    @JsonProperty("Yas")
    private String yas;
    @JsonProperty("OlumTarihiStr")
    private String olumTarihiStr;
    @JsonProperty("OlumTarihi")
    private LocalDateTime olumTarihi;
    @JsonProperty("DefinTarihi")
    private LocalDateTime definTarihi;
    @JsonProperty("Cinsiyet")
    private String cinsiyet;
}
