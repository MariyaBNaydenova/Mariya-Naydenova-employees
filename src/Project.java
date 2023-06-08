import java.time.LocalDateTime;

public class Project {

    private int projectId;
    private LocalDateTime from;
    private LocalDateTime to;

    public Project() {
    }

    public Project(int projectId, LocalDateTime from, LocalDateTime to) {
        this.projectId = projectId;
        this.from = from;
        this.to = to;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }
}
