package pwit.organizer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pwit.organizer.adapter.SqlTaskRepository;
import pwit.organizer.logic.TaskService;
import pwit.organizer.model.Task;
import pwit.organizer.model.TaskRepository;
import pwit.organizer.model.projection.TaskGroupReadModel;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(SqlTaskRepository.class);
    private final TaskRepository repository;
    private TaskService service;

    public TaskController(TaskRepository repository, TaskService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTask() {
        logger.info("Exposing all the tasks");
        return service.findAllAsync().thenApply(ResponseEntity::ok);
    }

//    @GetMapping
//    ResponseEntity<List<Task>> readAllTask(Pageable pageable) {
//        logger.info("Custom pageable");
//        return ResponseEntity.ok(repository.findAll(pageable).getContent());
//    }

    @GetMapping
    ResponseEntity<Task> getById(@PathVariable int id) {
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/done")
    ResponseEntity<?> readDoneTasks(@RequestParam(defaultValue = "true") boolean state){
        return ResponseEntity.ok(repository.findByDone(state));
    }

    @PostMapping
    ResponseEntity<Task> saveTask(@RequestBody @Valid Task task) {
        Task result = repository.save(task);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @Transactional
    @PatchMapping("/toggle/{id}")
   public ResponseEntity<?> toggleTask(@PathVariable int id){
        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody Task toUpdate){
        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        repository.findById(id)
                .ifPresent(task -> task.updateFrom(toUpdate));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable int id){
        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAllTasks(){
        repository.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    ResponseEntity<?> test(){
        return ResponseEntity.ok(repository.findAll().stream()
                .sorted((a,b) -> (b.getId() - (a.getId())))
                        .skip(2)
                .collect(Collectors.toList()));
    }
}
