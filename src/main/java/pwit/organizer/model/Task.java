package pwit.organizer.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task extends TaskParent {

    private LocalDateTime deadLine;

    public Task(Integer id, String description) {
        super(id, description);
    }

    public Task(Integer id, String description, LocalDateTime deadLine) {
        super(id, description);
        this.deadLine = deadLine;
    }

    public Task(@NotBlank(message = "couldn't be blank or null") String description,LocalDateTime deadLine, TaskGroup group) {
        super(description);
        this.deadLine = deadLine;
        if(group != null) {
            this.group = group;
        }
    }

    @ManyToOne()
    @JoinColumn(name = "tasks_group_id")
    @Getter(AccessLevel.NONE)
    private TaskGroup group;

    public Task(String description, LocalDateTime deadLine) {
        setDescription(description);
        setDeadLine(deadLine);
    }

    public Task(String description) {
        setDescription(description);
    }

    public void updateFrom(final Task source) {
        setDescription(source.getDescription());
        setDone(source.isDone());
        setDeadLine(source.getDeadLine());
    }
}
