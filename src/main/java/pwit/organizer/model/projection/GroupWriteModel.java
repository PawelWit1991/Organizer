package pwit.organizer.model.projection;

import pwit.organizer.model.Project;
import pwit.organizer.model.TaskGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {

    public GroupWriteModel() {
        tasks.add(new TaskGroupWriteModel());
    }

    @NotBlank(message = "Task group description must not be empty")
    private String description;
    @Valid
    private List<TaskGroupWriteModel> tasks = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TaskGroupWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskGroupWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup(Project project) {
        var result = new TaskGroup();
        result.setDescription(description);
        result.setTasks(
                tasks.stream()
                       .map(source -> source.toTask(result))
                       .collect(Collectors.toSet()));
        result.setProject(project);
        return result;
    }
}
