package school.sptech.crudrisecanvas.unittestutils;

import school.sptech.crudrisecanvas.entities.Action;
import school.sptech.crudrisecanvas.entities.ActionVoluntary;

public class ActionVoluntaryMocks {

    public static ActionVoluntary getActionVoluntary() {
        ActionVoluntary actionVoluntary = new ActionVoluntary();

        actionVoluntary.setId(1);
        actionVoluntary.setAction(ActionMocks.getAction());
        actionVoluntary.setVoluntary(VoluntaryMocks.getVoluntary());

        return actionVoluntary;
    }

    public static ActionVoluntary getActionVoluntaryNoAction() {
        ActionVoluntary actionVoluntary = new ActionVoluntary();

        actionVoluntary.setId(1);
        actionVoluntary.setVoluntary(VoluntaryMocks.getVoluntary());

        return actionVoluntary;
    }

    public static ActionVoluntary getActionVoluntaryNoVoluntary() {
        ActionVoluntary actionVoluntary = new ActionVoluntary();

        actionVoluntary.setId(1);
        actionVoluntary.setAction(ActionMocks.getAction());

        return actionVoluntary;
    }
}
