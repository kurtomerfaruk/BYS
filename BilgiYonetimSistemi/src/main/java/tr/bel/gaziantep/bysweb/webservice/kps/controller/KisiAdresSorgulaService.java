package tr.bel.gaziantep.bysweb.webservice.kps.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import kong.unirest.core.RequestBodyEntity;
import kong.unirest.core.Unirest;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.webservice.kps.model.kisi.KisiAdresModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.08.2025 10:09
 */
public class KisiAdresSorgulaService extends KpsAbstractService<KisiAdresModel> {

    @Serial
    private static final long serialVersionUID = -4474986112757110634L;

    public KisiAdresSorgulaService() {
        super(KisiAdresModel.class);
    }

    public KisiAdresModel kisiAdresSorgula(String link, String token, KisiParameter parameter) {
        return this.postExecute(link + "AdresBilgi/TcNoileKoordinatBul", token, parameter);
    }

    @Override
    public KisiAdresModel postExecute(String link, String token, KisiParameter parameter) {
        if (StringUtil.isBlank(link) || StringUtil.isBlank(token)) {
            return null;
        }

        try {
            Unirest.config().verifySsl(false);
            Gson gson = new Gson();
            String body = gson.toJson(parameter);
            RequestBodyEntity bodyEntity = Unirest.post(link)
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .body(body);
            String json = bodyEntity.asJson().getBody().toString();
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.coercionConfigFor(LogicalType.POJO).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsEmpty);
            return objectMapper.readValue(json, KisiAdresModel.class);
        } catch (Exception ex) {
            System.out.println("Sorgulama yapılırken hata oluştu:" + ex.getMessage());
            return null;
        }
    }

}
