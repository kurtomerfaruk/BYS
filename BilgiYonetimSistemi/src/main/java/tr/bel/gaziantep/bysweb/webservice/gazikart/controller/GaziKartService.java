package tr.bel.gaziantep.bysweb.webservice.gazikart.controller;

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
import tr.bel.gaziantep.bysweb.webservice.gazikart.model.ServisSonucu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 13:49
 */
public class GaziKartService implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -2710217811364283386L;

    public ServisSonucu getList(String link, String baslangicTarihi, String bitisTarihi) throws JsonProcessingException {
//        try {
//            String format = "yyyyMMdd";
//            URL url = new URL(link+"startdate=" + baslangicTarihi + "&enddate=" + bitisTarihi);
//            URLConnection urlConnection = url.openConnection();
//            InputStream is = urlConnection.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
//
//            Gson gson = new Gson();
//
//            return gson.fromJson(isr, new TypeToken<ServisSonucu<ServisModel>>() {
//            }.getType());
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//        }
//        return null;
        Unirest.config().verifySsl(false);
        String url = link  +"startdate=" + baslangicTarihi + "&enddate=" + bitisTarihi;
        HttpResponse<JsonNode> postResult = Unirest.post(url)
                .asJson();

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.coercionConfigFor(LogicalType.POJO).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsEmpty);
        String json = postResult.getBody().toString();
        return objectMapper.readValue(json, ServisSonucu.class);
    }
}
