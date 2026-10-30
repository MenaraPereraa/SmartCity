package lk.menara.smartcity360.patterns.decorator;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class TestRun {
    public static void main(String[] args) throws Exception {
        String rawData = "KWH: 150, Voltage: 230";

        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        SecretKey key = generator.generateKey();
        String token = "CITY-TOKEN-360";

        MeterStream stream = new AuthenticationStream(
                new EncryptionStream(
                        new CompressionStream(
                                new BasicMeterStream(rawData)
                        ), key
                ), token, token
        );

        System.out.println("Layers: " + stream.describe());
        System.out.println("Read Data: " + stream.read());
    }
}