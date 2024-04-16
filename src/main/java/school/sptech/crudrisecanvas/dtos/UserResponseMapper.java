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
        userResponseDto.setMapping(user.getMapping());
        return userResponseDto;
    }

    public static User toEntity(UserResponseDto userResponseDto) {
        User user = new User();
        user.setName(userResponseDto.getName());
        user.setEmail(userResponseDto.getEmail());
        user.setCpf(userResponseDto.getCpf());
        user.setIp(userResponseDto.getIp());
        user.setMapping(userResponseDto.getMapping());
        return user;
    }

    public static List<UserResponseDto> toDto(List<User> users) {
        return users.stream().map(UserResponseMapper::toDto).toList();
    }
    
}
