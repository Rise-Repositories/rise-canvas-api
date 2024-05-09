package school.sptech.crudrisecanvas.dtos.user;

import java.util.List;

import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseMapper;
import school.sptech.crudrisecanvas.dtos.ong.OngRequestDto;
import school.sptech.crudrisecanvas.entities.User;

public class UserMapper {
    public static User toEntity(UserRequestDto userRequestDto) {
        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setCpf(userRequestDto.getCpf());
        return user;
    }

    public static User toEntity(OngRequestDto userOng) {
        User user = new User();
        user.setName(userOng.getNameUser());
        user.setEmail(userOng.getEmailUser());
        user.setCpf(userOng.getCpfUser());
        user.setPassword(userOng.getPasswordUser());
        return user;
    }

    public static User toEntity(UserRequestUpdateDto userRequestUpdateDto) {
        User user = new User();
        user.setName(userRequestUpdateDto.getName());
        user.setEmail(userRequestUpdateDto.getEmail());
        user.setCpf(userRequestUpdateDto.getCpf());
        return user;
    }

    public static UserResponseDto toDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setCpf(user.getCpf());
        userResponseDto.setMapping(MappingResponseMapper.toNoRelationDto(user.getMapping()));
        return userResponseDto;
    }

    public static List<UserResponseDto> toDto(List<User> users) {
        return users == null
            ? null
            : users.stream().map(UserMapper::toDto).toList();
    }

    public static UserResponseNoRelationDto toNoRelationDto(User user) {
        UserResponseNoRelationDto userResponseNoRelationDto = new UserResponseNoRelationDto();
        userResponseNoRelationDto.setId(user.getId());
        userResponseNoRelationDto.setName(user.getName());
        userResponseNoRelationDto.setEmail(user.getEmail());
        userResponseNoRelationDto.setCpf(user.getCpf());
        return userResponseNoRelationDto;
    }

    public static List<UserResponseNoRelationDto> toNoRelationDto(List<User> users) {
        return users == null 
            ? null 
            : users.stream().map(UserMapper::toNoRelationDto).toList();
    }

    
    public static UserTokenDto toTokenDto(User user, String token) {
        UserTokenDto userToken = new UserTokenDto();
        userToken.setUserId(user.getId());
        userToken.setNome(user.getName());
        userToken.setEmail(user.getEmail());
        userToken.setToken(token);
        return userToken;
    }

    
}