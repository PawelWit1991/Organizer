package pwit.organizer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pwit.organizer.model.Task;
import pwit.organizer.model.TaskParent;
import pwit.organizer.model.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repo;
    @Test
    void httpHet_returnsGivenTask(){
        //given
        Task task = repo.save(new Task("foo", LocalDateTime.now()));


        try {
            mockMvc.perform(get("/tasks/" + task.getId()))
                    .andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void httpHet_returnsCreatedTask(){
        //given
        Task task = repo.save(new Task("foo", LocalDateTime.now()));
        when(repo.save(new Task("foo", LocalDateTime.now()))).thenReturn(task);


        try {
            mockMvc.perform(post("/tasks/"))
                    .andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
