package pwit.organizer.model.projection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pwit.organizer.model.Task;
import pwit.organizer.model.TaskGroup;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {

    @Test
    @DisplayName("should crete null deadline for group when no task deadline")
    void constructor_noDeadlines_createsNullDeadline(){
        //given
        var source = new TaskGroup();
        source.setId(100);
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar",null)));

        //when
        var result = new GroupReadModel(source);

        //then
        assertThat(result).hasFieldOrPropertyWithValue("deadline",null);
    }
}