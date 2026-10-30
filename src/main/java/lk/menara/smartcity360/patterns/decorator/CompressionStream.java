package lk.menara.smartcity360.patterns.decorator;

public class CompressionStream extends MeterStreamDecorator {
    public CompressionStream(MeterStream innerStream) {
        super(innerStream);
    }

    @Override
    public String read() {
        return "[COMPRESSED: " + innerStream.read() + "]";
    }

    @Override
    protected String layerName() {
        return "Compression";
    }
}