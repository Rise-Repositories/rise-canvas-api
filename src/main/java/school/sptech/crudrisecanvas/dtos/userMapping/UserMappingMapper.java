package school.sptech.crudrisecanvas.dtos.userMapping;

import school.sptech.crudrisecanvas.dtos.mapping.MappingMapper;
import school.sptech.crudrisecanvas.dtos.user.UserMapper;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.entities.UserMapping;

public class UserMappingMapper {

    public static UserMapping toEntity(User user, Mapping mapping){
        if (user == null || mapping == null) return null;

        UserMapping userMapping = new UserMapping();
        userMapping.setUser(user);
        userMapping.setMapping(mapping);

        return userMapping;
    }

    public static UserMappingDto toResponse(UserMapping userMapping){
        if(userMapping == null) return null;
        UserMappingDto result = new UserMappingDto();
        result.setId(userMapping.getId());
        result.setMapping(MappingMapper.toResponse(userMapping.getMapping()));
        result.setUser(UserMapper.toResponse(userMapping.getUser()));

        return result;
    }
}
