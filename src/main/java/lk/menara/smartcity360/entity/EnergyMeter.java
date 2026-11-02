package lk.menara.smartcity360.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "energy_meter")
public class EnergyMeter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "meter_code", length = 30, nullable = false, unique = true)
    private String meterCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "installed_at", nullable = false)
    private Date installedAt;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "building_id", nullable = false)
    private int buildingId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMeterCode() { return meterCode; }
    public void setMeterCode(String meterCode) { this.meterCode = meterCode; }
    public Date getInstalledAt() { return installedAt; }
    public void setInstalledAt(Date installedAt) { this.installedAt = installedAt; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public int getBuildingId() { return buildingId; }
    public void setBuildingId(int buildingId) { this.buildingId = buildingId; }
}
