package lk.menara.smartcity360.dto;

import java.sql.Timestamp;

public class UserDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private int roleId;
    private Timestamp joinedAt;
    private Integer buildingId;
    private String status;
    private int mustChangePassword;
    private Integer assignedZoneId;

    public UserDTO() {}

    public UserDTO(int id, String firstName, String lastName, String email, String passwordHash,
                int roleId, Timestamp joinedAt, Integer buildingId, String status,
                int mustChangePassword, Integer assignedZoneId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
        this.joinedAt = joinedAt;
        this.buildingId = buildingId;
        this.status = status;
        this.mustChangePassword = mustChangePassword;
        this.assignedZoneId = assignedZoneId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public Timestamp getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Timestamp joinedAt) { this.joinedAt = joinedAt; }

    public Integer getBuildingId() { return buildingId; }
    public void setBuildingId(Integer buildingId) { this.buildingId = buildingId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getMustChangePassword() { return mustChangePassword; }
    public void setMustChangePassword(int mustChangePassword) { this.mustChangePassword = mustChangePassword; }

    public Integer getAssignedZoneId() { return assignedZoneId; }
    public void setAssignedZoneId(Integer assignedZoneId) { this.assignedZoneId = assignedZoneId; }
}