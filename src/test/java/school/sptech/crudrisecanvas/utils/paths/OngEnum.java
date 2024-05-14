package school.sptech.crudrisecanvas.utils.paths;

public enum OngEnum {

    BASE_URI("%s/ong".formatted(BaseEnum.BASE_PATH.path)),
    BY_ID("%s/".formatted(BASE_URI.path));

    OngEnum(String path) { this.path = path; }

    public final String path;
}
