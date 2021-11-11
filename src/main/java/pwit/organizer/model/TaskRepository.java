package pwit.organizer.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();

    Optional<Task> findById(Integer id);

    boolean existsById(Integer id);

    Task save(Task entity);

    Page<Task> findAll(Pageable page);

    List<Task> findAllByGroup_Id(Integer groupId);

    List<Task> findByDone(@Param("state") boolean done);

    void deleteById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    void deleteAll();
}
