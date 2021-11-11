package pwit.organizer.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks_groups")
public class  TaskGroup extends TaskParent {

    public TaskGroup(String description) {
        super(description);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
