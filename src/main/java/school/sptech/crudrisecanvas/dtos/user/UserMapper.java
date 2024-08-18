package school.sptech.crudrisecanvas.dtos.user;

import java.util.List;

import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryMapper;
import school.sptech.crudrisecanvas.dtos.address.AddressMapper;
import school.sptech.crudrisecanvas.dtos.mapping.MappingMapper;
import school.sptech.crudrisecanvas.entities.User;

public class UserMapper {
    public static User toEntity(UserRequestDto userRequestDto) {
        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setCpf(userRequestDto.getCpf());
        user.setAddress(AddressMapper.toEntity(userRequestDto.getAddress()));
        return user;
    }

    public static User toEntity(UserRequestUpdateDto userRequestUpdateDto) {
        User user = new User();
        user.setName(userRequestUpdateDto.getName());
        user.setEmail(userRequestUpdateDto.getEmail());
        user.setCpf(userRequestUpdateDto.getCpf());
        user.setAddress(AddressMapper.toEntity(userRequestUpdateDto.getAddress()));
        return user;
    }

    public static UserResponseDto toResponse(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setCpf(user.getCpf());
        userResponseDto.setMapping(MappingMapper.toUserNoRelationDto(user.getMapping()));
        userResponseDto.setVoluntary(VoluntaryMapper.toUserNoRelationDto(user.getVoluntary()));
        userResponseDto.setAddress(AddressMapper.toDto(user.getAddress()));
        return userResponseDto;
    }

    public static List<UserResponseDto> toResponse(List<User> users) {
        return users == null
            ? null
            : users.stream().map(UserMapper::toResponse).toList();
    }

    public static UserResponseNoRelationDto toNoRelationDto(User user) {
        if (user == null) return null;

        UserResponseNoRelationDto userResponseNoRelationDto = new UserResponseNoRelationDto();
        userResponseNoRelationDto.setId(user.getId());
        userResponseNoRelationDto.setName(user.getName());
        userResponseNoRelationDto.setEmail(user.getEmail());
        userResponseNoRelationDto.setCpf(user.getCpf());
        userResponseNoRelationDto.setAddress(AddressMapper.toDto(user.getAddress()));
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