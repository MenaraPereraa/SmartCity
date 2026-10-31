package lk.menara.smartcity360.patterns.decorator;

import lk.menara.smartcity360.exception.AuthenticationFailedException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

public class AuthenticationDecorator extends MeterStreamDecorator {
    private final String providedToken;
    private final String expectedToken;

    public AuthenticationDecorator(MeterStream innerStream, String providedToken, String expectedToken) {
        super(innerStream);
        this.providedToken = providedToken;
        this.expectedToken = Objects.requireNonNull(expectedToken, "Expected token is required");
    }

    @Override
    public String read() {
        if (providedToken == null || !MessageDigest.isEqual(
                providedToken.getBytes(StandardCharsets.UTF_8),
                expectedToken.getBytes(StandardCharsets.UTF_8))) {
            throw new AuthenticationFailedException("Invalid meter stream token");
        }
        return innerStream.read();
    }

    @Override
    protected String layerName() {
        return "Authentication";
    }
}
