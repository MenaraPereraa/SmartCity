package lk.menara.smartcity360.dto;

public class UserDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private int roleId;
    private Integer assignedZoneId;

    public UserDTO() {}

    public UserDTO(int id, String firstName, String lastName, String email, int roleId, Integer assignedZoneId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roleId = roleId;
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

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public Integer getAssignedZoneId() { return assignedZoneId; }
    public void setAssignedZoneId(Integer assignedZoneId) { this.assignedZoneId = assignedZoneId; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getRoleDisplay() {
        switch (roleId) {
            case 1: return "Resident";
            case 2: return "Local Staff";
            case 3: return "Supervisor";
            case 4: return "City Manager";
            default: return "Unknown";
        }
    }

    public boolean isResident() {
        return roleId == 1;
    }

    public boolean isStaff() {
        return roleId != 1;
    }

    public boolean isCanAccessEnergy() {
        return roleId >= 2;
    }

    public boolean getCanAccessEnergy() {
        return roleId >= 2;
    }

    public boolean isCanAccessStreetLights() {
        return roleId >= 2;
    }

    public boolean getCanAccessStreetLights() {
        return roleId >= 2;
    }

    public boolean isCanAccessZoneCommand() {
        return roleId >= 3;
    }

    public boolean getCanAccessZoneCommand() {
        return roleId >= 3;
    }

    public boolean isCanAccessPolicy() {
        return roleId == 4;
    }

    public boolean getCanAccessPolicy() {
        return roleId == 4;
    }
}