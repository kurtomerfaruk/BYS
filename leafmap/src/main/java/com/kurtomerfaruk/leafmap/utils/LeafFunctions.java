package com.kurtomerfaruk.leafmap.utils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.08.2025 15:22
 */
public class LeafFunctions implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 8194559955221043838L;

    public static String generateGrades(List<Integer> values) {
        List<Integer> result = new ArrayList<>();
        int minValue = values.stream().min(Integer::compare).orElse(0);
        int maxValue = values.stream().max(Integer::compare).orElse(0);

        int range = maxValue - minValue;
        int stepSize = range / 6;

        for (int i = 0; i < 6; i++) {

            int lowerBound = minValue + i * stepSize;
            int upperBound = minValue + (i + 1) * stepSize;
            if (!result.contains(lowerBound)) {
                result.add(lowerBound);
            }
            if (!result.contains(upperBound)) {
                result.add(upperBound);
            }
        }
        return result.toString();
    }
}
