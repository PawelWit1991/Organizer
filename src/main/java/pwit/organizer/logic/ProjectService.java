package pwit.organizer.logic;

import pwit.organizer.TaskConfigurationProperties;
import pwit.organizer.model.*;
import pwit.organizer.model.projection.GroupReadModel;
import pwit.organizer.model.projection.GroupWriteModel;
import pwit.organizer.model.projection.ProjectWriteModel;
import pwit.organizer.model.projection.TaskGroupWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private TaskGroupRepository taskGroupRepository;
    private TaskGroupService taskGroupService;
    private TaskConfigurationProperties conf;

    public ProjectService(ProjectRepository projectRepository, TaskGroupRepository taskGroupRepository, TaskGroupService taskGroupService, TaskConfigurationProperties conf) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskGroupService = taskGroupService;
        this.conf = conf;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(ProjectWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if ((!conf.getTemplate().isAllowMultipleTasksFromTemplate() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId))) {
            throw new IllegalStateException("Only one undone group from project id allowed");
        }
        GroupReadModel result = projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                       var task = new TaskGroupWriteModel();
                                                task.setDescription(project.getDescription());
                                                task.setDeadLine(deadline.plusDays(projectStep.getDaysToDeadline()));
                                                return task;
                                    }).collect(Collectors.toList())
                    );
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return result;
    }
}
