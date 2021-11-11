package pwit.organizer.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwit.organizer.model.Task;
import pwit.organizer.model.TaskRepository;

import java.util.List;

@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer>
{

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    @Override
    List<Task> findAllByGroup_Id(Integer groupId);

    void deleteAll();
}
