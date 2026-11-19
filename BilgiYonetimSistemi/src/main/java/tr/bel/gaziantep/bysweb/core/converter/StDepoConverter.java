package tr.bel.gaziantep.bysweb.core.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StDepoService;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 19.11.2026 14:05
 */
@Named
@ApplicationScoped
@FacesConverter(value = "yetkiConverter", managed = true)
public class StDepoConverter implements Converter<StDepo> {
    @Inject
    private StDepoService service;

    @Override
    public StDepo getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.trim().isEmpty()) {
            try {
                return service.find(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid stok takip depo."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, StDepo value) {
        if (value != null) {
            return String.valueOf(value.getId());
        } else {
            return null;
        }
    }
}
