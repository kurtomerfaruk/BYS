package tr.bel.gaziantep.bysweb.webservice.kps.util;

import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameter;

import java.time.LocalDate;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 15:39
 */
public class KpsUtil {
    public static KisiParameter setDate(LocalDate date){
        KisiParameter parameter = new KisiParameter();
        parameter.setDogumYil(date.getYear());
        parameter.setDogumAy(date.getMonthValue());
        parameter.setDogumGun(date.getDayOfMonth());
        return parameter;
    }
}
