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
        mapping.setUsersMappings(List.of());

        return mapping;
    }

    public static Mapping getMapping2() {
        Mapping mapping = new Mapping();

        mapping.setId(2);
        mapping.setQtyAdults(1);
        mapping.setQtyChildren(1);
        mapping.setReferencePoint("No canto da praça");
        mapping.setHasDisorders(false);
        mapping.setDescription("1 mulher e 1 menino");
        mapping.setLatitude(-23.530192);
        mapping.setLongitude(-46.724012);
        mapping.setStatus(MappingStatus.ACTIVE);
        mapping.setDate(LocalDate.of(2024, 3, 8));
        mapping.setUsersMappings(List.of(UserMappingMocks.getUserMapping()));

        return mapping;
    }

    public static List<Mapping> getMappingList() {
        return List.of(getMapping(), getMapping2());
    }

    public static Mapping getCreationMapping() {
        Mapping mapping = new Mapping();

        mapping.setId(1);
        mapping.setQtyAdults(2);
        mapping.setQtyChildren(0);
        mapping.setReferencePoint("Muro verde");
        mapping.setHasDisorders(false);
        mapping.setDescription("Dois adultos");
        mapping.setLatitude(-23.531682);
        mapping.setLongitude(-46.721047);
        mapping.setStatus(null);
        mapping.setDate(LocalDate.of(2024, 5, 19));
        mapping.setUsersMappings(List.of(UserMappingMocks.getUserMapping(), UserMappingMocks.getUserMapping2()));

        return mapping;
    }
}
