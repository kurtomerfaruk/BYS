package tr.bel.gaziantep.bysweb.webservice.gazikart.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tr.bel.gaziantep.bysweb.webservice.gazikart.model.ServisModel;
import tr.bel.gaziantep.bysweb.webservice.gazikart.model.ServisSonucu;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serial;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 13:49
 */
public class GaziKartService implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -2710217811364283386L;

    public ServisSonucu<ServisModel> getList(String link, String baslangicTarihi, String bitisTarihi) {
        try {
            String format = "yyyyMMdd";
            URL url = new URL(link+"startdate=" + baslangicTarihi + "&enddate=" + bitisTarihi);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);

            Gson gson = new Gson();

            return gson.fromJson(isr, new TypeToken<ServisSonucu<ServisModel>>() {
            }.getType());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
