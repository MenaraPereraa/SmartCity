package lk.menara.smartcity360.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    @Column(name = "email", length = 150, unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    // Role ටේබල් එකත් සමඟ ඇති සම්බන්ධතාවය (Many Users can have one Role)
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "joined_at", insertable = false, updatable = false)
    private Date joinedAt;

    @Column(name = "building_id")
    private Integer buildingId;

    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

    @Column(name = "must_change_password")
    private int mustChangePassword;

    @Column(name = "assigned_zone_id")
    private Integer assignedZoneId;

    // Default Constructor
    public AppUser() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(int mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public Integer getAssignedZoneId() {
        return assignedZoneId;
    }

    public void setAssignedZoneId(Integer assignedZoneId) {
        this.assignedZoneId = assignedZoneId;
    }
}