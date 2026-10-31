package lk.menara.smartcity360.patterns.decorator;

import com.google.crypto.tink.Aead;

public final class StreamPipelineFactory {
    private StreamPipelineFactory() {
    }

    public static MeterStream build(MeterStream source, boolean compress, boolean encrypt, Aead aead,
                                    boolean authenticate, String providedToken, String expectedToken) {
        MeterStream stream = source;
        if (compress) {
            stream = new CompressionDecorator(stream);   // compress first
        }
        if (encrypt) {
            stream = new EncryptionDecorator(stream, aead); // then encrypt
        }
        if (authenticate) {
            stream = new AuthenticationDecorator(stream, providedToken, expectedToken); //  access check
        }
        return stream;
    }
}