package tr.bel.gaziantep.bysweb.core.utils;



import org.primefaces.model.map.Point;

import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 3.09.2025 16:06
 */
public class GeoUtil {

    private static final double EARTH_RADIUS = 6371; // km

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return EARTH_RADIUS * c;
    }

    public static List<Point> solve(Point start, List<Point> points) {
        List<Point> route = new ArrayList<>();
        Set<Point> remaining = new HashSet<>(points);

        Point current = start;
        route.add(current);
        while (!remaining.isEmpty()) {
            Point finalCurrent = current;
            Point nearest = remaining.stream()
                    .min(Comparator.comparingDouble(p -> GeoUtil.haversine(
                            finalCurrent.getX(), finalCurrent.getY(),
                            p.getX(), p.getY())))
                    .orElseThrow();

            route.add(nearest);
            remaining.remove(nearest);
            current = nearest;
        }

        // dönüş
        route.add(start);

        return route;
    }

    public static List<Point> nearestNeighbor(Point start, List<Point> points) {
        List<Point> route = new ArrayList<>();
        Set<Point> remaining = new HashSet<>(points);

        Point current = start;
        route.add(current);

        while (!remaining.isEmpty()) {
            Point finalCurrent = current;
            Point nearest = remaining.stream()
                    .min(Comparator.comparingDouble(p -> GeoUtil.haversine(
                            finalCurrent.getX(), finalCurrent.getY(),
                            p.getX(), p.getY())))
                    .orElseThrow();

            route.add(nearest);
            remaining.remove(nearest);
            current = nearest;
        }

        // tekrar başlangıca dön
        route.add(start);

        return route;
    }

    public static List<Point> twoOpt(List<Point> route) {
        boolean improvement = true;
        double bestDistance = totalDistance(route);

        while (improvement) {
            improvement = false;
            for (int i = 1; i < route.size() - 2; i++) {
                for (int j = i + 1; j < route.size() - 1; j++) {
                    List<Point> newRoute = twoOptSwap(route, i, j);
                    double newDistance = totalDistance(newRoute);

                    if (newDistance < bestDistance) {
                        route = newRoute;
                        bestDistance = newDistance;
                        improvement = true;
                    }
                }
            }
        }
        return route;
    }

    private static List<Point> twoOptSwap(List<Point> route, int i, int j) {
        List<Point> newRoute = new ArrayList<>(route.subList(0, i));
        List<Point> reversed = new ArrayList<>(route.subList(i, j + 1));
        Collections.reverse(reversed);
        newRoute.addAll(reversed);
        newRoute.addAll(route.subList(j + 1, route.size()));
        return newRoute;
    }

    public static double totalDistance(List<Point> route) {
        double distance = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            distance += haversine(
                    route.get(i).getX(), route.get(i).getY(),
                    route.get(i + 1).getX(), route.get(i + 1).getY());
        }
        return distance;
    }
}
