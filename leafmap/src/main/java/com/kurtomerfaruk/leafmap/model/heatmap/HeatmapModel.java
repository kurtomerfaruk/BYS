package com.kurtomerfaruk.leafmap.model.heatmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.08.2025 11:20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeatmapModel implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -6427328569456623031L;

    private double lat;
    private double lng;
    private int count;
}
