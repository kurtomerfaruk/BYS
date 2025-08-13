package tr.bel.gaziantep.bysweb.webservice.kps.model;


import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.adres.AdresModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk.BilesikKutukModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.kisi.KisiAdresModel;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 15:43
 */
@Setter
@Getter
public class KpsModel {

    private BilesikKutukModel kutukModel;
    private AdresModel adresModel;
    private KisiAdresModel kisiAdresModel;

}
