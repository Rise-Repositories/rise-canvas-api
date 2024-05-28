package school.sptech.crudrisecanvas.utils.adpters;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailValue {
    private String email;
    private String subject;
    private String body;
}
