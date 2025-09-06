package com.kurtomerfaruk.leafmap.model.heatmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.09.2025 21:52
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeatmapModel implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 1019004692183864895L;
    private double lat;
    private double lng;
    private int count;
}
