package lk.menara.smartcity360.patterns.flyweight;

import lk.menara.smartcity360.exception.ValidationException;

/**
 * Context object: keeps the extrinsic (unique) state of one street light and a reference
 * to the shared {@link StreetLightModel} flyweight.
 */
public class StreetLight {
    private final String id;
    private final double latitude;
    private final double longitude;
    private final StreetLightModel model;
    private boolean on;

    public StreetLight(String id, double latitude, double longitude, StreetLightModel model) {
        if (id == null || id.isBlank()) {
            throw new ValidationException("Street light id is required");
        }
        if (latitude < -90 || latitude > 90) {
            throw new ValidationException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new ValidationException("Longitude must be between -180 and 180");
        }
        if (model == null) {
            throw new ValidationException("Street light model is required");
        }
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public StreetLightModel getModel() {
        return model;
    }

    public boolean isOn() {
        return on;
    }

    public void turnOn() {
        on = true;
    }

    public void turnOff() {
        on = false;
    }

    public String render() {
        return id + ": " + model.render(latitude, longitude, on);
    }
}
