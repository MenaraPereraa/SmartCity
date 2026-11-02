package lk.menara.smartcity360.dto;

/**
 * View model for a policy rule.
 */
public class PolicyView {

    private int id;
    private String name;
    private String expression;
    private String violationMessage;
    private boolean active;
    private String zoneName;
    private String createdAt;

    public PolicyView() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }

    public String getViolationMessage() { return violationMessage; }
    public void setViolationMessage(String violationMessage) { this.violationMessage = violationMessage; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
