package school.sptech.crudrisecanvas.integrationtests.utils.paths;

public enum VoluntaryEnum {

    BASE_URI("%s/voluntary".formatted(BaseEnum.BASE_PATH.path)),
    BY_ID("%s/".formatted(BASE_URI.path));

    VoluntaryEnum(String path) { this.path = path; }

    public final String path;
}
