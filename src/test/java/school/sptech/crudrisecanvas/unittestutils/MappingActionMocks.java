package school.sptech.crudrisecanvas.unittestutils;

import school.sptech.crudrisecanvas.entities.MappingAction;

public class MappingActionMocks {

    public static MappingAction getMappingAction() {
        MappingAction mappingAction = new MappingAction(
                ActionMocks.getAction(),
                MappingMocks.getMapping(),
                3,
                0,
                false,
                false,
                "Entrega de cestas básicas"
        );

        return mappingAction;
    }
}
