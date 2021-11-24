package pwit.organizer.model.projection;

import pwit.organizer.model.Task;
import pwit.organizer.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    private LocalDateTime deadline;
    private List<TaskGroupReadModel> tasks;

    public GroupReadModel(TaskGroup source) {
        id = source.getId();
        description = source.getDescription();
        source.getTasks().stream()
                .map(Task::getDeadLine)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> deadline = date);
        tasks = source.getTasks().stream()
                .map(TaskGroupReadModel::new)
                .collect(Collectors.toList());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public List<TaskGroupReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskGroupReadModel> tasks) {
        this.tasks = tasks;
    }
}
