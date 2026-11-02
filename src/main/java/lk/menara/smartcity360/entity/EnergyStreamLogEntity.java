package lk.menara.smartcity360.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "energy_stream_log")
public class EnergyStreamLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "energy_stream_layer_id", nullable = false)
    private long energyStreamLayerId;

    @Column(name = "energy_meter_id", nullable = false)
    private int energyMeterId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public long getEnergyStreamLayerId() { return energyStreamLayerId; }
    public void setEnergyStreamLayerId(long energyStreamLayerId) { this.energyStreamLayerId = energyStreamLayerId; }
    public int getEnergyMeterId() { return energyMeterId; }
    public void setEnergyMeterId(int energyMeterId) { this.energyMeterId = energyMeterId; }
}
