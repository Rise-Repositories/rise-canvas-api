package school.sptech.crudrisecanvas.service;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import school.sptech.crudrisecanvas.entities.Action;
import school.sptech.crudrisecanvas.entities.MappingAction;
import school.sptech.crudrisecanvas.utils.EmailConfig;
import school.sptech.crudrisecanvas.utils.adpters.ScheduleQueueAdpter;

@Component
public class ScheduleService {
    private static final Queue<ScheduleQueueAdpter> queue = new LinkedList<>();

    private final EmailConfig emailConfig = new EmailConfig();

    @Scheduled(cron = "0 * * * * *")
    public void scheduleTaskWithCronExpression() {
        if(queue.isEmpty()) return;

        ScheduleQueueAdpter data = queue.poll();
        emailConfig.sendEmail(
            data.getEmail(),
            "Rise Canvas - Seu pin foi atendido",
            "<h1>Olá, seu pin foi atendido!</h1><br> A ação " + data.getAction().getName() + " foi realizada e atendeu " + (data.getMappingActionBody().getQtyServedAdults() + data.getMappingActionBody().getQtyServedChildren()) + " pessoas.");
    }
    
    public static void add(String email, Action action, MappingAction mappingActionBody) {
        queue.add(new ScheduleQueueAdpter(email, action, mappingActionBody));
    }
}
