package lk.menara.smartcity360.entity;


import javax.persistence.*;

@Entity
@Table(name = "building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "is_powered_on", nullable = false)
    private boolean poweredOn = true;

    @Column(name = "zone_id", nullable = false)
    private int zoneId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isPoweredOn() { return poweredOn; }
    public void setPoweredOn(boolean poweredOn) { this.poweredOn = poweredOn; }
    public int getZoneId() { return zoneId; }
    public void setZoneId(int zoneId) { this.zoneId = zoneId; }
}
