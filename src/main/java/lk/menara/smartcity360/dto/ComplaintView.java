package lk.menara.smartcity360.dto;

import java.util.Date;
import java.util.Locale;
import lk.menara.smartcity360.model.enums.ComplaintStatus;
import lk.menara.smartcity360.model.enums.HandlerLevel;
import lk.menara.smartcity360.util.CustomFormatter;

public class ComplaintView {
    private static final String BADGE_SUCCESS = "success";
    private static final String BADGE_WARNING = "warning";
    private static final String BADGE_DANGER = "danger";
    private static final String BADGE_PRIMARY = "primary";
    private static final String BADGE_SECONDARY = "secondary";
    private static final String CLOSED_LEVEL = "Closed";

    private int id;
    private String categoryName;
    private String description;
    private String zoneName;
    private String reporterName;
    private int severity;
    private String status;
    private Integer currentRoleId;
    private String currentRoleName;
    private Date createdAt;
    private Date resolvedAt;

    public ComplaintView() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCurrentRoleId() {
        return currentRoleId;
    }

    public void setCurrentRoleId(Integer currentRoleId) {
        this.currentRoleId = currentRoleId;
    }

    public String getCurrentRoleName() {
        return currentRoleName;
    }

    public void setCurrentRoleName(String currentRoleName) {
        this.currentRoleName = currentRoleName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Date resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getSeverityLabel() {
        if (HandlerLevel.LOCAL_STAFF.canHandle(severity)) {
            return "Minor";
        }
        if (HandlerLevel.SUPERVISOR.canHandle(severity)) {
            return "Serious";
        }
        return "Critical";
    }

    public String getSeverityBadge() {
        if (HandlerLevel.LOCAL_STAFF.canHandle(severity)) {
            return BADGE_SUCCESS;
        }
        if (HandlerLevel.SUPERVISOR.canHandle(severity)) {
            return BADGE_WARNING;
        }
        return BADGE_DANGER;
    }

    public String getStatusLabel() {
        return status == null ? "" : prettify(status);
    }

    public String getStatusBadge() {
        if (status == null) {
            return BADGE_SECONDARY;
        }
        try {
            switch (ComplaintStatus.valueOf(status.toUpperCase(Locale.ROOT))) {
                case OPEN:
                    return BADGE_PRIMARY;
                case ESCALATED:
                    return BADGE_WARNING;
                case RESOLVED:
                    return BADGE_SUCCESS;
                case UNRESOLVED:
                    return BADGE_DANGER;
                default:
                    return BADGE_SECONDARY;
            }
        } catch (IllegalArgumentException e) {
            return BADGE_SECONDARY;
        }
    }

    public String getCurrentLevel() {
        return currentRoleName == null ? CLOSED_LEVEL : prettify(currentRoleName);
    }

    public String getCreatedAtText() {
        return createdAt == null ? "" : CustomFormatter.formatDateTime(createdAt);
    }

    public String getResolvedAtText() {
        return resolvedAt == null ? "" : CustomFormatter.formatDateTime(resolvedAt);
    }

    private static String prettify(String value) {
        StringBuilder text = new StringBuilder();
        for (String word : value.toLowerCase(Locale.ROOT).split("_")) {
            if (word.isEmpty()) {
                continue;
            }
            if (text.length() > 0) {
                text.append(' ');
            }
            text.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return text.toString();
    }
}