package lk.menara.smartcity360.entity;


import javax.persistence.*;

@Entity
@Table(name = "street_light")
public class StreetLightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "is_on", nullable = false)
    private boolean isOn = false;

    @Column(name = "zone_id", nullable = false)
    private int zoneId;

    @Column(name = "street_light_type_id", nullable = false)
    private int streetLightTypeId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public boolean isOn() { return isOn; }
    public void setOn(boolean on) { isOn = on; }
    public int getZoneId() { return zoneId; }
    public void setZoneId(int zoneId) { this.zoneId = zoneId; }
    public int getStreetLightTypeId() { return streetLightTypeId; }
    public void setStreetLightTypeId(int streetLightTypeId) { this.streetLightTypeId = streetLightTypeId; }
}
