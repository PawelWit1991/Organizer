package pwit.organizer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pwit.organizer.logic.TaskGroupService;
import pwit.organizer.model.Task;
import pwit.organizer.model.TaskGroupRepository;
import pwit.organizer.model.TaskRepository;
import pwit.organizer.model.projection.GroupReadModel;
import pwit.organizer.model.projection.GroupWriteModel;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class TaskGroup2Controller {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupRepository.class);
    private final TaskRepository repository;
    private TaskGroupService service;

    public TaskGroup2Controller(TaskRepository repository, TaskGroupService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        GroupReadModel result = service.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(service.createGroup(toCreate));
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.info("Exposing all the tasks");
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id) {
        service.toggleGroup(id);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<?> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
