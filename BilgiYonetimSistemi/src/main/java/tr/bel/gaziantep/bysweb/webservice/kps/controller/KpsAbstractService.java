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
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameters;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KodParameter;

import java.io.Serial;
import java.util.ArrayList;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:04
 */
public abstract class KpsAbstractService<T> implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -5465445649706451184L;

    private Class<T> itemClass;

    public KpsAbstractService(Class<T> itemClass) {
        this.itemClass = itemClass;
    }

    public <T> T postExecute(String link, String token, KisiParameters parameters) {
        if (StringUtil.isBlank(link) || StringUtil.isBlank(token)) {
            return null;
        }

        try {
            Unirest.config().verifySsl(false);
            Gson gson = new Gson();
            String body = gson.toJson(parameters);
            RequestBodyEntity bodyEntity = Unirest.post(link)
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .body(body);
            String json = bodyEntity.asJson().getBody().toString();
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.coercionConfigFor(LogicalType.POJO).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsEmpty);
            return (T) objectMapper.readValue(json, itemClass);
        } catch (Exception ex) {
            System.out.println("Tc Kimlik No Sorgulama yapılırken hata oluştu:" + ex.getMessage());
            return null;
        }
    }

    public <T> T postExecute(String link, String token, KisiParameter parameter) {
        if (StringUtil.isBlank(link) || StringUtil.isBlank(token)) {
            return null;
        }

        try {
            Unirest.config().verifySsl(false);
            Gson gson = new Gson();
            KisiParameters parameters = new KisiParameters();
            parameters.setKisiler(new ArrayList<>());
            parameters.getKisiler().add(parameter);
            String body = gson.toJson(parameters);
            RequestBodyEntity bodyEntity = Unirest.post(link)
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .body(body);
            String json = bodyEntity.asJson().getBody().toString();
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.coercionConfigFor(LogicalType.POJO).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsEmpty);
            return (T) objectMapper.readValue(json, itemClass);
        } catch (Exception ex) {
            System.out.println("Sorgulama yapılırken hata oluştu:" + ex.getMessage());
            return null;
        }
    }

    public <T> T postExecute(String link, String token, KodParameter parameter) {
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
            return (T) objectMapper.readValue(json, itemClass);
        } catch (Exception ex) {
            System.out.println("Sorgulama yapılırken hata oluştu:" + ex.getMessage());
            return null;
        }
    }
}
