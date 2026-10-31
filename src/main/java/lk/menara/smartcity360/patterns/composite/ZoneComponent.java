package lk.menara.smartcity360.patterns.composite;

import lk.menara.smartcity360.model.enums.CommandType;

import java.util.List;

public interface ZoneComponent {
    String getName();

    double getResourceUsage();

    void executeCommand(CommandType command, List<String> log);
}
