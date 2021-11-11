package pwit.organizer.model.projection;

import pwit.organizer.model.Task;
import pwit.organizer.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String description;
    /**
     * Deadline from the latest task in group
     */
    private LocalDateTime deadLine;
    private Set<TaskGroupReadModel> tasks;

    public GroupReadModel(TaskGroup source) {
        id = source.getId();
        description = source.getDescription();
        source.getTasks().stream()
                .map(Task::getDeadLine)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> deadLine = date);
        tasks = source.getTasks().stream()
                .map(TaskGroupReadModel::new)
                .collect(Collectors.toSet());
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

    public Set<TaskGroupReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskGroupReadModel> tasks) {
        this.tasks = tasks;
    }
}
