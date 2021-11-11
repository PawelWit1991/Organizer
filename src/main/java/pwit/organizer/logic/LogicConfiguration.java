package pwit.organizer.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pwit.organizer.TaskConfigurationProperties;
import pwit.organizer.model.ProjectRepository;
import pwit.organizer.model.TaskGroupRepository;
import pwit.organizer.model.TaskRepository;

@Configuration
class LogicConfiguration {

    @Bean
    ProjectService projectService(ProjectRepository repository,
                           TaskGroupRepository taskGroupRepository,
                           TaskGroupService taskGroupService,
                           TaskConfigurationProperties conf) {
        return new ProjectService(repository, taskGroupRepository, taskGroupService, conf);
    }

    @Bean
    TaskGroupService taskGroupService(TaskGroupRepository taskGroupRepository,
                                      TaskRepository taskRepository){
        return new TaskGroupService(taskGroupRepository, taskRepository);
    }
}
