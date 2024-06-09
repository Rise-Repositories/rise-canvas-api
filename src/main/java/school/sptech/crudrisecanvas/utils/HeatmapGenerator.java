package school.sptech.crudrisecanvas.utils;

import school.sptech.crudrisecanvas.dtos.mapping.MappingHeatmapDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.acos;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

public class HeatmapGenerator {

    private static final int EARTH_RADIUS_IN_M = 6371000;
    public static Double[][] getHeatmapPointsNotHelped(List<MappingHeatmapDto> mappings, double radiusToGroup, LocalDateTime olderThan) {
        if (mappings == null || mappings.isEmpty()) {
            return new Double[0][0];

        } else {
            Double[][] points = new Double[mappings.size()][3];
            int qtyPoints = 0;
            Double maxIntensity = 0.;

//            List<Mapping> validMappings = mappings.stream()
//                .filter((map) -> map.getMappingActions().isEmpty() || !map.getMappingActions().stream()
//                    .filter((mapAction) -> !mapAction.getNoDonation() &&
//                                            mapAction.getAction().getDatetimeStart().isBefore(olderThan)
//                    ).toList().isEmpty()
//                ).toList();

            List<MappingHeatmapDto> validMappings = mappings.stream()
                    .filter((map) -> map.getDatetimeStart() == null ||
                                     map.getDatetimeStart().isBefore(olderThan)).toList();

            for (int i = 0; i < validMappings.size(); i++) {
                boolean newPoint = true;
                MappingHeatmapDto curMap = validMappings.get(i);

                for (int j = 0; j < qtyPoints; j++) {
                    if (getDistance(
                            curMap.getLatitude(), curMap.getLongitude(),
                            points[j][0], points[j][1]
                    ) <= radiusToGroup) {
                        points[j][2] += curMap.getQtyAdults() + curMap.getQtyChildren();
                        maxIntensity = Math.max(maxIntensity, points[j][2]);
                        newPoint = false;
                        break;
                    }
                }

                if (newPoint) {
                    points[qtyPoints][0] = curMap.getLatitude();
                    points[qtyPoints][1] = curMap.getLongitude();
                    points[qtyPoints][2] = curMap.getQtyAdults().doubleValue() + curMap.getQtyChildren().doubleValue();

                    maxIntensity = Math.max(maxIntensity, points[qtyPoints][2]);
                    qtyPoints++;
                }
            }

            Double finalMaxIntensity = maxIntensity;
            points = Arrays.stream(Arrays.copyOfRange(points, 0, qtyPoints))
                    .peek((heatData) -> heatData[2] = (heatData[2] * 0.9 / finalMaxIntensity) + 0.1)
                    .toArray(size -> new Double[size][3]);

            return points;
        }
    }

    private static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        return acos(
                sin(toRadians(lat1)) * sin(toRadians(lat2)) +
                cos(toRadians(lat1)) * cos(toRadians(lat2)) * cos(toRadians(lon2) - toRadians(lon1))
        ) * EARTH_RADIUS_IN_M;
    }
}
