package lk.menara.smartcity360.patterns.chain;

public class Complaint {
    private String description;
    private int severityLevel; // 1: Low, 2: Medium, 3: High

    public Complaint(String description, int severityLevel) {
        this.description = description;
        this.severityLevel = severityLevel;
    }

    public String getDescription() {
        return description;
    }

    // මෙම getSeverityLevel() මෙතඩ් එක Complaint පන්තියට එකතු කරන්න
    public int getSeverityLevel() {
        return severityLevel;
    }
}