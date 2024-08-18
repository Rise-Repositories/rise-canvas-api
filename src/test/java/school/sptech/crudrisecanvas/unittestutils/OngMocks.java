package school.sptech.crudrisecanvas.unittestutils;

import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.utils.Enums.OngStatus;

import java.util.ArrayList;
import java.util.List;

public class OngMocks {

    public static Ong getOng() {
        Ong ong = new Ong();
        ong.setId(1);
        ong.setName("Instituto A Corrente do Bem");
        ong.setCnpj("44.454.154/0001-29");
        ong.setAddress(AddressMocks.getAddress());
        ong.setStatus(OngStatus.PENDING);
        return ong;
    }

    public static Ong getOng2() {
        Ong ong = new Ong();
        ong.setId(2);
        ong.setName("Teto");
        ong.setCnpj("76852512000148");
        ong.setAddress(AddressMocks.getAddress2());
        ong.setStatus(OngStatus.PENDING);

        return ong;
    }


    public static List<Ong> getOngList() {
        List<Ong> ongList = new ArrayList<>();

        Ong ong1 = new Ong();
        ong1.setId(1);
        ong1.setName("Instituto A Corrente do Bem");
        ong1.setCnpj("44.454.154/0001-29");
        ong1.setAddress(AddressMocks.getAddress());
        ong1.setStatus(OngStatus.PENDING);

        Ong ong2 = new Ong();
        ong2.setId(2);
        ong2.setName("Teto");
        ong2.setCnpj("76852512000148");
        ong2.setAddress(AddressMocks.getAddress2());
        ong2.setStatus(OngStatus.PENDING);

        ongList.add(ong1);
        ongList.add(ong2);
        return ongList;
    }
}
