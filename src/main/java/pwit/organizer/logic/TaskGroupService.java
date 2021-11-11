package pwit.organizer.logic;

import org.springframework.stereotype.Service;
import pwit.organizer.model.TaskGroup;
import pwit.organizer.model.TaskGroupRepository;
import pwit.organizer.model.TaskRepository;
import pwit.organizer.model.projection.GroupReadModel;
import pwit.organizer.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serwis jest to warstwa pośrednia między controlerem a repozytorium
 */

//@Service
public class TaskGroupService {

    private TaskGroupRepository taskGroupRepository;
    private TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.taskGroupRepository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source){
        TaskGroup result = taskGroupRepository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return taskGroupRepository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first");
        }
        TaskGroup result = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        taskGroupRepository.save(result);
    }
}
