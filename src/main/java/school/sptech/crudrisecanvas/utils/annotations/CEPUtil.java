package school.sptech.crudrisecanvas.utils.annotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CEPUtil {

    private static String FRONT_IP;

    @Value("${front.private.ip}")
    public void setFrontIp(String frontIp) {
        FRONT_IP = frontIp;
    }

    public static String getFrontIp() {
        return FRONT_IP;
    }
}
