package pwit.organizer.model;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable
 class BaseAuditableEntityEmbeddable {

    LocalDateTime createdOn;
    LocalDateTime updatedOn;

    @PrePersist
    void prePersist(){
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate(){
        updatedOn = LocalDateTime.now();
    }
}
