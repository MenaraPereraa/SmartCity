package lk.menara.smartcity360.patterns.decorator;

public class BasicMeterStream implements MeterStream {
    private final String rawData;

    public BasicMeterStream(String rawData) {
        this.rawData = rawData;
    }

    @Override
    public String read() {
        return rawData;
    }

    @Override
    public String describe() {
        return "BasicMeterStream";
    }
}
