package com.kurtomerfaruk.leafmap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 3.09.2025 16:20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Point implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -7296199353813167777L;
    private String name;
    private double lat;
    private double lng;
}
