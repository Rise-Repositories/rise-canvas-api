package school.sptech.crudrisecanvas.dtos.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressViacepDto {

    private String cep;
    @JsonProperty(value = "logradouro")
    private String street;
    @JsonProperty(value = "complemento")
    private String complement;
    @JsonProperty(value = "bairro")
    private String neighbourhood;
    @JsonProperty(value = "localidade")
    private String city;
    @JsonProperty(value = "uf")
    private String state;
}
