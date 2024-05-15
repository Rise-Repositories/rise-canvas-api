package school.sptech.crudrisecanvas.utils.adpters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.sptech.crudrisecanvas.entities.Action;
import school.sptech.crudrisecanvas.entities.MappingAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleQueueAdpter {
    private String email;
    private Action action;
    private MappingAction mappingActionBody;
}
