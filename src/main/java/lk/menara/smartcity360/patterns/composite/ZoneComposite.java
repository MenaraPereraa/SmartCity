package lk.menara.smartcity360.patterns.composite;

import java.util.ArrayList;
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
    }

    @Override
    double getResourceUsage() {
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
}