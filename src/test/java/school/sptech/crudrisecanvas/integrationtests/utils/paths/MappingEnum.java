package school.sptech.crudrisecanvas.integrationtests.utils.paths;

public enum MappingEnum {

    BASE_URI("%s/mapping".formatted(BaseEnum.BASE_PATH.path)),
    BY_ID("%s/".formatted(BASE_URI.path));

    MappingEnum(String path) { this.path = path; }

    public final String path;
}
