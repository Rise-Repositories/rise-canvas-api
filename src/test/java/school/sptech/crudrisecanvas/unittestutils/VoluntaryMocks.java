package school.sptech.crudrisecanvas.unittestutils;

import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryRequestDto;
import school.sptech.crudrisecanvas.dtos.user.UserRequestDto;
import school.sptech.crudrisecanvas.entities.Voluntary;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

import java.util.List;

public class VoluntaryMocks {

    public static Voluntary getVoluntary() {
        Voluntary voluntary = new Voluntary(UserMocks.getUser(), OngMocks.getOng(), VoluntaryRoles.OWNER);
        voluntary.setId(1);
        return voluntary;
    }

    public static Voluntary getVoluntary2() {
        Voluntary voluntary = new Voluntary(UserMocks.getUser2(), OngMocks.getOng(), VoluntaryRoles.VOLUNTARY);
        voluntary.setId(2);
        return voluntary;
    }

    public static Voluntary getVoluntary3() {
        Voluntary voluntary = new Voluntary(UserMocks.getUser3(), OngMocks.getOng2(), VoluntaryRoles.OWNER);
        voluntary.setId(3);
        return voluntary;
    }

    public static List<Voluntary> getVoluntaryList() {
        return List.of(getVoluntary(), getVoluntary2());
    }

    public static VoluntaryRequestDto getVoluntaryRequestDto() {
        VoluntaryRequestDto voluntaryReqDto = new VoluntaryRequestDto();
        voluntaryReqDto.setRole(VoluntaryRoles.VOLUNTARY);
        voluntaryReqDto.setUser(UserMocks.getUserRequestDto());
        return voluntaryReqDto;
    }
}
