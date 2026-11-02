package lk.menara.smartcity360.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "meter_reading")
public class MeterReadingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "kwh", nullable = false, precision = 10, scale = 3)
    private BigDecimal kwh;

    @Column(name = "voltage", nullable = false, precision = 6, scale = 2)
    private BigDecimal voltage;

    @Column(name = "read_at", insertable = false, updatable = false)
    private Timestamp readAt;

    @Column(name = "energy_meter_id", nullable = false)
    private int energyMeterId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public BigDecimal getKwh() { return kwh; }
    public void setKwh(BigDecimal kwh) { this.kwh = kwh; }
    public BigDecimal getVoltage() { return voltage; }
    public void setVoltage(BigDecimal voltage) { this.voltage = voltage; }
    public Timestamp getReadAt() { return readAt; }
    public void setReadAt(Timestamp readAt) { this.readAt = readAt; }
    public int getEnergyMeterId() { return energyMeterId; }
    public void setEnergyMeterId(int energyMeterId) { this.energyMeterId = energyMeterId; }
}
