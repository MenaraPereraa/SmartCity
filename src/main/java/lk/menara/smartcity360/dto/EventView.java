package lk.menara.smartcity360.dto;

/**
 * View model for a single complaint timeline event.
 */
public class EventView {

    private String title;
    private String badge;
    private String actor;
    private String whenText;
    private String remarks;

    public EventView() {}

    public EventView(String title, String badge, String actor, String whenText, String remarks) {
        this.title = title;
        this.badge = badge;
        this.actor = actor;
        this.whenText = whenText;
        this.remarks = remarks;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }

    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }

    public String getWhenText() { return whenText; }
    public void setWhenText(String whenText) { this.whenText = whenText; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    /**
     * Derives the Bootstrap badge colour from the outcome string.
     */
    public static String badgeForOutcome(String outcome) {
        if (outcome == null) return "secondary";
        switch (outcome.toUpperCase()) {
            case "SUBMITTED":  return "primary";
            case "ESCALATED":  return "warning";
            case "RESOLVED":   return "success";
            case "UNRESOLVED": return "danger";
            case "HANDLED":    return "info";
            default:           return "secondary";
        }
    }
}
