package school.sptech.crudrisecanvas.dtos.userMapping;

import lombok.Getter;

@Getter
public class UserMappingCountDto {
    private Long qtyMapping;
    private Integer userId;

    public UserMappingCountDto(Long qtyMapping, Integer userId) {
        this.qtyMapping = qtyMapping;
        this.userId = userId;
    }
}
