package school.sptech.crudrisecanvas.unittestutils;

import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.utils.Enums.MappingStatus;

import java.time.LocalDate;
import java.util.List;

public class MappingMocks {

    public static Mapping getMapping() {
        Mapping mapping = new Mapping();

        mapping.setId(1);
        mapping.setQtyAdults(2);
        mapping.setQtyChildren(0);
        mapping.setReferencePoint("Muro verde");
        mapping.setHasDisorders(false);
        mapping.setDescription("Dois adultos");
        mapping.setLatitude(-23.531682);
        mapping.setLongitude(-46.721047);
        mapping.setStatus(MappingStatus.ACTIVE);
        mapping.setDate(LocalDate.of(2024, 5, 19));
        mapping.setUsers(List.of(UserMocks.getUser()));

        return mapping;
    }
}
