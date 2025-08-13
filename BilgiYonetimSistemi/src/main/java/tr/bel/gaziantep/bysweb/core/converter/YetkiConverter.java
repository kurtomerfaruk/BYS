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
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyYetki;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyYetkiService;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 16:36
 */
@Named
@ApplicationScoped
@FacesConverter(value = "yetkiConverter", managed = true)
public class YetkiConverter implements Converter<SyYetki> {

    @Inject
    private SyYetkiService service;

    @Override
    public SyYetki getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.trim().isEmpty()) {
            try {
                return service.find(Integer.parseInt(value));
            }
            catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid yetki."));
            }
        }
        else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, SyYetki value) {
        if (value != null) {
            return String.valueOf(value.getId());
        }
        else {
            return null;
        }
    }
}