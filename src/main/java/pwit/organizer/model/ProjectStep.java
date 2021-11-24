package pwit.organizer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name ="projects_steps")
public class ProjectStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Project's step description must not be empty")
    private String description;

    private Integer daysToDeadline;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
