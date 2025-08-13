package tr.bel.gaziantep.bysweb.webservice.kps.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import kong.unirest.core.GetRequest;
import kong.unirest.core.Unirest;
import org.apache.poi.ss.formula.functions.T;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.webservice.kps.model.koordinat.KoordinatModel;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 20.06.2025 10:44
 */
public class KoordinatService extends KpsAbstractService<KoordinatModel> {

    @Serial
    private static final long serialVersionUID = 5509602949908153134L;

    public KoordinatService() {
        super(KoordinatModel.class);
    }

    public KoordinatModel binaNoIleKoordinatSorgula(String link, String token, Integer binaNo) {
        if (StringUtil.isBlank(link) || StringUtil.isBlank(token)) {
            return null;
        }
        try {
            Unirest.config().verifySsl(false);
            GetRequest bodyEntity = Unirest.get(link+"AdresBilgi/BinaKoordinat/?Uavt_Bina_No="+binaNo)
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json");
            String json = bodyEntity.asJson().getBody().toString();
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.coercionConfigFor(LogicalType.POJO).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsEmpty);
            return (KoordinatModel) objectMapper.readValue(json, KoordinatModel.class);
        } catch (Exception ex) {
            System.out.println("Sorgulama yapılırken hata oluştu:" + ex.getMessage());
            return null;
        }
    }
}
