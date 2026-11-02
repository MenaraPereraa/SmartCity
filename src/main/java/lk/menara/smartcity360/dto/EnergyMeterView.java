package lk.menara.smartcity360.dto;

public class EnergyMeterView {
    private final int id;
    private final String meterCode;
    private final String buildingName;
    private final boolean active;
    private final String latestKwh;
    private final String latestVoltage;
    private final String lastReadAt;

    public EnergyMeterView(int id, String meterCode, String buildingName, boolean active,
                           String latestKwh, String latestVoltage, String lastReadAt) {
        this.id = id;
        this.meterCode = meterCode;
        this.buildingName = buildingName;
        this.active = active;
        this.latestKwh = latestKwh;
        this.latestVoltage = latestVoltage;
        this.lastReadAt = lastReadAt;
    }

    public int getId() { return id; }
    public String getMeterCode() { return meterCode; }
    public String getBuildingName() { return buildingName; }
    public boolean isActive() { return active; }
    public String getLatestKwh() { return latestKwh; }
    public String getLatestVoltage() { return latestVoltage; }
    public String getLastReadAt() { return lastReadAt; }
}
