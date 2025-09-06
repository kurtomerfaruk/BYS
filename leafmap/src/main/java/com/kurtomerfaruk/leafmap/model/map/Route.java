package com.kurtomerfaruk.leafmap.model.map;

import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Overlay;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 5.09.2025 23:04
 */
public class Route<T> extends Overlay<T> {
    @Serial
    private static final long serialVersionUID = -6612976800002117046L;

    private String title;
    private LatLng latlng;

    public Route(LatLng latlng) {
        this.latlng = latlng;
    }

    public Route(LatLng latlng, String title) {
        this.latlng = latlng;
        this.title = title;
    }

    public Route(LatLng latlng, String title, T data) {
        super(data);
        this.latlng = latlng;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }
}
