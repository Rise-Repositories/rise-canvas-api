package school.sptech.crudrisecanvas.dtos.user;

import school.sptech.crudrisecanvas.entities.User;

public class UserRequestMapper {
    public static User toEntity(UserRequestDto userRequestDto) {
        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setCpf(userRequestDto.getCpf());
        return user;
    }

    public static User toEntity(UserRequestUpdateDto userRequestUpdateDto) {
        User user = new User();
        user.setName(userRequestUpdateDto.getName());
        user.setEmail(userRequestUpdateDto.getEmail());
        user.setCpf(userRequestUpdateDto.getCpf());
        return user;
    }

    
}