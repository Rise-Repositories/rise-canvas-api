package school.sptech.crudrisecanvas.Utils.Enums;

public enum OngStatus {
    ACPECTED("aceito"),
    PENDING("pendente"),
    REJECTED("rejeitado");

    private String status;

    OngStatus(String status) {
        this.status = status;
    }
}
