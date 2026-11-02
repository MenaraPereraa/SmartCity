package lk.menara.smartcity360.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "complaint")
public class ComplaintEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "category_id", nullable = false)
    private int categoryId;

    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @Column(name = "severity", nullable = false)
    private int severity;

    @Column(name = "status", length = 30, nullable = false)
    private String status = "OPEN";

    @Column(name = "current_role_id")
    private Integer currentRoleId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", insertable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "resolved_at")
    private Date resolvedAt;

    @Column(name = "zone_id", nullable = false)
    private int zoneId;

    @Column(name = "app_user_id", nullable = false)
    private int appUserId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getSeverity() { return severity; }
    public void setSeverity(int severity) { this.severity = severity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getCurrentRoleId() { return currentRoleId; }
    public void setCurrentRoleId(Integer currentRoleId) { this.currentRoleId = currentRoleId; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Date resolvedAt) { this.resolvedAt = resolvedAt; }
    public int getZoneId() { return zoneId; }
    public void setZoneId(int zoneId) { this.zoneId = zoneId; }
    public int getAppUserId() { return appUserId; }
    public void setAppUserId(int appUserId) { this.appUserId = appUserId; }
}
