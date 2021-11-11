//package pwit.organizer.model;
//
//import javax.persistence.MappedSuperclass;
//import javax.persistence.PrePersist;
//import javax.persistence.PreUpdate;
//import java.time.LocalDateTime;
//
////jezeli chcemy miec klase bazowa ktora nie ma swojego odzwierciedlenia w tabeli,
// trzeba po niej dziedziczyc
//@MappedSuperclass
//public abstract class BaseAuditableEntity {
//
//
//    LocalDateTime createdOn;
//    LocalDateTime updatedOn;
//
//    @PrePersist
//    void prePersist(){
//        createdOn = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    void preUpdate(){
//        updatedOn = LocalDateTime.now();
//    }
//}
