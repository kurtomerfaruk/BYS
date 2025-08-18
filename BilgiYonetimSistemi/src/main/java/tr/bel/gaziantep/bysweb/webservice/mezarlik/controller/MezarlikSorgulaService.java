package tr.bel.gaziantep.bysweb.webservice.mezarlik.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import tr.bel.gaziantep.bysweb.webservice.mezarlik.model.VefatEdenRoot;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.08.2025 09:47
 */
public class MezarlikSorgulaService implements java.io.Serializable {

    public VefatEdenRoot vefatEdenSorgula(String link, String token, String startDate, String endDate) throws JsonProcessingException {
        Unirest.config().verifySsl(false);
        HttpResponse<JsonNode> postResult = Unirest.post(link + "Mezarlik/VefatEdenler")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + token)
                .field("BaslangicTarihi", startDate)
                .field("BitisTarihi", endDate)
                .asJson();

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.coercionConfigFor(LogicalType.POJO).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsEmpty);
        String json = postResult.getBody().toString();
        return objectMapper.readValue(json, VefatEdenRoot.class);
    }
}
