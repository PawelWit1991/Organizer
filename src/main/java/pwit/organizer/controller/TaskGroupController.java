package pwit.organizer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pwit.organizer.logic.TaskGroupService;
import pwit.organizer.model.Task;
import pwit.organizer.model.TaskGroupRepository;
import pwit.organizer.model.TaskRepository;
import pwit.organizer.model.projection.GroupReadModel;
import pwit.organizer.model.projection.GroupWriteModel;
import pwit.organizer.model.projection.ProjectWriteModel;
import pwit.organizer.model.projection.TaskGroupWriteModel;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupRepository.class);
    private final TaskRepository repository;
    private TaskGroupService service;

    public TaskGroupController(TaskRepository repository, TaskGroupService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String showGroups(Model model) {
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        GroupReadModel result = service.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(service.createGroup(toCreate));
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.info("Exposing all the tasks");
        return ResponseEntity.ok(service.readAll());
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    @ResponseBody
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id) {
        service.toggleGroup(id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(@ModelAttribute("group") @Valid GroupWriteModel current, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "groups";
        }
        service.createGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Dodano Grupę !");
        return "groups";
    }


    @PostMapping(params = "addTask", produces = MediaType.TEXT_HTML_VALUE)
    String addGroupTask(@ModelAttribute("group") GroupWriteModel current) {
        current.getTasks().add(new TaskGroupWriteModel());
        return "groups";
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<?> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ModelAttribute("groups")
    List<GroupReadModel> getGroups() {
        return service.readAll();
    }
}