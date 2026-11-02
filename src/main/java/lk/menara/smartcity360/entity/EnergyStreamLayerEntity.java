package lk.menara.smartcity360.entity;


import javax.persistence.*;

@Entity
@Table(name = "energy_stream_layer")
public class EnergyStreamLayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "security_layer_id", nullable = false)
    private int securityLayerId;

    @Column(name = "layer_order", nullable = false)
    private byte layerOrder;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public int getSecurityLayerId() { return securityLayerId; }
    public void setSecurityLayerId(int securityLayerId) { this.securityLayerId = securityLayerId; }
    public byte getLayerOrder() { return layerOrder; }
    public void setLayerOrder(byte layerOrder) { this.layerOrder = layerOrder; }
}
