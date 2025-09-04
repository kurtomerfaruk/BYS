package tr.bel.gaziantep.bysweb.webservice.gazikart.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 13:50
 */
@Getter
@Setter
public class ServisModel implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 3826554024679815289L;
    @JsonProperty("IDENTITY_NO")
    public String identityNo;
    @JsonProperty("NAME")
    public String name;
    @JsonProperty("SURNAME")
    public String surname;
    @JsonProperty("FATHER_NAME")
    public String fatherName;
    @JsonProperty("BIRTH_DATE")
    public String birthDate;
    @JsonProperty("BIRTH_PLACE")
    public String birthPlace;
    @JsonProperty("ADDRESS")
    public String address;
    @JsonProperty("TEL_MOBILE")
    public String telMobile;
    @JsonProperty("E_MAIL")
    public Object email;
    @JsonProperty("CORPORATION")
    public String corporation;
    @JsonProperty("DISABLED_DEGREE")
    public String disabledDegree;
    @JsonProperty("RELATIONSHIP")
    public Object relationship;
    @JsonProperty("ALIAS_NO")
    public String aliasNo;
    @JsonProperty("OLD_ALIAS")
    public Object oldAlias;
    @JsonProperty("DESCRIPTION")
    public String description;
    @JsonProperty("DATETIME")
    public String datetime;
    @JsonProperty("USERNAME")
    public String username;
    @JsonProperty("CARD_TYPE")
    public String cardType;
    @JsonProperty("CREATED_DATETIME")
    public String createdDatetime;
    @JsonProperty("CURRENT_VISA_DATE")
    public String currentVisaDate;
}
