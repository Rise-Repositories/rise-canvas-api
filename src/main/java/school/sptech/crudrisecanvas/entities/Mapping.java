package school.sptech.crudrisecanvas.entities;

import java.time.LocalDate;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import school.sptech.crudrisecanvas.Utils.Converters.MappingStatusConvert;
import school.sptech.crudrisecanvas.Utils.Enums.MappingStatus;

@Entity
public class Mapping {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String description;

    //TODO: add validation 
    private Double latitute;

    //TODO: add validation
    private Double longitude;

    @Convert(converter = MappingStatusConvert.class)
    private MappingStatus status;

    @NotBlank
    @PastOrPresent
    private LocalDate date;

    //getter and setter

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitute() {
        return latitute;
    }

    public void setLatitute(Double latitute) {
        this.latitute = latitute;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public MappingStatus getStatus() {
        return status;
    }

    public void setStatus(MappingStatus status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
