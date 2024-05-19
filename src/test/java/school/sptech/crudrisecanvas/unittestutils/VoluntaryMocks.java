package school.sptech.crudrisecanvas.unittestutils;

import school.sptech.crudrisecanvas.entities.Voluntary;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

public class VoluntaryMocks {

    public static Voluntary getVoluntary() {
        return new Voluntary(UserMocks.getUser(), OngMocks.getOng(), VoluntaryRoles.OWNER);
    }
}
