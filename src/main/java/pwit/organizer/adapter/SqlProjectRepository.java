package pwit.organizer.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pwit.organizer.model.Project;
import pwit.organizer.model.ProjectRepository;

import java.util.List;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query("from Project g join fetch g.steps")
    List<Project> findAll();
}
