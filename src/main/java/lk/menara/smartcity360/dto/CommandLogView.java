package lk.menara.smartcity360.dto;

/**
 * View model for a command_log entry.
 */
public class CommandLogView {

    private int id;
    private String command;
    private String executedAt;
    private String zoneName;
    private String buildingName;
    private String userName;

    public CommandLogView() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCommand() { return command; }
    public void setCommand(String command) { this.command = command; }

    public String getExecutedAt() { return executedAt; }
    public void setExecutedAt(String executedAt) { this.executedAt = executedAt; }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
