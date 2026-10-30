package lk.menara.smartcity360.patterns.composite;

public class Building implements ZoneComponent {
    private String name;
    private double powerUsage;

    public Building(String name, double powerUsage) {
        this.name = name;
        this.powerUsage = powerUsage;
    }

    @Override
    public void executeCommand(String command) {
        System.out.println("Building [" + name + "] executing command: " + command);
    }

    @Override
    public double getResourceUsage() {
        return this.powerUsage;
    }
}