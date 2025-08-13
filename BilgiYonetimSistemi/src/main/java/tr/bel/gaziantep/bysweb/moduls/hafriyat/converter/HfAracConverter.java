package tr.bel.gaziantep.bysweb.moduls.hafriyat.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.HfArac;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.service.HfAracService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.07.2025 08:45
 */
@FacesConverter(value = "hfAracConverter")
public class HfAracConverter  implements Converter {

    @Inject
    private HfAracService ejbFacade;


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || FacesUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    Integer getKey(String value) {
        Integer key;
        key = Integer.valueOf(value);
        return key;
    }

    String getStringKey(Integer value) {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof HfArac) {
            HfArac o = (HfArac) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), HfArac.class.getName()});
            return null;
        }
    }
}