package lk.menara.smartcity360.patterns.decorator;

public abstract class MeterStreamDecorator implements MeterStream {
    protected final MeterStream innerStream;

    protected MeterStreamDecorator(MeterStream innerStream) {
        this.innerStream = innerStream;
    }

    protected abstract String layerName();

    @Override
    public String read() {
        return innerStream.read();
    }

    @Override
    public String describe() {
        return innerStream.describe() + " -> " + layerName();
    }
}