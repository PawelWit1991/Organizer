package pwit.organizer.logic;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pwit.organizer.TaskConfigurationProperties;
import pwit.organizer.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("Should throw illegalStateException when configured to allow just 1 group and te other undone group")
    void createGroup() {
        //given
        TaskGroupRepository mockGroupRepository = taskGroupRepositoryReturning(true);
        //and
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasksFromTemplate()).thenReturn(false);
        //and
        var mockTaskConf = mock(TaskConfigurationProperties.class);
        when(mockTaskConf.getTemplate()).thenReturn(mockTemplate);
        //system under test
        var toTest = new ProjectService(null, mockGroupRepository,null, mockTaskConf);
        /**
         *                 AssertJ

         1 sposób
         try {
         toTest.createGroup(0, LocalDateTime.now());
         }
         catch (IllegalStateException e){
         assertThat(e).hasMessageContaining("undone");
         }

         2 sposób
         assertThatThrownBy(() -> {
         toTest.createGroup(0, LocalDateTime.now());
         }).isInstanceOf(IllegalStateException.class);


         3 sposób
         assertThatExceptionOfType(IllegalStateException.class)
         .isThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()));


         4 sposób dedykowany dla konkretnego wyjątku

         assertThatIllegalStateException()
         .isThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()));

         */
        //when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone");
    }

    @Test
    void createGroup_configurationOk_And_noProject_throwsIllegalArgumentException() {
        //given
        ProjectRepository mockRepository = projectRepositoryReturning();

        TaskConfigurationProperties mockTaskConf = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository, null,null, mockTaskConf);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("Should throw illegalStateException when configured to allow just 1 group and no grups and projects for a")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
        //given
        TaskGroupRepository mockGroupRepository = taskGroupRepositoryReturning(false);
        //and
        ProjectRepository mockProjectRepository = projectRepositoryReturning();
        //and
        TaskConfigurationProperties mockTaskConf = configurationReturning(false);
        //system under test
        var toTest = new ProjectService(mockProjectRepository, mockGroupRepository,null, mockTaskConf);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @Disabled
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        //and
        var project = projectWith("bar", Set.of(-1,-2));
        //and
        ProjectRepository projectRepository = mock(ProjectRepository.class);
        when(projectRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        //and
        TaskConfigurationProperties conf = configurationReturning(true);
        //and
        InMemoryGroupRepository inMemoryTaskGroupRepository = inMemoryGroupRepository();
        var serviceWithInMemoryRepo = dummyGroupService(inMemoryTaskGroupRepository);
        int countBeforeCall = inMemoryTaskGroupRepository.count();
        //system under test
        var toTest = new ProjectService(projectRepository,inMemoryTaskGroupRepository,serviceWithInMemoryRepo, conf);

//        //when
//        GroupReadModel result = toTest.createGroup(1,today);
//
//        //then
//        assertThat(result.getDescription()).isEqualTo("bar");
        /** lub
        assertThat(result)
                .hasFieldOrPropertyWithValue("description", "bar");
         */
//        assertThat(result.getDeadLine()).isEqualTo(today.minusDays(1));
//        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1)
                .isEqualTo(inMemoryGroupRepository().count());
    }

    private TaskGroupService dummyGroupService(InMemoryGroupRepository inMemoryTaskGroupRepository) {
        return new TaskGroupService(inMemoryTaskGroupRepository, null);
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {

        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
       return new InMemoryGroupRepository();
    }

    private ProjectRepository projectRepositoryReturning() {
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        return mockRepository;
    }

    private TaskGroupRepository taskGroupRepositoryReturning(boolean t) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(t);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(final boolean result) {
        //given
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasksFromTemplate()).thenReturn(result);
        //and
        var mockTaskConf = mock(TaskConfigurationProperties.class);
        when(mockTaskConf.getTemplate()).thenReturn(mockTemplate);
        return mockTaskConf;
    }

private static class InMemoryGroupRepository implements TaskGroupRepository{

    private int index = 0;
    private Map<Integer, TaskGroup> map = new HashMap<>();

    public int count(){
        return map.values().size();
    }

    @Override
    public List<TaskGroup> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Optional<TaskGroup> findById(Integer id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public TaskGroup save(final TaskGroup entity) {
        if (entity.getId() == 0){
            try {
               var field =  TaskGroup.class.getDeclaredField("id");
               field.setAccessible(true);
               field.set(entity, ++index);
            }
            catch (NoSuchFieldException | IllegalAccessException e){
                throw new RuntimeException(e);
            }
        }
        map.put(entity.getId(),entity);
        return entity;
    }

    @Override
    public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
        return map.values().stream()
                .filter(group -> !group.isDone())
                .anyMatch(group -> group != null && group.getProject().equals(projectId));
    }
}
}

