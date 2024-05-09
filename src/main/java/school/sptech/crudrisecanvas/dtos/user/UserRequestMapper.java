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

    public static UserRequestDto toDto (User user) {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName(user.getName());
        userRequestDto.setEmail(user.getEmail());
        userRequestDto.setPassword(user.getPassword());
        userRequestDto.setCpf(user.getCpf());
        return userRequestDto;
    }


    
}