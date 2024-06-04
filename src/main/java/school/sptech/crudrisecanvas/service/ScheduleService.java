package school.sptech.crudrisecanvas.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import school.sptech.crudrisecanvas.utils.EmailConfig;
import school.sptech.crudrisecanvas.utils.Queue;
import school.sptech.crudrisecanvas.utils.adpters.MailValue;

@Component
public class ScheduleService {
    private static final Queue<MailValue> queue = new Queue<MailValue>(20);

    private final EmailConfig emailConfig = new EmailConfig();

    @Scheduled(cron = "0 * * * * *")
    public void AsyncMailSender() {
        if(queue.isEmpty()) return;

        int runTimes = (int) Math.ceil(queue.getTamanho() / 3.0);

        for(int i = 0; i < runTimes; i++) {
            MailValue mail = queue.poll();
            emailConfig.sendEmail(mail.getEmail(), mail.getSubject(), mail.getBody());
        }
    }
    
    public static void add(MailValue mailValue) {
        queue.insert(mailValue);
    }
}
