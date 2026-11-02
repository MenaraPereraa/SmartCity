package lk.menara.smartcity360.dto;

import java.util.List;

public class StreamLogView {
    private final String createdAt;
    private final String meterCode;
    private final List<String> layers;

    public StreamLogView(String createdAt, String meterCode, List<String> layers) {
        this.createdAt = createdAt;
        this.meterCode = meterCode;
        this.layers = layers;
    }

    public String getCreatedAt() { return createdAt; }
    public String getMeterCode() { return meterCode; }
    public List<String> getLayers() { return layers; }
}
