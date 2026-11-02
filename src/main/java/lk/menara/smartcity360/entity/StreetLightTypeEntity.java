package lk.menara.smartcity360.entity;


import javax.persistence.*;

@Entity
@Table(name = "street_light_type")
public class StreetLightTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "model", length = 50, nullable = false)
    private String model;

    @Column(name = "watts", nullable = false)
    private short watts;

    @Column(name = "brightness_lumens", nullable = false)
    private int brightnessLumens;

    @Column(name = "colour_temp_kelvin", nullable = false)
    private short colourTempKelvin;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public short getWatts() { return watts; }
    public void setWatts(short watts) { this.watts = watts; }
    public int getBrightnessLumens() { return brightnessLumens; }
    public void setBrightnessLumens(int brightnessLumens) { this.brightnessLumens = brightnessLumens; }
    public short getColourTempKelvin() { return colourTempKelvin; }
    public void setColourTempKelvin(short colourTempKelvin) { this.colourTempKelvin = colourTempKelvin; }
}
