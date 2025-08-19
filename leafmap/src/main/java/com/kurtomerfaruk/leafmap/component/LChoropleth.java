package com.kurtomerfaruk.leafmap.component;

import com.kurtomerfaruk.leafmap.utils.LeafMap;
import jakarta.faces.component.UIComponentBase;

import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.08.2025 14:56
 */
public class LChoropleth extends UIComponentBase {

    protected enum PropertyKeys {
        data,
        title,
        info,
        values,
        click,
        unit
    }

    @Override
    public String getFamily() {
        return LeafMap.COMPONENT_FAMILY;
    }

    public String getData() {
        return (String) getStateHelper().eval(PropertyKeys.data);
    }

    public void setData(final String data) {
        getStateHelper().put(PropertyKeys.data, data);
    }

    public String getTitle() {
        return (String) getStateHelper().eval(PropertyKeys.title, "Kişi Yoğunluk");
    }

    public void setTitle(final String title) {
        getStateHelper().put(PropertyKeys.title, title);
    }

    public String getInfo() {
        return (String) getStateHelper().eval(PropertyKeys.info, "İlçenin üzerine geliniz");
    }

    public void setInfo(final String info) {
        getStateHelper().put(PropertyKeys.info, info);
    }

    public List<Integer> getValues() {
        return (List<Integer>) getStateHelper().eval(PropertyKeys.values);
    }

    public void setValues(final List<Integer> values) {
        getStateHelper().put(PropertyKeys.values, values);
    }

    public String getClick() {
        return (String) getStateHelper().eval(PropertyKeys.click);
    }

    public void setClick(final String click) {
        getStateHelper().put(PropertyKeys.click, click);
    }

    public String getUnit() {
        return (String) getStateHelper().eval(PropertyKeys.unit);
    }

    public void setUnit(final String unit) {
        getStateHelper().put(PropertyKeys.unit, unit);
    }
}
