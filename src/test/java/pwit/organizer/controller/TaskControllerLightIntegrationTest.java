package pwit.organizer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pwit.organizer.model.Task;
import pwit.organizer.model.TaskRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerLightIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository repo;

    @Test
    void httpGet_returnsGivenTask() throws Exception {
        // given
        String desc = "foo";
        when(repo.findById(anyInt())).thenReturn(Optional.of(new Task(desc, LocalDateTime.now())));

        // when + then
        mockMvc.perform(get("/tasks/12"))
                .andDo(print())
                .andExpect(content().string(containsString(desc)));
    }

    @Test
    void httpGet_returnsCreatedTask() throws Exception {
        // given
        Task task = new Task("foo");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        when(repo.save(any(Task.class))).thenReturn(task);

        // when + then
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(201))
                .andExpect(content().string(containsString("\"description\":\"foo\"")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void httpGet_returnsUpdatedTask() throws Exception {
        // given
        Task taskAfterUpdate = new Task("task after update");
        Gson gson = new Gson();
        String json = gson.toJson(taskAfterUpdate);
        when(repo.existsById(anyInt())).thenReturn(true);

        // when + then
        mockMvc.perform(patch("/tasks/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void httpGet_returnsOkWhenDeletedTask() throws Exception {
        // given
        when(repo.existsById(anyInt())).thenReturn(true);

        // when + then
        mockMvc.perform(delete("/tasks/delete/" + anyInt()))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }


}
