package lk.menara.smartcity360.patterns.composite;

import java.util.ArrayList;
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
    }

    @Override
    public double getResourceUsage() {
        double totalUsage = 0;
        for (ZoneComponent component : zoneComponents) {
            totalUsage += component.getResourceUsage();
        }
        return totalUsage;
    }
}