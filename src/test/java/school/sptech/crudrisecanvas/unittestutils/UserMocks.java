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

    public static User getUser2() {
        User user = new User();
        user.setId(2);
        user.setName("Ana Soares");
        user.setEmail("ana.soares@email.com");
        user.setCpf("080.277.410-50");
        return user;
    }

    public static User getUser3() {
        User user = new User();
        user.setId(3);
        user.setName("José Soares");
        user.setEmail("jose.soares@email.com");
        user.setCpf("336.089.690-47");
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

    public static User getUserWithVoluntary2() {
        User user = getUser2();
        Voluntary voluntary = VoluntaryMocks.getVoluntary2();
        user.setVoluntary(List.of(voluntary));
        return user;
    }

    public static User getUserWithVoluntary3() {
        User user = getUser3();
        Voluntary voluntary = VoluntaryMocks.getVoluntary3();
        user.setVoluntary(List.of(voluntary));
        return user;
    }
}
