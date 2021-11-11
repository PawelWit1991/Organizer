package pwit.organizer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pwit.organizer.model.Task;
import pwit.organizer.model.TaskRepository;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class TaskGroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repo;
    @Test
    void httpHet_returnsGivenTask(){
        //given
        Task task = repo.save(new Task("foo", LocalDateTime.now()));


        try {
            mockMvc.perform(post("/tasks/", new Task("foo", LocalDateTime.now())))
                    .andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
