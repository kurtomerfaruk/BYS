package tr.bel.gaziantep.bysweb.moduls.engelsizler.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 5.12.2025 15:37
 */
@FacesConverter(value = "eyKisiConverter")
public class EyKisiConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext fc, UIComponent comp, String value) {
        DualListModel<EyKisi> model = (DualListModel<EyKisi>) ((PickList) comp).getValue();
        for (EyKisi eyKisi : model.getSource()) {
            if (eyKisi.getId() == Integer.parseInt(value)) {
                return eyKisi;
            }
        }
        for (EyKisi eyKisi : model.getTarget()) {
            if (eyKisi.getId() == Integer.parseInt(value)) {
                return eyKisi;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent comp, Object value) {
        return getStringKey(((EyKisi) value).getId());
    }

    String getStringKey(Integer value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }
}

