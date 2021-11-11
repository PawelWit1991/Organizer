package pwit.organizer.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {

    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Integer id);

    TaskGroup save(TaskGroup entity);

    boolean existsByDoneIsFalseAndProject_Id(Integer id);

//    boolean existsById(Integer id);

//    Page<Task> findAll(Pageable page);

//    List<Task> findByDone(@Param("state") boolean done);

//    void deleteById(Integer id);


}
