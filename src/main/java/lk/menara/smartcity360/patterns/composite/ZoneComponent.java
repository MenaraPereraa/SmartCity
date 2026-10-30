package lk.menara.smartcity360.patterns.composite;

public interface ZoneComponent {
    void executeCommand(String command);
    double getResourceUsage();
}