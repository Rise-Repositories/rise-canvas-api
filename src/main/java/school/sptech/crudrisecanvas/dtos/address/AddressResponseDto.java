package school.sptech.crudrisecanvas.dtos.address;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponseDto {

    private Integer id;
    private String cep;
    private String street;
    private String complement;
    private Integer number;
    private String neighbourhood;
    private String city;
    private String state;
}
