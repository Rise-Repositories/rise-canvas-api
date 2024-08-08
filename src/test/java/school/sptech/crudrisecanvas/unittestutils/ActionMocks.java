package school.sptech.crudrisecanvas.unittestutils;

import school.sptech.crudrisecanvas.dtos.action.ActionRequestDto;
import school.sptech.crudrisecanvas.entities.Action;

import java.time.LocalDateTime;
import java.util.List;

public class ActionMocks {

    public static Action getAction() {
        Action action = new Action();
        action.setId(1);
        action.setName("R. Jacques Gabriel, 260");
        action.setDatetimeStart(LocalDateTime.of(2024, 5, 15, 10, 30, 0));
        action.setDatetimeEnd(LocalDateTime.of(2024, 5, 15, 17, 0, 0));
        action.setDescription("Doação de cestas básicas");
        action.setLatitude(-23.564032);
        action.setLongitude(-46.752337);
        action.setOng(OngMocks.getOng());
        action.setActionVoluntaries(List.of(ActionVoluntaryMocks.getActionVoluntaryNoAction()));


        return action;
    }

    public static Action getAction2() {
        Action action = new Action();
        action.setId(2);
        action.setName("R. Campo Grande, 62");
        action.setDatetimeStart(LocalDateTime.of(2024, 3, 7, 8, 10, 0));
        action.setDatetimeEnd(LocalDateTime.of(2024, 3, 7, 15, 45, 0));
        action.setDescription("Doação de comida e itens de higiene pessoal");
        action.setLatitude(-23.531682);
        action.setLongitude(-46.721047);
        action.setOng(OngMocks.getOng());
        // action.setVoluntaries(List.of(VoluntaryMocks.getVoluntary()));

        return action;
    }

    public static List<Action> getActionList() {
        return List.of(getAction(), getAction2());
    }

    public static ActionRequestDto getActionRequestDto() {
        ActionRequestDto action = new ActionRequestDto();

        action.setName("R. Jacques Gabriel, 260");
        action.setDescription("Doação de cestas básicas");
        action.setDateTimeStart(LocalDateTime.of(2024, 5, 15, 10, 30, 0));
        action.setDateTimeEnd(LocalDateTime.of(2024, 5, 15, 17, 0, 0));
        action.setLatitude(-23.564032);
        action.setLongitude(-46.752337);

        return action;
    }
}
