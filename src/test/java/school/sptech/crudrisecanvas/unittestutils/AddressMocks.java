package school.sptech.crudrisecanvas.unittestutils;

import school.sptech.crudrisecanvas.dtos.address.AddressRequestDto;
import school.sptech.crudrisecanvas.dtos.address.AddressRequestUpdateDto;
import school.sptech.crudrisecanvas.entities.Address;

public class AddressMocks {


    public static Address getAddress() {
        Address address = new Address();
        address.setId(1);
        address.setCity("São Paulo");
        address.setState("SP");
        address.setNeighbourhood("Jaguaré");
        address.setStreet("Rua Tiagem");
        address.setCep("05334050");
        address.setNumber(123);
        address.setComplement("apto 2");
        return address;
    }


    public static Address getAddress2() {
        Address address = new Address();
        address.setId(1);
        address.setCity("São Paulo");
        address.setState("SP");
        address.setNeighbourhood("Cerqueira César");
        address.setStreet("Rua Haddock Lobo");
        address.setCep("01414001");
        address.setNumber(595);
        address.setComplement("");
        return address;
    }

    public static AddressRequestDto getAddressRequestDto() {
        AddressRequestDto dto = new AddressRequestDto();
        dto.setCep("05334050");
        dto.setNumber(123);
        dto.setComplement("apto 2");
        return dto;
    }

    public static AddressRequestUpdateDto getAddressRequestUpdateDto() {
        AddressRequestUpdateDto dto = new AddressRequestUpdateDto();
        dto.setCep("01414001");
        dto.setNumber(595);
        dto.setComplement("");
        return dto;
    }
}
