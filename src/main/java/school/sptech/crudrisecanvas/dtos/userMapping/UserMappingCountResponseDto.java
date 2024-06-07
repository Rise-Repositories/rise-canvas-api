package school.sptech.crudrisecanvas.dtos.userMapping;

import lombok.Getter;

@Getter
public class UserMappingCountResponseDto {
    private Double zero;
    private Double oneOrTwo;
    private Double threeOrFour;
    private Double fiveOrMore;

    public UserMappingCountResponseDto() {
        this.zero = 0.;
        this.oneOrTwo = 0.;
        this.threeOrFour = 0.;
        this.fiveOrMore = 0.;
    }

    public void addTo(Long count) {
        if (count == 0) {
            zero++;
        } else if (count < 3) {
            oneOrTwo++;
        } else if (count < 5) {
            threeOrFour++;
        } else {
            fiveOrMore++;
        }
    }

    public Double total() {
        return zero + oneOrTwo + threeOrFour + fiveOrMore;
    }

    public Double getZero() {
        return zero / total();
    }

    public Double getOneOrTwo() {
        return oneOrTwo / total();
    }

    public Double getThreeOrFour() {
        return threeOrFour / total();
    }

    public Double getFiveOrMore() {
        return fiveOrMore / total();
    }
}
