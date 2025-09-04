package com.kurtomerfaruk.leafmap.model.cluster;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.08.2025 11:34
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClusterModel implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 6287146755859923578L;

    private double lat;
    private double lng;
    private String title;
}
