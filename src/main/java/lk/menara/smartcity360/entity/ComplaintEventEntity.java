package lk.menara.smartcity360.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "complaint_event")
public class ComplaintEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "outcome", length = 30, nullable = false)
    private String outcome;

    @Column(name = "remarks", length = 255)
    private String remarks;

    @Column(name = "occurred_at", insertable = false, updatable = false)
    private Timestamp occurredAt;

    @Column(name = "complaint_id", nullable = false)
    private int complaintId;

    @Column(name = "role_id", nullable = false)
    private int roleId;

    @Column(name = "app_user_id", nullable = false)
    private int appUserId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public Timestamp getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Timestamp occurredAt) { this.occurredAt = occurredAt; }
    public int getComplaintId() { return complaintId; }
    public void setComplaintId(int complaintId) { this.complaintId = complaintId; }
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
    public int getAppUserId() { return appUserId; }
    public void setAppUserId(int appUserId) { this.appUserId = appUserId; }
}
