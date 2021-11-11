package pwit.organizer.logic;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pwit.organizer.model.TaskGroup;
import pwit.organizer.model.TaskGroupRepository;
import pwit.organizer.model.TaskRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw when undone task")
    void toggleGroup_undoneTasks_throwsIllegalStateException() {
        //given
        TaskRepository taskRepository = taskRepositoryReturning(true);
        when(taskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);
        //and
        TaskGroupRepository taskGroupRepository = mock(TaskGroupRepository.class);
        //system under test
        var toTest = new TaskGroupService(null, taskRepository);
        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone");
    }

    @Test
    @DisplayName("should throw when id not found")
    void toggleGroup_wrongId_throwsIllegalArgumentException() {
        //given
        TaskRepository taskRepository = taskRepositoryReturning(false);
        //given
        var taskGroupRepository = mock(TaskGroupRepository.class);
        when(taskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new TaskGroupService(taskGroupRepository, taskRepository);
        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should toggle group")
    void toggleGroup_toggleGroup() {

        //given
        TaskRepository taskRepository = taskRepositoryReturning(false);
        //and
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        //and
        var taskGroupRepository = mock(TaskGroupRepository.class);
        when(taskGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));
        //system under test
        var toTest = new TaskGroupService(taskGroupRepository, taskRepository);
        //when
        toTest.toggleGroup(10);
        //then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);
    }

    private TaskRepository taskRepositoryReturning(boolean result){
        TaskRepository taskRepository = mock(TaskRepository.class);
        when(taskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return taskRepository;
    }
}