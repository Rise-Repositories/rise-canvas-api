package school.sptech.crudrisecanvas.integrationtests.utils.paths;

public enum ActionEnum {

    BASE_URI("%s/actions".formatted(BaseEnum.BASE_PATH.path)),
    BY_ID("%s/".formatted(BASE_URI.path));

    ActionEnum(String path) { this.path = path; }

    public final String path;
}
