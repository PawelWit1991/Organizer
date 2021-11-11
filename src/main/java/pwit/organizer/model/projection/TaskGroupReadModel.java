package pwit.organizer.model.projection;

import pwit.organizer.model.Task;

public class TaskGroupReadModel {

    private String description;
    private boolean done;

    public TaskGroupReadModel(Task source) {
        description=source.getDescription();
        done=source.isDone();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
