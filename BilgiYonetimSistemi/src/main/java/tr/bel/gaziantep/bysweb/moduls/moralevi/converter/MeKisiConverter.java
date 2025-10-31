package tr.bel.gaziantep.bysweb.moduls.moralevi.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeKisi;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.10.2025 11:36
 */
@FacesConverter(value = "meKisiConverter")
public class MeKisiConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext fc, UIComponent comp, String value) {
        DualListModel<MeKisi> model = (DualListModel<MeKisi>) ((PickList) comp).getValue();
        for (MeKisi employee : model.getSource()) {
            if (employee.getId() == Integer.parseInt(value)) {
                return employee;
            }
        }
        for (MeKisi employee : model.getTarget()) {
            if (employee.getId() == Integer.parseInt(value)) {
                return employee;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent comp, Object value) {
        return getStringKey(((MeKisi) value).getId());
    }

    String getStringKey(Integer value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }
}
