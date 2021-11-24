package pwit.organizer.model.projection;

import org.springframework.format.annotation.DateTimeFormat;
import pwit.organizer.model.Task;
import pwit.organizer.model.TaskGroup;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class TaskGroupWriteModel {

    @NotBlank(message = "Task's description must not be empty")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadLine;

    public TaskGroupWriteModel() {
    }

    public TaskGroupWriteModel(String description, LocalDateTime deadLine) {
        this.description = description;
        this.deadLine = deadLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadLine) {
        this.deadLine = deadLine;
    }

    public Task toTask(TaskGroup group){
        return new Task(description, deadLine, group);
    }
}
