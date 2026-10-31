package lk.menara.smartcity360.patterns.composite;

import java.util.ArrayList;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> da3b151e8a1de2211f81daef7f2563e22c505db6
import java.util.Collections;
import java.util.List;
import lk.menara.smartcity360.exception.ValidationException;
import lk.menara.smartcity360.model.enums.CommandType;
import lk.menara.smartcity360.model.enums.ZoneLevel;

public class ZoneComposite implements ZoneComponent {
    private final String name;
    private final ZoneLevel level;
    private final List<ZoneComponent> children = new ArrayList<>();

    public ZoneComposite(String name, ZoneLevel level) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Zone name is required");
        }
        if (level == null) {
            throw new ValidationException("Zone level is required");
        }
        this.name = name;
        this.level = level;
    }

    @Override
    public String getName() {
        return name;
    }

    public ZoneLevel getLevel() {
        return level;
    }

    public List<ZoneComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void add(ZoneComponent component) {
        if (component == null) {
            throw new ValidationException("Component is required");
        }
        if (component == this || (component instanceof ZoneComposite zone && zone.contains(this))) {
            throw new ValidationException("A zone cannot contain itself");
        }
        children.add(component);
    }

    public void remove(ZoneComponent component) {
        children.remove(component);
    }

    private boolean contains(ZoneComponent target) {
        for (ZoneComponent child : children) {
            if (child == target) {
                return true;
            }
            if (child instanceof ZoneComposite zone && zone.contains(target)) {
                return true;
            }
        }
        return false;
<<<<<<< HEAD
=======
import java.util.List;

public class ZoneComposite implements ZoneComponent {
    private String name;
    private List<ZoneComponent> zoneComponents = new ArrayList<>();

    public ZoneComposite(String name) {
        this.name = name;
    }

    public void add(ZoneComponent component) {
        zoneComponents.add(component);
    }

    public void remove(ZoneComponent component) {
        zoneComponents.remove(component);
    }

    @Override
    public void executeCommand(String command) {
        System.out.println("Zone: " + name + " broadcasting command -> " + command);
        for (ZoneComponent component : zoneComponents) {
            component.executeCommand(command);
        }
>>>>>>> main
    }

    @Override
    public double getResourceUsage() {
<<<<<<< HEAD
=======
    }

    @Override
    double getResourceUsage() {
>>>>>>> da3b151e8a1de2211f81daef7f2563e22c505db6
        double total = 0;
        for (ZoneComponent child : children) {
            total += child.getResourceUsage();
        }
        return total;
    }

    @Override
    public void executeCommand(CommandType command, List<String> log) {
        log.add(level + " [" + name + "] broadcasting " + command);
        for (ZoneComponent child : children) {
            child.executeCommand(command, log);
        }
    }
<<<<<<< HEAD
}
=======
        double totalUsage = 0;
        for (ZoneComponent component : zoneComponents) {
            totalUsage += component.getResourceUsage();
        }
        return totalUsage;
    }
}
>>>>>>> main
=======
}
>>>>>>> da3b151e8a1de2211f81daef7f2563e22c505db6
