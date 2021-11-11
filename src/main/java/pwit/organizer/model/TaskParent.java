package pwit.organizer.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskParent {

    public TaskParent(String description) {
        this.description = description;
    }

    public TaskParent(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "couldn't be blank or null")
    private String description;
    private boolean done;
    @Embedded
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    BaseAuditableEntityEmbeddable baseAuditableEntityEmbeddable = new BaseAuditableEntityEmbeddable();
}
