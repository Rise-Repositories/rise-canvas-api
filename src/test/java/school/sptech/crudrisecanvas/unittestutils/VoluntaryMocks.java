package school.sptech.crudrisecanvas.unittestutils;

import school.sptech.crudrisecanvas.entities.Voluntary;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

import java.util.List;

public class VoluntaryMocks {

    public static Voluntary getVoluntary() {
        return new Voluntary(UserMocks.getUser(), OngMocks.getOng(), VoluntaryRoles.OWNER);
    }

    public static Voluntary getVoluntary2() {
        return new Voluntary(UserMocks.getUser2(), OngMocks.getOng(), VoluntaryRoles.VOLUNTARY);
    }

    public static Voluntary getVoluntary3() {
        return new Voluntary(UserMocks.getUser3(), OngMocks.getOng2(), VoluntaryRoles.OWNER);
    }

    public static List<Voluntary> getVoluntaryList() {
        return List.of(getVoluntary(), getVoluntary2());
    }
}
