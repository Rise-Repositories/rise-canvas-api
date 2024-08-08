package school.sptech.crudrisecanvas.dtos.address;

import school.sptech.crudrisecanvas.entities.Address;

public class AddressMapper {

    public static Address toEntity(AddressViacepDto dto, Integer number, String complement) {
        if (dto == null) return null;

        Address entity = new Address();

        entity.setCep(dto.getCep());
        entity.setStreet(dto.getStreet());
        entity.setNeighbourhood(dto.getNeighbourhood());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setNumber(number);
        entity.setComplement(complement);

        return entity;
    }

    public static Address toEntity(AddressRequestDto dto) {
        if (dto == null) return null;

        Address entity = new Address();

        entity.setCep(dto.getCep());
        entity.setNumber(dto.getNumber());
        entity.setComplement(dto.getComplement());

        return entity;
    }

    public static AddressResponseDto toDto(Address entity) {
        if (entity == null) return null;

        AddressResponseDto dto = new AddressResponseDto();

        dto.setId(entity.getId());
        dto.setCep(entity.getCep());
        dto.setStreet(entity.getStreet());
        dto.setNeighbourhood(entity.getNeighbourhood());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setNumber(entity.getNumber());
        dto.setComplement(entity.getComplement());

        return dto;
    }
}
