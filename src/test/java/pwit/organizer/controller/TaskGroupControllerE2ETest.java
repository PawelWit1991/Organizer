package pwit.organizer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import pwit.organizer.model.Task;
import pwit.organizer.model.TaskGroup;
import pwit.organizer.model.TaskGroupRepository;
import pwit.organizer.model.TaskRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskGroupControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TaskGroupRepository repo;

    @Test
    void httpGet_returnsAllTasks() {

        //given
        int counter = repo.findAll().size();
        repo.save(new TaskGroup("foo"));

        //when
        TaskGroup[] result = restTemplate.getForObject("http://localhost:" + port + "/tasksGroups", TaskGroup[].class);

        assertThat(result).hasSize(counter + 1);
    }
}