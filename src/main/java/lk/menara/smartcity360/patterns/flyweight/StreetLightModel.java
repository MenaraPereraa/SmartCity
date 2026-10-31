package lk.menara.smartcity360.patterns.flyweight;

import java.util.Locale;
import lk.menara.smartcity360.exception.ValidationException;

public final class StreetLightModel {
    private final String model;
    private final int watts;
    private final int brightnessLumens;
    private final int colourTempKelvin;

    StreetLightModel(String model, int watts, int brightnessLumens, int colourTempKelvin) {
        if (model == null || model.isBlank()) {
            throw new ValidationException("Street light model name is required");
        }
        if (watts <= 0) {
            throw new ValidationException("Watts must be positive");
        }
        if (brightnessLumens <= 0) {
            throw new ValidationException("Brightness must be positive");
        }
        if (colourTempKelvin <= 0) {
            throw new ValidationException("Colour temperature must be positive");
        }
        this.model = model;
        this.watts = watts;
        this.brightnessLumens = brightnessLumens;
        this.colourTempKelvin = colourTempKelvin;
    }

    public String getModel() {
        return model;
    }

    public int getWatts() {
        return watts;
    }

    public int getBrightnessLumens() {
        return brightnessLumens;
    }

    public int getColourTempKelvin() {
        return colourTempKelvin;
    }

    public double energyKwh(double hours) {
        if (hours < 0) {
            throw new ValidationException("Hours cannot be negative");
        }
        return watts * hours / 1000.0;
    }

    public String render(double latitude, double longitude, boolean on) {
        return String.format(Locale.ROOT, "%s %dW %dlm %dK at (%.5f, %.5f) is %s",
                model, watts, brightnessLumens, colourTempKelvin, latitude, longitude, on ? "ON" : "OFF");
    }
}
