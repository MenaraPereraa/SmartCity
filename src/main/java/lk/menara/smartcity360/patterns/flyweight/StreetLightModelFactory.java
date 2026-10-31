package lk.menara.smartcity360.patterns.flyweight;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class StreetLightModelFactory {
    private record ModelKey(String model, int watts, int brightnessLumens, int colourTempKelvin) {
    }

    private final Map<ModelKey, StreetLightModel> cache = new ConcurrentHashMap<>();

    public StreetLightModel get(String model, int watts, int brightnessLumens, int colourTempKelvin) {
        String name = model == null ? null : model.trim();
        ModelKey key = new ModelKey(name, watts, brightnessLumens, colourTempKelvin);
        return cache.computeIfAbsent(key,
                k -> new StreetLightModel(k.model(), k.watts(), k.brightnessLumens(), k.colourTempKelvin()));
    }

    public int size() {
        return cache.size();
    }
}
