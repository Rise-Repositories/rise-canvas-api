package school.sptech.crudrisecanvas.unittestutils;

import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.entities.Voluntary;

import java.util.List;

public class UserMocks {

    public static User getUser() {
        User user = new User();
        user.setId(1);
        user.setName("Marcelo Soares");
        user.setEmail("marcelo.soares@email.com");
        user.setCpf("017.895.420-90");
        return user;
    }

    public static User getAuthUser() {
        User user = new User();
        user.setId(1);
        user.setName("Marcelo Soares");
        user.setEmail("marcelo.soares@email.com");
        user.setPassword("$2a$10$1LVW48XLAeC7pg8cJJlHNeYD5pILhE1iNZjEYH3aAE3jvaoiQmo2m");
        user.setCpf("017.895.420-90");
        return user;
    }

    public static String getToken() {
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXJjZWxvLnNvYXJlc0BlbWFpbC5jb20iLCJpYXQiOjE3MTYxMjYwNjksImV4cCI6MTgwMjUyNjA2OX0.46tyYNm8IKPGxz8cRrsIbYOSKA3EiAzE_v0eCUtBPwe9tdCmsKmQIjacxI-XzueT58ZVWsRO6tkrvA2BFbeYGQ";
    }

    public static User getUserWithVoluntary() {
        User user = getUser();
        Voluntary voluntary = VoluntaryMocks.getVoluntary();
        user.setVoluntary(List.of(voluntary));
        return user;
    }
}
