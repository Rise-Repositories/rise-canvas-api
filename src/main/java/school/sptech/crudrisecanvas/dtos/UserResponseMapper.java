package school.sptech.crudrisecanvas.dtos;

import java.util.List;

import school.sptech.crudrisecanvas.entities.User;

public class UserResponseMapper {
    public static UserResponseDto toDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setCpf(user.getCpf());
        userResponseDto.setIp(user.getIp());
        userResponseDto.setMapping(MappingResponseMapper.toNoRelationDto(user.getMapping()));
        return userResponseDto;
    }

    public static UserResponseNoRelationDto toNoRelationDto(User user) {
        UserResponseNoRelationDto userResponseNoRelationDto = new UserResponseNoRelationDto();
        userResponseNoRelationDto.setId(user.getId());
        userResponseNoRelationDto.setName(user.getName());
        userResponseNoRelationDto.setEmail(user.getEmail());
        userResponseNoRelationDto.setCpf(user.getCpf());
        userResponseNoRelationDto.setIp(user.getIp());
        return userResponseNoRelationDto;
    }

    public static List<UserResponseDto> toDto(List<User> users) {
        return users.stream().map(UserResponseMapper::toDto).toList();
    }
    
}
