package school.sptech.crudrisecanvas.unittestutils;

import school.sptech.crudrisecanvas.entities.UserMapping;

public class UserMappingMocks {
    public static UserMapping getUserMapping() {
        UserMapping userMapping = new UserMapping();

        userMapping.setId(1);
        userMapping.setUser(UserMocks.getUser());

        return userMapping;
    }

    public static UserMapping getUserMapping2() {
        UserMapping userMapping = new UserMapping();

        userMapping.setId(2);
        userMapping.setUser(UserMocks.getUserWithVoluntary());

        return userMapping;
    }
    
}
