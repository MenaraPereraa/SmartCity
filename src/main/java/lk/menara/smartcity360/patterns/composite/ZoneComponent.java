package lk.menara.smartcity360.patterns.composite;

import java.util.List;
import lk.menara.smartcity360.model.enums.CommandType;

public interface ZoneComponent {
    String getName();

    double getResourceUsage();

    void executeCommand(CommandType command, List<String> log);
}