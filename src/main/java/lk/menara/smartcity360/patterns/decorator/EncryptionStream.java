package lk.menara.smartcity360.patterns.decorator;

import javax.crypto.SecretKey;

public class EncryptionStream extends MeterStreamDecorator {
    private final SecretKey key;

    public EncryptionStream(MeterStream innerStream, SecretKey key) {
        super(innerStream);
        this.key = key;
    }

    @Override
    public String read() {
        return "[ENCRYPTED: " + innerStream.read() + "]";
    }

    @Override
    protected String layerName() {
        return "Encryption";
    }
}