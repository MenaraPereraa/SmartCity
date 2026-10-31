package lk.menara.smartcity360.patterns.decorator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import lk.menara.smartcity360.exception.StreamProcessingException;

public class CompressionDecorator extends MeterStreamDecorator {

    public CompressionDecorator(MeterStream innerStream) {
        super(innerStream);
    }

    @Override
    public String read() {
        // ඇතුළේ තියෙන ස්ට්‍රීම් එකෙන් ඩේටා අරගෙන, compress() මෙතඩ් එකට දීලා කුඩා කරලා එළියට දෙනවා.
        String rawData = innerStream.read();
        return compress(rawData);
    }

    @Override
    protected String layerName() {
        return "Compression";
    }

    // ==========================================
    // 1. ඩේටා කොම්ප්‍රෙස් (Compress) කරන හැටි
    // ==========================================
    public static String compress(String text) {
        try (
                // පියවර 1: ඩේටා තාවකාලිකව මෙම්මරි එකේ තබා ගැනීමට බෆර් එකක් සකස් කිරීම
                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                // පියවර 2: GZIP භාවිතයෙන් එම දත්ත ප්‍රමාණයෙන් කුඩා කිරීම
                GZIPOutputStream gzipOutput = new GZIPOutputStream(byteOutput)
        ) {
            // ලැබෙන text එක bytes වලට හරවා GZIP එකට ලිවීම
            gzipOutput.write(text.getBytes(StandardCharsets.UTF_8));
            gzipOutput.finish();

            // පියවර 3: කොම්ප්‍රෙස් කරපු බයිට් ඩේටා ටික පරිගණකයට කියවන්න ලේසි වෙන්න Base64 String එකක් බවට හැරවීම
            return Base64.getEncoder().encodeToString(byteOutput.toByteArray());

        } catch (IOException e) {
            throw new StreamProcessingException("Compression failed", e);
        }
    }

    // ==========================================
    // 2. කොම්ප්‍රෙස් කළ ඩේටා ආපසු සාමාන්‍ය තත්ත්වයට (Decompress) ගන්න හැටි
    // ==========================================
    public static String decompress(String encodedStr) {
        try {
            // පියවර 1: Base64 ස්ට්‍රින් එක ආපසු බයිට්ස් වලට ඩිෙකෝඩ් කිරීම
            byte[] decodedBytes = Base64.getDecoder().decode(encodedStr);

            // පියවර 2: GZIP ආධාරයෙන් අර බයිට්ස් ටික ආපසු මුල් තත්ත්වයට දිගහැරීම (Decompress)
            ByteArrayInputStream byteInput = new ByteArrayInputStream(decodedBytes);
            GZIPInputStream gzipInput = new GZIPInputStream(byteInput);

            // පියවර 3: දිගහැරලා ලැබෙන බයිට්ස් ටික නැවත කියවිය හැකි සාමාන්‍ය String එකක් බවට හැරවීම
            byte[] originalBytes = gzipInput.readAllBytes();
            return new String(originalBytes, StandardCharsets.UTF_8);

        } catch (IOException | IllegalArgumentException e) {
            throw new StreamProcessingException("Decompression failed", e);
        }
    }
}