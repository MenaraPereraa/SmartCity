package lk.menara.smartcity360.dto;

/**
 * View model for a street light with its type information.
 */
public class StreetLightView {

    private int id;
    private double latitude;
    private double longitude;
    private boolean isOn;
    private String zoneName;
    private String model;
    private int watts;
    private int brightnessLumens;
    private int colourTempKelvin;

    public StreetLightView() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public boolean isOn() { return isOn; }
    public void setOn(boolean on) { isOn = on; }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getWatts() { return watts; }
    public void setWatts(int watts) { this.watts = watts; }

    public int getBrightnessLumens() { return brightnessLumens; }
    public void setBrightnessLumens(int brightnessLumens) { this.brightnessLumens = brightnessLumens; }

    public int getColourTempKelvin() { return colourTempKelvin; }
    public void setColourTempKelvin(int colourTempKelvin) { this.colourTempKelvin = colourTempKelvin; }
}
