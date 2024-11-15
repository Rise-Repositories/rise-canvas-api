package school.sptech.crudrisecanvas.utils;

import lombok.Data;

@Data
public class Coordinates {
    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates(String coordinates) {
        String[] coords = coordinates.split(",");
        this.latitude = Double.parseDouble(coords[0]);
        this.longitude = Double.parseDouble(coords[1]);
    }

    private Double latitude;
    private Double longitude;
}
