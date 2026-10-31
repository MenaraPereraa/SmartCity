package lk.menara.smartcity360.patterns.decorator;

import com.google.crypto.tink.Aead;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Objects;
import lk.menara.smartcity360.exception.StreamProcessingException;


public class EncryptionDecorator extends MeterStreamDecorator {

    private static final byte[] ASSOCIATED_DATA =
            "SmartCity360-MeterStream".getBytes(StandardCharsets.UTF_8);

    private final Aead aead;

    public EncryptionDecorator(MeterStream innerStream, Aead aead) {
        super(innerStream);
        this.aead = Objects.requireNonNull(aead, "Tink Aead primitive is required");
    }

    @Override
    public String read() {
        return encrypt(innerStream.read(), aead);
    }

    @Override
    protected String layerName() {
        return "Encryption";
    }

    public static String encrypt(String plainText, Aead aead) {
        try {
            byte[] cipherText = aead.encrypt(plainText.getBytes(StandardCharsets.UTF_8), ASSOCIATED_DATA);
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (GeneralSecurityException e) {
            throw new StreamProcessingException("Encryption failed", e);
        }
    }

    public static String decrypt(String encoded, Aead aead) {
        try {
            byte[] cipherText = Base64.getDecoder().decode(encoded);
            byte[] plain = aead.decrypt(cipherText, ASSOCIATED_DATA);
            return new String(plain, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new StreamProcessingException("Decryption failed", e);
        }
    }
}