package school.sptech.crudrisecanvas.integrationtests.utils.paths;

public enum BaseEnum {

    BASE_PATH("http://localhost:8080");

    BaseEnum(String path) { this.path = path; }

    public final String path;
}
