package school.sptech.crudrisecanvas.unittestutils;

import school.sptech.crudrisecanvas.entities.User;

public class UserMocks {

    public static User getUser() {
        User user = new User();
        user.setId(1);
        user.setName("marcelo");
        user.setEmail("marcelo.soares@email.com");
        user.setCpf("123.456.789-10");
        return user;
    }

    public static User getAuthUser() {
        User user = new User();
        user.setId(1);
        user.setName("marcelo");
        user.setEmail("marcelo.soares@email.com");
        user.setPassword("$2a$10$1LVW48XLAeC7pg8cJJlHNeYD5pILhE1iNZjEYH3aAE3jvaoiQmo2m");
        user.setCpf("123.456.789-10");
        return user;
    }

    public static String getToken() {
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXJjZWxvLnNvYXJlc0BlbWFpbC5jb20iLCJpYXQiOjE3MTYxMjYwNjksImV4cCI6MTgwMjUyNjA2OX0.46tyYNm8IKPGxz8cRrsIbYOSKA3EiAzE_v0eCUtBPwe9tdCmsKmQIjacxI-XzueT58ZVWsRO6tkrvA2BFbeYGQ";
    }
}