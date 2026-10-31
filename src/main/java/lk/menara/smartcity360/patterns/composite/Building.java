package lk.menara.smartcity360.patterns.composite;

import java.util.List;
import lk.menara.smartcity360.exception.ValidationException;
import lk.menara.smartcity360.model.enums.CommandType;
import lk.menara.smartcity360.model.enums.PowerState;

public class Building implements ZoneComponent {
    private final String name;
    private final double powerUsage;
    private PowerState powerState = PowerState.ON;

    public Building(String name, double powerUsage) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Building name is required");
        }
        if (powerUsage < 0) {
            throw new ValidationException("Power usage cannot be negative");
        }
        this.name = name;
        this.powerUsage = powerUsage;
    }

    @Override
    public String getName() {
        return name;
    }

    public PowerState getPowerState() {
        return powerState;
    }

    @Override
    public double getResourceUsage() {
        return powerState == PowerState.ON ? powerUsage : 0;
    }

    @Override
    public void executeCommand(CommandType command, List<String> log) {
        switch (command) {
            case SHUTDOWN_POWER -> {
                powerState = PowerState.OFF;
                log.add("Building [" + name + "] power OFF");
            }
            case RESTORE_POWER -> {
                powerState = PowerState.ON;
                log.add("Building [" + name + "] power ON");
            }
            case REPORT_USAGE -> log.add("Building [" + name + "] usage " + getResourceUsage() + " kWh");
        }
    }
}
