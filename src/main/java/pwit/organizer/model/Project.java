package pwit.organizer.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter()
@Setter
@Entity
@Table(name ="projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "couldn't be blank or null")
    private String description;

    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> groups;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> steps;

}
