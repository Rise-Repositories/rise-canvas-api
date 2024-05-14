package school.sptech.crudrisecanvas.utils.paths;

public enum UserEnum {

    BASE_URI("%s/user".formatted(BaseEnum.BASE_PATH.path)),
    LOGIN("%s/auth/login".formatted(BASE_URI.path)),
    CREATE("%s/auth/register".formatted(BASE_URI.path)),
    BY_ID("%s/".formatted(BASE_URI.path));

    UserEnum(String path) { this.path = path; }

    public final String path;
}
