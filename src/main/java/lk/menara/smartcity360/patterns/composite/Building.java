package lk.menara.smartcity360.patterns.composite;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> da3b151e8a1de2211f81daef7f2563e22c505db6
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
<<<<<<< HEAD
=======
public class Building implements ZoneComponent {
    private String name;
    private double powerUsage;

    public Building(String name, double powerUsage) {
>>>>>>> main
=======
>>>>>>> da3b151e8a1de2211f81daef7f2563e22c505db6
        this.name = name;
        this.powerUsage = powerUsage;
    }

    @Override
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> da3b151e8a1de2211f81daef7f2563e22c505db6
    public String getName() {
        return name;
    }

    public PowerState getPowerState() {
        return powerState;
<<<<<<< HEAD
=======
    public void executeCommand(String command) {
        System.out.println("Building [" + name + "] executing command: " + command);
>>>>>>> main
=======
>>>>>>> da3b151e8a1de2211f81daef7f2563e22c505db6
    }

    @Override
    public double getResourceUsage() {
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> da3b151e8a1de2211f81daef7f2563e22c505db6
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
<<<<<<< HEAD
=======
        return this.powerUsage;
    }
}
>>>>>>> main
=======
>>>>>>> da3b151e8a1de2211f81daef7f2563e22c505db6
